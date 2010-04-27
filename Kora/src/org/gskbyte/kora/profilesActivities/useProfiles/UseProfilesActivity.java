package org.gskbyte.kora.profilesActivities.useProfiles;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.gskbyte.kora.R;
import org.gskbyte.kora.customViews.detailedListView.DetailedViewModel;
import org.gskbyte.kora.profiles.ProfilesManager;
import org.gskbyte.kora.profiles.UseProfile;
import org.gskbyte.kora.profiles.User;
import org.gskbyte.kora.profiles.ProfilesManager.SettingsException;
import org.gskbyte.kora.profilesActivities.ProfilesActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
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
    
    private User mCurrentUser;
    
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        /* Iniciar manejadores, cargar usuario actual */
        mCurrentUser = ProfilesManager.getCurrentUser();
        
        
        /* Iniciar listeners */
        selectProfileListener = new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position,
                    long id)
            {
                Intent intent = new Intent(UseProfilesActivity.this,
                        SelectionActivity.class);
                
                mAdapter.setSelected(position);
                DetailedViewModel selected_model = 
                    (DetailedViewModel) mAdapter.getItem(position);
                mSelectedProfileName = selected_model.tag();
                intent.putExtra(TAG_USEPROFILE_NAME, mSelectedProfileName);

                UseProfilesActivity.this.startActivity(intent);
            }
    
        };
        
        addProfileListener = new OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(UseProfilesActivity.this,
                                           AddEditActivity.class);
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
    
    public void updateListView()
    {
        /* See updateListView() on UsersActivity */
        Collection<UseProfile> customUseProfiles =
            (Collection<UseProfile>) ProfilesManager.getCustomUseProfiles();
        
        List<DetailedViewModel> customs = new ArrayList<DetailedViewModel>();
        for(UseProfile up : customUseProfiles)
            customs.add(new DetailedViewModel(
                    up.getName(),
                    up.getName(),
                    "",
                    null));
        
        String defaultsSectionName = mResources.getString(R.string.defaults);
        if(mAdapter.getSectionIndex(defaultsSectionName) == -1){
            Collection<UseProfile> defaultUseProfiles =
                (Collection<UseProfile>) ProfilesManager.getDefaultUseProfiles();
            List<DetailedViewModel> defaults = new ArrayList<DetailedViewModel>();
            for(UseProfile up : defaultUseProfiles)
                defaults.add(new DetailedViewModel(
                        up.getName(),
                        up.getName(),
                        "",
                        null));
            
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