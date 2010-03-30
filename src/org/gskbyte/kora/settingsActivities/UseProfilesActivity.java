package org.gskbyte.kora.settingsActivities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.gskbyte.kora.R;
import org.gskbyte.kora.customViews.detailedListView.DetailedViewModel;
import org.gskbyte.kora.settings.SettingsManager;
import org.gskbyte.kora.settings.UseProfile;
import org.gskbyte.kora.settings.User;
import org.gskbyte.kora.settings.SettingsManager.SettingsException;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class UseProfilesActivity extends ProfilesActivity
{
    private static final String TAG = "UsersActivity";
    public static final String TAG_USEPROFILE_NAME = "USEPROFILE_NAME";
    
    private User mCurrentUser;
    private UseProfile mCurrentUseProfile;
    
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        /* Iniciar manejadores, cargar usuario actual */
        try {
            mSettings = SettingsManager.getInstance();
            mCurrentUser = mSettings.getCurrentUser();
            //mCurrentUseProfile = mSettings.getCurrentUseProfile();
        } catch (SettingsException e) {
            Log.e(TAG, e.getMessage());
        }
        
        
        /* Iniciar listeners */
        selectProfileListener = new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position,
                    long id)
            {
                /*Intent intent = new Intent(UsersActivity.this,
                        UserSelectionActivity.class);
                
                mAdapter.setSelected(position);
                DetailedViewModel selected_model = 
                    (DetailedViewModel) mAdapter.getItem(position);
                mSelectedProfileName = selected_model.tag();
                intent.putExtra(TAG_USER_NAME, mSelectedProfileName);

                UsersActivity.this.startActivity(intent);*/
            }
    
        };
        
        addProfileListener = new OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(UseProfilesActivity.this,
                                           UseProfileAddEditActivity.class);
                startActivity(intent);
            }
        };

        /* Ajustar vista al usuario actual */
        mAddButton.setText(mResources.getString(R.string.useProfile));
        mCurrentUserText.setText(mCurrentUser.getName());
        mCurrentUserPhoto.setImageDrawable(mCurrentUser.getPhoto());
        mCurrentUseProfileText.setText(mCurrentUser.getUseProfileName());
        mCurrentDeviceProfileText.setText(mCurrentUser.getDeviceProfileName());
    }
    
    public void onStart()
    {
        super.onStart();
        Toast.makeText(this, "START", Toast.LENGTH_SHORT);
    }
    
    public void onResume()
    {
        super.onResume();
        Toast.makeText(this, "RESUME", Toast.LENGTH_LONG);
    }

    public void updateCurrentProfileView()
    {
        /*User c = mSettings.getCurrentUser();
        if( !c.getName().equals(mCurrentProfile.getName()) ){
            mCurrentProfile = c;
            mCurrentProfileText.setText(c.getName());
            mCurrentProfileImage.setImageDrawable(c.getPhoto());
        }*/
    }
    
    /* Rendimiento MUY mejorable: debería actualizar solo cuando hay cambios */
    public void updateListView()
    {
        
    }

}