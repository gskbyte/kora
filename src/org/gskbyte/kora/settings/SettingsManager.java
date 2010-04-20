package org.gskbyte.kora.settings;
import java.util.List;

import org.gskbyte.kora.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;

public class SettingsManager
{
    private static final String TAG = "SettingsManager";
    private static final String PREFERENCES_TAG = "KoraProfilesPreferences";
    private static final String PREFERENCE_LAST_USER_NAME = "lastUser";
    
    private static User sCurrentUser = null;
    private static UseProfile sCurrentUseProfile = null;
    private static DeviceProfile sCurrentDeviceProfile = null;
    
    private static SettingsDbAdapter sDbAdapter;
    
    private static Context sContext = null;
    private static Resources sResources = null;
    
    private static SharedPreferences sPreferences;
    
    public static class SettingsException extends Exception
    {
        private static final long serialVersionUID = 1L;
        
		public Class affected = null;
        public static final int NOT_SET = 1,
                                BAD = 2,
                                NOT_EXISTS = 3,
                                EXISTS = 4,
                                HAS_DEPENDENCIES = 5;
        public int type = NOT_SET;
        public List<String> dependencies = null;
        
        private SettingsException(){;}// para invalidarlo
        
        private SettingsException(int type, Class affected)
        {
            this.type = type;
            this.affected = affected;
        }
        /*
        private void addDependency(String dep)
        {
            if(dependencies==null)
                dependencies = new ArrayList<String>();
            dependencies.add(dep);
        }*/
        
        public String getMessage()
        {
            String ret = new String();
            ret += "Type: "+type+"\n";
            for(String d : dependencies)
                ret += d +", ";
            return ret;
        }
    }
    
    public static void init(Context context) throws SettingsException
    {
        sContext = context;
        sResources = context.getResources();
        
        // Cargar último usuario, si no existe, establecer Default
        User.setDefaultPhoto(sResources.getDrawable(R.drawable.icon_user),
                             sContext);
                
        sDbAdapter = new SettingsDbAdapter(sContext);
        sDbAdapter.open();
        
        /* Cargar configuración previa */
        sPreferences =  sContext.getSharedPreferences(PREFERENCES_TAG,
                                                      Context.MODE_PRIVATE);
        String lastUserName = sPreferences.getString(PREFERENCE_LAST_USER_NAME, "Default");
        
        /* Cargar el último usuario y sus perfiles de uso asociados*/
        setCurrentUser(lastUserName);
        
        /* CARGAR PERFILES DE USO */
        
        // cargar perfiles según los datos del usuario
        //UseProfileManager.init(res);
        //UseProfile up = UseProfileManager.load(u.getUseProfileName());
        
        //DeviceManager.init(res);
        //DeviceProfile dp = UseProfileManager.load(u.getUseProfileName());
    }
    
    public static void finish()
    {
        sDbAdapter.close();
        // Guardar en el fichero de preferencias y hacer commit
        SharedPreferences.Editor editor = sPreferences.edit();
        editor.putString(PREFERENCE_LAST_USER_NAME, sCurrentUser.getName());
        editor.commit();
    }
    
    public static boolean existsUser(String name)
    {
        try{
            getUser(name);
            return true;
        } catch(SettingsException e){
            return false;
        }
    }
    
    public static User getUser(String name) throws SettingsException
    {
        List<User> res = sDbAdapter.getUsers(
                SettingsDbAdapter.USER_NAME+"='"+name+"'");
        if(res.size()==1)
            return res.get(0);
        else
            throw new SettingsException(SettingsException.NOT_EXISTS, User.class);
    }
    
    public static List<User> getDefaultUsers()
    {
        return sDbAdapter.getUsers(
                SettingsDbAdapter.USER_ISDEFAULT + "=" + 1);
    }
    
    public static List<User> getCustomUsers()
    {
        return sDbAdapter.getUsers(
                SettingsDbAdapter.USER_ISDEFAULT + "=" + 0);
    }
    
    public static void addUser(User u) throws SettingsException
    {
        if(u == null || u.getName() == null || u.getName().length()==0)
            throw new SettingsException(SettingsException.BAD, User.class);
        // Comprobar que los perfiles son correctos!
        if(sDbAdapter.addUser(u) != SettingsDbAdapter.RESULT_OK)
            throw new SettingsException(SettingsException.EXISTS, User.class);
    }
    
    public static void editUser(String previous_name, User u) throws SettingsException
    {
        if(previous_name == null || previous_name.length()==0 ||
           u == null || u.getName() == null || u.getName().length()==0)
            throw new SettingsException(SettingsException.BAD, User.class);
        
        if(!previous_name.equals(u.getName()))
            if(existsUser(u.getName()))
                throw new SettingsException(SettingsException.EXISTS, User.class);
        removeUser(previous_name);
        addUser(u); // nunca lanzará excepción
        if(sCurrentUser.getName().equals(previous_name))
            sCurrentUser = u;
    }
    
    public static void removeUser(String name) throws SettingsException
    {
        if(sDbAdapter.removeUser(name) != SettingsDbAdapter.RESULT_OK)
            throw new SettingsException(SettingsException.NOT_EXISTS, User.class);
    }
    
    public static User getCurrentUser()
    {
        return sCurrentUser;
    }
    
