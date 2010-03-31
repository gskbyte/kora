package org.gskbyte.kora.settingsActivities.useProfiles;

import org.gskbyte.kora.R;
import org.gskbyte.kora.settings.SettingsManager;
import org.gskbyte.kora.settings.UseProfile;
import org.gskbyte.kora.settings.User;
import org.gskbyte.kora.settings.SettingsManager.SettingsException;
import org.gskbyte.kora.settingsActivities.ProfilesActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddEditActivity extends Activity
{
    private static final String TAG = "UseProfileAddEditActivity";
    
    private Resources mResources;
    private SettingsManager mSettings;
    private UseProfile mCurrentUseProfile;
    
    private EditText mNameEdit;
    private Button mInteractionButton, mVisualizationButton,
                   mFeedbackButton, mSoundButton;
    private Button mAcceptButton, mCancelButton;

    
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_LEFT_ICON);
        setContentView(R.layout.use_profile_add_edit);   
        
        /* Load resources and views */
        mResources = getResources();
        
        mNameEdit = (EditText) findViewById(R.id.useProfileNameEdit);
        
        mInteractionButton = (Button) findViewById(R.id.interactionButton);
        mVisualizationButton = (Button) findViewById(R.id.visualizationButton);
        mFeedbackButton = (Button) findViewById(R.id.feedbackButton);
        mSoundButton = (Button) findViewById(R.id.soundVoiceButton);
        
        mAcceptButton = (Button) findViewById(R.id.acceptButton);
        mCancelButton = (Button) findViewById(R.id.cancelButton);
        
        /* Add listeners */
        mInteractionButton.setOnClickListener(interactionListener);
        
        mAcceptButton.setOnClickListener(acceptListener);
        mCancelButton.setOnClickListener(cancelListener);

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
        mCurrentUseProfile = null;
        
        try {
            Bundle extras = getIntent().getExtras();
            if(extras != null){
                String name = extras.getString(ProfilesActivity.TAG_USER_NAME);
                mCurrentUseProfile = mSettings.getUseProfile(name);
            } else {
                mCurrentUseProfile = null;
            }
        } catch (SettingsException e) {
            Log.e(TAG, e.getMessage());
            Toast.makeText(this, 
                    "ERROR LOADING USER. Please contact author.",
                    Toast.LENGTH_LONG);
            finish();
        }
        
        setView();
    }
    
    public void setView()
    {
        if(mCurrentUseProfile == null){
            getWindow().setFeatureDrawableResource(Window.FEATURE_LEFT_ICON,
                                                   R.drawable.action_add);
            setTitle(R.string.addUseProfile);
            
            mNameEdit.setText("");
        } else {
            getWindow().setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, 
                                                   R.drawable.action_edit);
            setTitle(mResources.getString(R.string.editUseProfile)+ ": " +
                    mCurrentUseProfile.getName());
            
            mNameEdit.setText(mCurrentUseProfile.getName());
        }
    }
    
    private View.OnClickListener interactionListener = new View.OnClickListener(){
        @Override
        public void onClick(View v)
        {
            Intent i = new Intent(AddEditActivity.this, InteractionActivity.class);
            startActivity(i);
        }
    };

    
    private View.OnClickListener acceptListener = new View.OnClickListener(){
        @Override
        public void onClick(View v)
        {
            // 0 = OK, -1 = campos vacíos
            // valores de SettingsException = fallo
            int result = 0;
            
            String name = mNameEdit.getText().toString();
            
            if(name.length()>0)
            {
                UseProfile up = new UseProfile(name);
                
                if(mCurrentUseProfile == null){ // modo añadir
                    try{
                        mSettings.addUseProfile(up);
                    } catch (SettingsException e){
                        result = e.type;
                    }
                } else {
                    try{
                        mSettings.editUseProfile(mCurrentUseProfile.getName(),
                                                 up);
                    } catch (SettingsException e){
                        result = e.type;
                    }
                }
            } else {
                result = -1;
            }
            
            String s;
            switch(result)
            {
            case 0:
                if(mCurrentUseProfile == null){
                    s = mResources.getString(R.string.addUseProfileOk) + ": " + 
                        name;
                } else {
                    s = mResources.getString(R.string.editUseProfileOk) + " " +
                        name;
                }
                break;
            case -1:
                s = mResources.getString(R.string.emptyUseProfileNameFail);
                break;
            case SettingsException.EXISTS:
                s = mResources.getString(R.string.existingUseProfileFail) + ": " +
                    name;
                break;
            default:
                s = mResources.getString(R.string.settingsError);
                break;
            }
            
            Toast.makeText(AddEditActivity.this, s, Toast.LENGTH_SHORT).show();
            if(result==0)
                finish();
        }
    };
    
    private View.OnClickListener cancelListener = new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                finish();
            }
        };

}
