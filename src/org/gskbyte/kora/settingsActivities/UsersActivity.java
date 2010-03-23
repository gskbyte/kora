package org.gskbyte.kora.settingsActivities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.gskbyte.kora.R;
import org.gskbyte.kora.customViews.detailedListView.DetailedViewModel;
import org.gskbyte.kora.customViews.detailedListView.SectionedListAdapter;
import org.gskbyte.kora.settings.Profile;
import org.gskbyte.kora.settings.SettingsManager;
import org.gskbyte.kora.settings.User;
import org.gskbyte.kora.settings.SettingsManager.SettingsException;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;


public class UsersActivity extends ProfilesActivity
{
    private static final String TAG = "UsersActivity";
    
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        /* Iniciar manejadores */
        try {
            mSettings = SettingsManager.getInstance();
        } catch (SettingsException e) {
            Log.e(TAG, e.getMessage());
        }
        mCurrentProfile = mSettings.getCurrentUser();
        
        
        /* Ajustar vista */
        updateCurrentProfileView();
        updateProfilesList(true);
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
            try {
                u = (User) mSettings.getUser(mSelectedProfileName);
                dialog = new CopyUserDialog(this, u);
            } catch (SettingsException e) {
                Toast.makeText(this, 
                    "User not found: "+mSelectedProfileName+". Reset app.\n"+e.getMessage(),
                    Toast.LENGTH_LONG).show();
            }
            break;
            
