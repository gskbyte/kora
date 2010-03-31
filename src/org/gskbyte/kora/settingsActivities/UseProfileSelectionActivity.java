package org.gskbyte.kora.settingsActivities;

import org.gskbyte.kora.R;
import org.gskbyte.kora.settings.SettingsManager;
import org.gskbyte.kora.settings.UseProfile;
import org.gskbyte.kora.settings.User;
import org.gskbyte.kora.settings.SettingsManager.SettingsException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

public class UseProfileSelectionActivity extends Activity
{
    private static final String TAG = "UserSelectionActivity";
    
    private Resources mResources;
    private SettingsManager mSettings;
    private User mCurrentUser;
    private UseProfile mSelectedUseProfile;
    private Button mChooseButton, mCopyButton, mEditButton, mDeleteButton,
                   mReturnButton;
    
    private Dialog mDeleteDialog = null;
    
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.use_profile_select);
        
        mResources = getResources();

        mChooseButton = (Button) findViewById(R.id.chooseButton);
        mCopyButton = (Button) findViewById(R.id.copyButton);
        mEditButton = (Button) findViewById(R.id.editButton);
        mDeleteButton = (Button) findViewById(R.id.deleteButton);
        mReturnButton = (Button) findViewById(R.id.returnButton);

        //mChooseButton.setOnClickListener(chooseListener);
        //mCopyButton.setOnClickListener(copyListener);
        //mEditButton.setOnClickListener(editListener);
        //mDeleteButton.setOnClickListener(deleteListener);
        mReturnButton.setOnClickListener(returnListener);
        
        try {
            mSettings = SettingsManager.getInstance();
        } catch (SettingsException e) {
            Log.e(TAG, e.getMessage());
            Toast.makeText(this, 
                    "ERROR LOADING SETTINGS. Please contact author.",
                    Toast.LENGTH_LONG);
        }
    }
    
    public void onStart()
    {
        super.onStart();
        
        
        try {
            Bundle extras = getIntent().getExtras();
            String useProfileName =  extras.getString(UseProfilesActivity.TAG_USEPROFILE_NAME);
            mCurrentUser = mSettings.getCurrentUser();
            mSelectedUseProfile = mSettings.getUseProfile(useProfileName);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            Toast.makeText(this, 
                    "ERROR LOADING USE PROFILE. Please contact author.",
                    Toast.LENGTH_LONG);
        }
        
        setView();
    }
    
    public void setView()
    {
        setTitle(mResources.getString(R.string.useProfile) + ": " +
                 mSelectedUseProfile.getName());

        mChooseButton.setText(mResources.getString(R.string.chooseUseProfileFor) +
                " " +mCurrentUser.getName());
        
        boolean isCustom = mSelectedUseProfile.isCustom();
        
        // Desactivar o hacer algo si están siendo usados
        mEditButton.setEnabled(isCustom);
        mDeleteButton.setEnabled(isCustom);
    }
    
    
    
    private android.view.View.OnClickListener returnListener = 
        new android.view.View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        };
}
