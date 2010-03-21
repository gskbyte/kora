package org.gskbyte.kora.settingsActivities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.gskbyte.kora.R;
import org.gskbyte.kora.customViews.detailedListView.DetailedViewModel;
import org.gskbyte.kora.customViews.detailedListView.SectionedListAdapter;
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

public class UseProfilesActivity extends Activity
{
    private static final String TAG = "UseProfilesActivity";
    
    private Resources mResources;
    
    private ListView mListView;
    private TextView mTitle;
    private TextView mCurrentUserNameTextView;
    private ImageView mCurrentUserImageView;
    private Button mAddUseProfileButton;

    private SectionedListAdapter mAdapter;
    
    private SettingsManager mSettings;
    private User mCurrentUser;
    private UseProfile mCurrentUseProfile;

    private String mSelectedUseProfileName;
    
    /* Dialog IDs */
    private static final int ADD_USEPROFILE_DIALOG_ID = 0;
    private static final int EDIT_USEPROFILE_DIALOG_ID = 1;
    private static final int SELECT_USEPROFILE_DIALOG_ID = 2;
    private static final int CONFIRM_DELETE_USEPROFILE_DIALOG_ID = 3;
    
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_list);
        mResources = getResources();

        /* Cargar elementos de la vista */
        mListView = (ListView) findViewById(R.id.listView);
        mTitle = (TextView) findViewById(R.id.title);
        mCurrentUserNameTextView = (TextView) findViewById(R.id.text);
        mCurrentUserImageView = (ImageView) findViewById(R.id.image);
        mAddUseProfileButton = (Button) findViewById(R.id.addButton);
       
        /* Iniciar manejadores */
        try {
            mSettings = SettingsManager.getInstance();
        } catch (SettingsException e) {
            Log.e(TAG, e.getMessage());
        }
        mCurrentUser = mSettings.getCurrentUser();
        mCurrentUseProfile = mSettings.getCurrentUseProfile();
        
        /* Iniciar adaptadores de listas */
        //mCustomUsersAdapter = new DetailedListViewAdapter(this, R.layout.detailedlistview);
        //mDefaultUsersAdapter = new DetailedListViewAdapter(this, R.layout.detailedlistview);
        mAdapter=new SectionedListAdapter(this/*, R.layout.detailedlistview*/);
        
        /* Ajustar vista */
        mAddUseProfileButton.setText(mResources.getString(R.string.addUser));

        updateCurrentUseProfileView();
        updateUseProfilesList(true);
        mListView.setAdapter(mAdapter);
        
        
        /* Asociar eventos */
        //mListView.setOnItemClickListener(selectUserListener);
        //mAddUseProfileButton.setOnClickListener(addUseProfileListener);
    }

    protected Dialog onCreateDialog(int id)
    {
        return null;
    }
    
    protected void onPrepareDialog(int id, Dialog dialog)
    {
        
    }
    
    protected void updateCurrentUseProfileView()
    {
        mCurrentUserNameTextView.setText(mCurrentUser.getName());
        mCurrentUserImageView.setImageDrawable(mCurrentUser.getPhoto());
    }
    
    protected void updateUseProfilesList(boolean first_time)
    {
        // Usuarios personalizados
        Collection<UseProfile> customUseProfiles = (Collection) mSettings.getCustomUseProfiles();
        List<DetailedViewModel> customs = new ArrayList<DetailedViewModel>();
        for(UseProfile u : customUseProfiles)
            customs.add(new DetailedViewModel(
                    u.name,
                    u.name,
                    "",
                    null));
        
        if(first_time){
            // Usuarios por defecto
            Collection<UseProfile> defaultUseProfiles = (Collection) mSettings.getDefaultUseProfiles();
            List<DetailedViewModel> defaults = new ArrayList<DetailedViewModel>();
            for(UseProfile u : defaultUseProfiles)
                defaults.add(new DetailedViewModel(
                        u.name,
                        u.name,
                        "",
                        null));
            
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
}