    public static void setCurrentUser(String name) throws SettingsException
    {
        User u = getUser(name);
        /* ¡Cambiar perfiles */
        
        sCurrentUser = u;
        sCurrentUseProfile = getUseProfile(u.getUseProfileName());
        //sCurrentDeviceProfile = getDeviceProfile(u.getDeviceProfileName());
    }
    
    public static List<String> getUseProfilesList()
    {
        return sDbAdapter.getUseProfilesList();
    }
    
    public static boolean existsUseProfile(String name)
    {
        try{
            getUseProfile(name);
            return true;
        } catch(SettingsException e){
            return false;
        }
    }
    
    public static UseProfile getUseProfile(String name) throws SettingsException
    {
        List<UseProfile> res = sDbAdapter.getUseProfiles(
                SettingsDbAdapter.USEPROFILE_NAME+"='"+name+"'");
        if(res.size()==1)
            return res.get(0);
        else
            throw new SettingsException(SettingsException.NOT_EXISTS, User.class);
    }
    
    public static List<UseProfile> getDefaultUseProfiles()
    {
        return sDbAdapter.getUseProfiles(
                SettingsDbAdapter.USEPROFILE_ISDEFAULT + "=" + 1);
    }
    
    public static List<UseProfile> getCustomUseProfiles()
    {
        return sDbAdapter.getUseProfiles(
                SettingsDbAdapter.USEPROFILE_ISDEFAULT + "=" + 0);
    }
    
    public static void addUseProfile(UseProfile up) throws SettingsException
    {
        if(up == null || up.getName() == null || up.getName().length()==0)
            throw new SettingsException(SettingsException.BAD,
                    UseProfile.class);
        // Comprobar que los perfiles son correctos!
        if(sDbAdapter.addUseProfile(up) != SettingsDbAdapter.RESULT_OK)
            throw new SettingsException(SettingsException.EXISTS,
                    UseProfile.class);
    }
    
    public static void editUseProfile(String previous_name, UseProfile up) throws SettingsException
    {
        if(previous_name == null || previous_name.length()==0 ||
           up == null || up.getName() == null || up.getName().length()==0)
            throw new SettingsException(SettingsException.BAD, User.class);
        
        // If name is not changed, act normally
        boolean different_name = !previous_name.equals(up.getName());
        
        if(different_name)
            if(existsUseProfile(up.getName()))
                throw new SettingsException(SettingsException.EXISTS, User.class);
        // Delete previous configuration
        if(sDbAdapter.removeUseProfile(previous_name, false) != SettingsDbAdapter.RESULT_OK)
            throw new SettingsException(SettingsException.NOT_EXISTS,
                    UseProfile.class);
        // And add the new one
        addUseProfile(up);
        
        // If the name has changed, the users have to be modified
        List<User> users = sDbAdapter.getUsers(SettingsDbAdapter.USER_USEPROFILE+"='"+previous_name+"'");
        for(User u : users){
            u.setUseProfileName(up.name);
            editUser(u.name, u);
        }
        // If the use profile was the current, we have to change it
        if(sCurrentUseProfile.getName().equals(previous_name))
            sCurrentUseProfile = up;
        
    }
    
    public static void removeUseProfile(String name) throws SettingsException
    {
        if(sDbAdapter.removeUseProfile(name, true) == SettingsDbAdapter.QUERY_FAIL)
            throw new SettingsException(SettingsException.NOT_EXISTS,
                    UseProfile.class);
        else if(sDbAdapter.removeUseProfile(name, true) == SettingsDbAdapter.HAS_DEPENDENCIES)
            throw new SettingsException(SettingsException.HAS_DEPENDENCIES,
                    UseProfile.class);
            
    }
    
    public static UseProfile getCurrentUseProfile()
    {
        return sCurrentUseProfile;
    }
    
    public static List<String> getDeviceProfilesList()
    {
        return sDbAdapter.getDeviceProfilesList();
    }
    
    public static boolean existsDeviceProfile(String name)
    {
        try{
            getDeviceProfile(name);
            return true;
        } catch(SettingsException e){
            return false;
        }
    }
    
    public static DeviceProfile getDeviceProfile(String name) throws SettingsException
    {
        List<DeviceProfile> res = sDbAdapter.getDeviceProfiles(
                SettingsDbAdapter.DEVICEPROFILE_NAME+"='"+name+"'");
        if(res.size()==1)
            return res.get(0);
        else
            throw new SettingsException(SettingsException.NOT_EXISTS, User.class);
    }
    
    public static List<DeviceProfile> getDefaultDeviceProfiles()
    {
        return sDbAdapter.getDeviceProfiles(
                SettingsDbAdapter.DEVICEPROFILE_ISDEFAULT + "=" + 1);
    }
    
    public static List<DeviceProfile> getCustomDeviceProfiles()
    {
        return sDbAdapter.getDeviceProfiles(
                SettingsDbAdapter.DEVICEPROFILE_ISDEFAULT + "=" + 0);
    }
    
    public static void addDeviceProfile(DeviceProfile u) throws SettingsException
    {
    }
    
    public static void editDeviceProfile(String previous_name, DeviceProfile u) throws SettingsException
    {
        
    }
    
    public static void removeDeviceProfile(String name) throws SettingsException
    {
    }
    
    public static DeviceProfile getCurrentDeviceProfile()
    {
        return sCurrentDeviceProfile;
    }
}
