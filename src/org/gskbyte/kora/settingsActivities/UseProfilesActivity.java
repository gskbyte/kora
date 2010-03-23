package org.gskbyte.kora.settingsActivities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.gskbyte.kora.R;
import org.gskbyte.kora.customViews.detailedListView.DetailedViewModel;
import org.gskbyte.kora.customViews.detailedListView.SectionedListAdapter;
import org.gskbyte.kora.settings.Profile;
import org.gskbyte.kora.settings.SettingsManager;
import org.gskbyte.kora.settings.UseProfile;
import org.gskbyte.kora.settings.User;
import org.gskbyte.kora.settings.SettingsManager.SettingsException;

import android.app.Activity;
import android.app.Dialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class UseProfilesActivity extends ProfilesActivity
{
    private static final String TAG = "UseProfilesActivity";
    private User mCurrentUser;
    
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        /* Iniciar manejadores, cargar usuario y perfil de uso asociado */
        try {
            mSettings = SettingsManager.getInstance();
            mCurrentUser = mSettings.getCurrentUser();
            mCurrentProfile = mSettings.getUseProfile(mCurrentUser.getUseProfileName());
        } catch (SettingsException e) {
            Log.e(TAG, e.getMessage());
        }
        
        /* Ajustar vista al usuario y perfiles actuales*/
        mTitle = mResources.getString(R.string.currentUseProfile) + " " + 
                 mCurrentUser.getName() + ":";
        mCurrentProfileText.setText(mCurrentUser.getName());
        mCurrentProfileImage.setImageDrawable(mCurrentUser.getPhoto());
        
        /* Iniciar lista de perfiles */
        updateList();
        mListView.setAdapter(mAdapter);
    }

    protected Dialog onCreateDialog(int id)
    {
        Dialog dialog=null;
        User u;
        switch(id)
        {
        case ADD_DIALOG_ID:
            dialog = new AddEditUserDialog(this, null);
            break;
        case COPY_DIALOG_ID:
            /*try {
                u = (User) mSettings.getUser(mSelectedProfileName);
                dialog = new CopyUserDialog(this, u);
            } catch (SettingsException e) {
                Toast.makeText(this, 
                    "User not found: "+mSelectedProfileName+". Reset app.\n"+e.getMessage(),
                    Toast.LENGTH_LONG).show();
            }*/
            break;
            
        case EDIT_DIALOG_ID:
            /*try {
                u = (User) mSettings.getUser(mSelectedProfileName);
                dialog = new AddEditUserDialog(this, u);
            } catch (SettingsException e) {
                Toast.makeText(this, 
                    "User not found: "+mSelectedProfileName+". Reset app.\n"+e.getMessage(),
                    Toast.LENGTH_LONG).show();
            }*/
            break;
        case SELECT_DIALOG_ID:
            /*try {
                u = (User) mSettings.getUser(mSelectedProfileName);
                dialog = new SelectUserDialog(this, u);
            } catch (SettingsException e) {
                Toast.makeText(this, 
                    "User not found: "+mSelectedProfileName+". Reset app.\n"+e.getMessage(),
                    Toast.LENGTH_LONG).show();
            }*/
            break;
        case CONFIRM_DELETE_DIALOG_ID:
            /*dialog = createConfirmDeleteUserDialog();*/
            break;
        default:
            dialog = null;
        }

        return dialog;
    }
    
    protected void onPrepareDialog(int id, Dialog dialog)
    {
        
    }
    
    public void updateList()
    {
        // Perfiles personalizados
        Collection<UseProfile> customUseProfiles =
            (Collection<UseProfile>) mSettings.getCustomUseProfiles();
        List<DetailedViewModel> customs = new ArrayList<DetailedViewModel>();
        for(UseProfile u : customUseProfiles)
            ;
            /*customs.add(new DetailedViewModel(
                    u.getName(),
                    u.getName(),
                    u.getUseProfileName()+ "\n"+ u.getDeviceProfileName(),
                    u.getPhoto()));
             */
        String defaultsSectionName = mResources.getString(R.string.defaults);
        if(mAdapter.getSectionIndex(defaultsSectionName) == -1){
            // Usuarios por defecto
            Collection<UseProfile> defaultUsers =
                (Collection<UseProfile>) mSettings.getDefaultUseProfiles();
            List<DetailedViewModel> defaults = new ArrayList<DetailedViewModel>();
            for(UseProfile u : defaultUsers)
                ;
                /*defaults.add(new DetailedViewModel(
                        u.getName(),
                        u.getName(),
                        u.getUseProfileName()+ "\n"+ u.getDeviceProfileName(),
                        u.getPhoto()));
                 */
            if(customs.size()>0)
                mAdapter.addSection(mResources.getString(R.string.customs), customs);
            mAdapter.addSection(defaultsSectionName, defaults);
        } else {
            mAdapter.removeSection(mResources.getString(R.string.customs));
            if(customs.size()>0)
                mAdapter.addSection(0, mResources.getString(R.string.customs), customs);
        }
        
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void addProfile(Profile p)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void chooseCurrentProfile()
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void deleteProfile(String id)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void editProfile(String previousId, Profile p)
    {
        // TODO Auto-generated method stub
        
    }
}