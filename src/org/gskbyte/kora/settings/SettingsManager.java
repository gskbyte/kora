package org.gskbyte.kora.settings;
import java.util.ArrayList;
import java.util.List;

import org.gskbyte.kora.R;

import android.content.Context;
import android.content.res.Resources;
public class SettingsManager
{
    private static final String TAG = "SettingsManager";
    
    private static User mCurrentUser = null;
    private static UseProfile mCurrentUseProfile = null;
    private static DeviceProfile mCurrentDeviceProfile = null;
    
    private static SettingsDbAdapter mDbAdapter;
    
    private static Context mContext = null;
    private static Resources mResources = null;
    
    private static SettingsManager instance = null;
    
    public final class SettingsException extends Exception
    {
        private static final long serialVersionUID = 1L;
        
        public Class affected = null;
        public static final int NOT_SET = 0,
                                BAD = 1,
                                NOT_EXISTS = 2,
                                EXISTS = 3,
                                HAS_DEPENDENCIES = 4;
        public int type = NOT_SET;
        public List<String> dependencies = null;
        
        private SettingsException(){;}// para invalidarlo
        
        public SettingsException(int type, Class affected)
        {
            this.type = type;
            this.affected = affected;
        }
        
        public void addDependency(String dep)
        {
            if(dependencies==null)
                dependencies = new ArrayList<String>();
            dependencies.add(dep);
        }
        
        public String getMessage()
        {
            String ret = new String();
            ret += "Type: "+type+"\n";
            for(String d : dependencies)
                ret += d +", ";
            return ret;
        }
    }
    
    protected SettingsManager() throws SettingsException
    {
        /* Cargar configuración previa */
        // cargar nombre de usuario del último
        String lastUser = "Default";
        
        /* Iniciar gestores con lo básico*/
        setCurrentUser(lastUser);
        
        // cargar perfiles según los datos del usuario
        //UseProfileManager.init(res);
        //UseProfile up = UseProfileManager.load(u.getUseProfileName());
        
        
        //DeviceManager.init(res);
        //DeviceProfile dp = UseProfileManager.load(u.getUseProfileName());

    }
    
    public static void init(Context context)
    {
        mContext = context;
        mResources = context.getResources();
        
        User.setDefaultPhoto(mResources.getDrawable(R.drawable.icon_user));
        
        mDbAdapter = new SettingsDbAdapter(mContext);
        mDbAdapter.open();
    }
    
    public static SettingsManager getInstance() throws SettingsException
    {
        if(instance == null)
            instance = new SettingsManager();
        return instance;
    }
    
    public boolean existsUser(String name)
    {
        try{
            getUser(name);
            return true;
        } catch(SettingsException e){
            return false;
        }
    }
    
    public User getUser(String name) throws SettingsException
    {
        List<User> res = mDbAdapter.getUsers(
                SettingsDbAdapter.USER_NAME+"='"+name+"'");
        if(res.size()==1)
            return res.get(0);
        else
            throw new SettingsException(SettingsException.NOT_EXISTS, User.class);
    }
    
    public List<User> getDefaultUsers()
    {
        return mDbAdapter.getUsers(
                SettingsDbAdapter.USER_ISDEFAULT + "=" + 1);
    }
    
    public List<User> getCustomUsers()
    {
        return mDbAdapter.getUsers(
                SettingsDbAdapter.USER_ISDEFAULT + "=" + 0);
    }
    
    public void addUser(User u) throws SettingsException
    {
        if(u == null || u.getName() == null || u.getName().length()==0)
            throw new SettingsException(SettingsException.BAD, User.class);
        // Comprobar que los perfiles son correctos!
        if(!mDbAdapter.addUser(u))
            throw new SettingsException(SettingsException.EXISTS, User.class);
    }
    
    public void editUser(String previous_name, User u) throws SettingsException
    {
        if(previous_name == null || previous_name.length()==0 ||
           u == null || u.getName() == null || u.getName().length()==0)
            throw new SettingsException(SettingsException.BAD, User.class);
        
        
        
        if(!previous_name.equals(u.getName()))
            if(existsUser(u.getName()))
                throw new SettingsException(SettingsException.EXISTS, User.class);
        removeUser(previous_name);
        addUser(u); // nunca lanzará excepción
    }
    
