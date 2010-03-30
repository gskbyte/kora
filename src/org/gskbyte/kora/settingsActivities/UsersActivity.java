package org.gskbyte.kora.settingsActivities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.gskbyte.kora.R;
import org.gskbyte.kora.customViews.detailedListView.DetailedViewModel;
import org.gskbyte.kora.settings.Profile;
import org.gskbyte.kora.settings.SettingsManager;
import org.gskbyte.kora.settings.User;
import org.gskbyte.kora.settings.SettingsManager.SettingsException;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class UsersActivity extends ProfilesActivity
{
    private static final String TAG = "UsersActivity";
    public static final String TAG_USER_NAME = "USER_NAME";

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        /* Iniciar manejadores, cargar usuario actual */
        try {
            mSettings = SettingsManager.getInstance();
            mCurrentProfile = mSettings.getCurrentUser();
        } catch (SettingsException e) {
            Log.e(TAG, e.getMessage());
        }
        /* Ajustar vista al usuario actual */
        mTitleText.setText(mResources.getString(R.string.current));
        mCurrentProfileText.setText(mCurrentProfile.getName());
        mCurrentProfileImage.setImageDrawable(((User)mCurrentProfile).getPhoto());
        
        /* Iniciar lista de perfiles */
        updateList();
        mListView.setAdapter(mAdapter);
        
        /* Asociar eventos */
        mListView.setOnItemClickListener(selectProfileListener);
        mAddButton.setOnClickListener(addProfileListener);
    }
    

    public void updateView()
    {
        updateList();
        // actualizar vista de usuario
        //mCurrentProfile = mSettings.getCurrentUser();
        //mCurrentProfileText.setText(mCurrentProfile.getName());
    }
    
    public void updateList()
    {
        // Usuarios personalizados
        Collection<User> customUsers = (Collection<User>) mSettings.getCustomUsers();
        List<DetailedViewModel> customs = new ArrayList<DetailedViewModel>();
        for(User u : customUsers)
            customs.add(new DetailedViewModel(
                    u.getName(),
                    u.getName(),
                    u.getUseProfileName()+ "\n"+ u.getDeviceProfileName(),
                    u.getPhoto()));
        
        String defaultsSectionName = mResources.getString(R.string.defaults);
        // Si no existe la sección de perfiles por defecto, es que no hay nada.
        // Crearlo todo entonces, en caso contrario borrar los personalizados
        // y meter los nuevos
        if(mAdapter.getSectionIndex(defaultsSectionName) == -1){
            // Usuarios por defecto
            Collection<User> defaultUsers = (Collection<User>) mSettings.getDefaultUsers();
            List<DetailedViewModel> defaults = new ArrayList<DetailedViewModel>();
            for(User u : defaultUsers)
                defaults.add(new DetailedViewModel(
                        u.getName(),
                        u.getName(),
                        u.getUseProfileName()+ "\n"+ u.getDeviceProfileName(),
                        u.getPhoto()));
            
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
    
    
    protected OnItemClickListener selectProfileListener = new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position,
                    long id)
            {
                Intent intent = new Intent(UsersActivity.this,
                        UserSelectionActivity.class);
                
                mAdapter.setSelected(position);
                DetailedViewModel selected_model = 
                    (DetailedViewModel) mAdapter.getItem(position);
                mSelectedProfileName = selected_model.tag();
                intent.putExtra(TAG_USER_NAME, mSelectedProfileName);

                UsersActivity.this.startActivity(intent);
            }
    
        };
    
    protected OnClickListener addProfileListener = new OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(UsersActivity.this,
                                           UserAddEditActivity.class);
                UsersActivity.this.startActivity(intent);
            }
        };

    
    
    
}
