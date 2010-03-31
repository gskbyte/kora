package org.gskbyte.kora.settingsActivities.users;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.gskbyte.kora.R;
import org.gskbyte.kora.customViews.detailedListView.DetailedViewModel;
import org.gskbyte.kora.settings.SettingsManager;
import org.gskbyte.kora.settings.User;
import org.gskbyte.kora.settings.SettingsManager.SettingsException;
import org.gskbyte.kora.settingsActivities.ProfilesActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class UsersActivity extends ProfilesActivity
{
    private static final String TAG = "UsersActivity";
    
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        /* Terminar de ajustar vista */
        mAddButton.setText(mResources.getString(R.string.user));
        
        /* Iniciar manejadores, cargar usuario actual */
        try {
            mSettings = SettingsManager.getInstance();
            mCurrentUser = mSettings.getCurrentUser();
        } catch (SettingsException e) {
            Log.e(TAG, e.getMessage());
        }
        
        
        /* Iniciar listeners */
        selectProfileListener = new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position,
                    long id)
            {
                Intent intent = new Intent(UsersActivity.this,
                        SelectionActivity.class);
                
                mAdapter.setSelected(position);
                DetailedViewModel selected_model = 
                    (DetailedViewModel) mAdapter.getItem(position);
                mSelectedProfileName = selected_model.tag();
                intent.putExtra(TAG_USER_NAME, mSelectedProfileName);

                UsersActivity.this.startActivity(intent);
            }
    
        };
        
        addProfileListener = new OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(UsersActivity.this,
                                           AddEditActivity.class);
                startActivity(intent);
            }
        };
    }
    
    /* Rendimiento MUY mejorable: debería actualizar solo cuando hay cambios */
    public void updateListView()
    {
        String useProfile = mResources.getString(R.string.useProfile) + ": ",
               deviceProfile = mResources.getString(R.string.deviceProfile) + ": ";
        
        // Usuarios personalizados
        Collection<User> customUsers = (Collection<User>) mSettings.getCustomUsers();
        List<DetailedViewModel> customs = new ArrayList<DetailedViewModel>();
        for(User u : customUsers)
            customs.add(new DetailedViewModel(
                    u.getName(),
                    u.getName(),
                    useProfile + u.getUseProfileName()+ "\n" +
                        deviceProfile + u.getDeviceProfileName(),
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
                        useProfile + u.getUseProfileName()+ "\n" +
                            deviceProfile + u.getDeviceProfileName(),
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
}