        case EDIT_DIALOG_ID:
            try {
                u = (User) mSettings.getUser(mSelectedProfileName);
                dialog = new AddEditUserDialog(this, u);
            } catch (SettingsException e) {
                Toast.makeText(this, 
                    "User not found: "+mSelectedProfileName+". Reset app.\n"+e.getMessage(),
                    Toast.LENGTH_LONG).show();
            }
            break;
        case SELECT_DIALOG_ID:
            try {
                u = (User) mSettings.getUser(mSelectedProfileName);
                dialog = new SelectUserDialog(this, u);
            } catch (SettingsException e) {
                Toast.makeText(this, 
                    "User not found: "+mSelectedProfileName+". Reset app.\n"+e.getMessage(),
                    Toast.LENGTH_LONG).show();
            }
            break;
        case CONFIRM_DELETE_DIALOG_ID:
            dialog = createConfirmDeleteUserDialog();
            break;
        default:
            dialog = null;
        }

        return dialog;
    }
    
    protected void onPrepareDialog(int id, Dialog dialog)
    {
        User u;
        switch(id)
        {
        case ADD_DIALOG_ID:
            ((AddEditUserDialog)dialog).setUser(null);
            break;
        case COPY_DIALOG_ID:
            try {
                u = (User) mSettings.getUser(mSelectedProfileName);
                ((CopyUserDialog)dialog).setUser(u);
            } catch (SettingsException e) {
                Toast.makeText(this, 
                        "User not found: "+mSelectedProfileName+". Reset app.\n"+e.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
            break;
            
        case EDIT_DIALOG_ID:
            try {
                u = (User) mSettings.getUser(mSelectedProfileName);
                ((AddEditUserDialog)dialog).setUser(u);
            } catch (SettingsException e) {
                Toast.makeText(this, 
                        "User not found: "+mSelectedProfileName+". Reset app.\n"+e.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
            break;
        case SELECT_DIALOG_ID:
            try {
                u = (User) mSettings.getUser(mSelectedProfileName);
                ((SelectUserDialog)dialog).setUser(u);
            } catch (SettingsException e) {
                Toast.makeText(this, 
                        "User not found: "+mSelectedProfileName+". Reset app.\n"+e.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
            break;
        case CONFIRM_DELETE_DIALOG_ID:
            break;
        default:
            dialog = null;
        }
    }
    
    protected void updateCurrentProfileView()
    {
        mCurrentProfileTextView.setText(((User)mCurrentProfile).getName());
        mCurrentProfileImageView.setImageDrawable(((User)mCurrentProfile).getPhoto());
    }

    protected void updateProfilesList(boolean first_time)
    {
        // Usuarios personalizados
        Collection<User> customUsers = (Collection) mSettings.getCustomUsers();
        List<DetailedViewModel> customs = new ArrayList<DetailedViewModel>();
        for(User u : customUsers)
            customs.add(new DetailedViewModel(
                    u.getName(),
                    u.getName(),
                    u.getUseProfileName()+ "\n"+ u.getDeviceProfileName(),
                    u.getPhoto()));
        
        if(first_time){
            // Usuarios por defecto
            Collection<User> defaultUsers = (Collection) mSettings.getDefaultUsers();
            List<DetailedViewModel> defaults = new ArrayList<DetailedViewModel>();
            for(User u : defaultUsers)
                defaults.add(new DetailedViewModel(
                        u.getName(),
                        u.getName(),
                        u.getUseProfileName()+ "\n"+ u.getDeviceProfileName(),
                        u.getPhoto()));
            
            if(customs.size()>0)
                mAdapter.addSection(mResources.getString(R.string.customs), customs);
            mAdapter.addSection(mResources.getString(R.string.defaults), defaults);
        } else {
            mAdapter.removeSection(mResources.getString(R.string.customs));
            if(customs.size()>0)
                mAdapter.addSection(0, mResources.getString(R.string.customs), customs);
            mAdapter.notifyDataSetChanged();
        }
        
        mAdapter.notifyDataSetChanged();
    }
    
    public void chooseCurrentProfile()
    {
        try {
            mSettings.setCurrentUser(mSelectedProfileName);
            mCurrentProfile = mSettings.getCurrentUser();
            updateCurrentProfileView();
        } catch (SettingsException e) {
            Toast.makeText(this, 
                "User not found: "+mSelectedProfileName+". Reset app.\n"+e.getMessage(),
                Toast.LENGTH_LONG).show();
        }
    }

    public void addProfile(Profile p)
    {
    	User u = (User) p;
        try{
            mSettings.addUser(u);
            Toast.makeText(this, 
                    "Usuario añadido: "+u.getName(), Toast.LENGTH_SHORT).show();
            updateProfilesList(false);
        } catch (SettingsManager.SettingsException e){
            Toast.makeText(this, 
                    "Fallo al añadir el usuario "+u.getName(), Toast.LENGTH_SHORT).show();
        }
    }
    
    public void editProfile(String previous_name, Profile p)
    {
    	User u = (User) p;
        try{
            mSettings.editUser(previous_name, u);
            Toast.makeText(this, 
                    "Edición correcta: "+previous_name+" -> "+u.getName(), Toast.LENGTH_SHORT).show();
            if(previous_name.equals(((User)mCurrentProfile).getName())){
                mCurrentProfile = u;
                updateCurrentProfileView();
            }
            updateProfilesList(false);
        }catch (SettingsManager.SettingsException e){
            Toast.makeText(this,
                    "Edición incorrecta: "+previous_name+" -X> "+u.getName(), Toast.LENGTH_SHORT).show();
        }
    }
    
    public void deleteProfile(String name)
    {
        boolean ok = !((User)mCurrentProfile).getName().equals(name);
        if(ok){
            try{
                mSettings.removeUser(mSelectedProfileName);
                Toast.makeText(this, 
                        "Usuario borrado con éxito: "+mSelectedProfileName, Toast.LENGTH_SHORT).show();
                updateProfilesList(false);
            }catch (SettingsManager.SettingsException e){
                Toast.makeText(this,
                        "Se produjo un error al borrar el usuario: "+mSelectedProfileName, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, 
                    "Seleccione otro usuario actual antes de borrarlo.", Toast.LENGTH_SHORT).show();
        }
    }
    
    private Dialog createConfirmDeleteUserDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(mResources.getString(R.string.userDeletionQuestion)+ " " + mSelectedProfileName + "?")
               //.setIcon()
               .setCancelable(false)
               .setPositiveButton(mResources.getString(R.string.yes), new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                        deleteProfile(mSelectedProfileName);
                   }
               })
               .setNegativeButton(mResources.getString(R.string.no), new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                   }
               });
        return builder.create();
    }
    
    
}