    public void removeUser(String name) throws SettingsException
    {
        if(!mDbAdapter.removeUser(name))
            throw new SettingsException(SettingsException.NOT_EXISTS, User.class);
    }
    
    public User getCurrentUser()
    {
        return mCurrentUser;
    }
    
    public void setCurrentUser(String name) throws SettingsException
    {
        User u = getUser(name);
        /* ¡Cambiar perfiles */
        
        // se hace después para evitar excepciones
        mCurrentUser = u;
    }
    
    
    public boolean existsUseProfile(String name)
    {
        try{
            getUseProfile(name);
            return true;
        } catch(SettingsException e){
            return false;
        }
    }
    
    public UseProfile getUseProfile(String name) throws SettingsException
    {
        List<UseProfile> res = mDbAdapter.getUseProfiles(
                SettingsDbAdapter.USEPROFILE_NAME+"='"+name+"'");
        if(res.size()==1)
            return res.get(0);
        else
            throw new SettingsException(SettingsException.NOT_EXISTS, User.class);
    }
    
    public List<UseProfile> getDefaultUseProfiles()
    {
        return mDbAdapter.getUseProfiles(
                SettingsDbAdapter.USEPROFILE_ISDEFAULT + "=" + 1);
    }
    
    public List<UseProfile> getCustomUseProfiles()
    {
        return mDbAdapter.getUseProfiles(
                SettingsDbAdapter.USEPROFILE_ISDEFAULT + "=" + 0);
    }
    
    public void addUseProfile(UseProfile u) throws SettingsException
    {
        /*if(u == null || u.getName() == null || u.getName().length()==0)
            throw new SettingsException(SettingsException.BAD, User.class);
        // Comprobar que los perfiles son correctos!
        if(!mDbAdapter.addUseProfile(u))
            throw new SettingsException(SettingsException.EXISTS, User.class);*/
    }
    
    public void editUseProfile(String previous_name, UseProfile u) throws SettingsException
    {
        /*if(previous_name == null || previous_name.length()==0 ||
           u == null || u.getName() == null || u.getName().length()==0)
            throw new SettingsException(SettingsException.BAD, User.class);
        
        
        
        if(!previous_name.equals(u.getName()))
            if(existsUser(u.getName()))
                throw new SettingsException(SettingsException.EXISTS, User.class);
        removeUser(previous_name);
        addUser(u); // nunca lanzará excepción*/
    }
    
    public void removeUseProfile(String name) throws SettingsException
    {
        /*if(!mDbAdapter.removeUseProfile(name))
            throw new SettingsException(SettingsException.NOT_EXISTS, User.class);*/
    }
    
    public UseProfile getCurrentUseProfile()
    {
        return mCurrentUseProfile;
    }
    
    
    public boolean existsDeviceProfile(String name)
    {
        try{
            getDeviceProfile(name);
            return true;
        } catch(SettingsException e){
            return false;
        }
    }
    
    public DeviceProfile getDeviceProfile(String name) throws SettingsException
    {
        List<DeviceProfile> res = mDbAdapter.getDeviceProfiles(
                SettingsDbAdapter.DEVICEPROFILE_NAME+"='"+name+"'");
        if(res.size()==1)
            return res.get(0);
        else
            throw new SettingsException(SettingsException.NOT_EXISTS, User.class);
    }
    
    public List<DeviceProfile> getDefaultDeviceProfiles()
    {
        return mDbAdapter.getDeviceProfiles(
                SettingsDbAdapter.DEVICEPROFILE_ISDEFAULT + "=" + 1);
    }
    
    public List<DeviceProfile> getCustomDeviceProfiles()
    {
        return mDbAdapter.getDeviceProfiles(
                SettingsDbAdapter.DEVICEPROFILE_ISDEFAULT + "=" + 0);
    }
    
    public void addDeviceProfile(DeviceProfile u) throws SettingsException
    {
    }
    
    public void editDeviceProfile(String previous_name, DeviceProfile u) throws SettingsException
    {
        
    }
    
    public void removeDeviceProfile(String name) throws SettingsException
    {
    }
    
    public DeviceProfile getCurrentDeviceProfile()
    {
        return mCurrentDeviceProfile;
    }
}
