package org.gskbyte.kora.settingsActivities.useProfiles;

import org.gskbyte.kora.R;
import org.gskbyte.kora.settings.SettingsManager;
import org.gskbyte.kora.settings.UseProfile;
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
    private static final int ADD_MODE = 0, 
                             EDIT_MODE = 1;
    public static final String TAG_USE_PROFILE = "UseProfile";
    public static final int RESULT_TAG = 1;
    
    private int mMode;
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
        mVisualizationButton.setOnClickListener(visualizationListener);
        mFeedbackButton.setOnClickListener(feedbackListener);
        mSoundButton.setOnClickListener(soundListener);
        
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
        
        try {
            Bundle extras = getIntent().getExtras();
            if(extras != null){
                String name = extras.getString(ProfilesActivity.TAG_USEPROFILE_NAME);
                mCurrentUseProfile = mSettings.getUseProfile(name);
                mMode = EDIT_MODE;
            } else {
                mCurrentUseProfile = new UseProfile("temp");
                mMode = ADD_MODE;
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
    
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        
        // Simplemente, sustituir el perfil de uso por el que me pasan
        if(requestCode == RESULT_TAG && resultCode == Activity.RESULT_OK){
            Bundle extras = data.getExtras();
            mCurrentUseProfile = (UseProfile) extras.getSerializable(TAG_USE_PROFILE);
        }
        AddEditActivity.this.setVisible(true);
    }
    
    private void setView()
    {
        if(mMode == ADD_MODE){
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
            i.putExtra(TAG_USE_PROFILE, mCurrentUseProfile);
            AddEditActivity.this.setVisible(false);
            startActivityForResult(i, RESULT_TAG);
        }
    };
    
    private View.OnClickListener visualizationListener = new View.OnClickListener(){
        @Override
        public void onClick(View v)
        {
            Intent i = new Intent(AddEditActivity.this, VisualizationActivity.class);
            i.putExtra(TAG_USE_PROFILE, mCurrentUseProfile);
            AddEditActivity.this.setVisible(false);
            startActivityForResult(i, RESULT_TAG);
        }
    };
    
    private View.OnClickListener feedbackListener = new View.OnClickListener(){
        @Override
        public void onClick(View v)
        {
            Intent i = new Intent(AddEditActivity.this, FeedbackActivity.class);
            i.putExtra(TAG_USE_PROFILE, mCurrentUseProfile);
            AddEditActivity.this.setVisible(false);
            startActivityForResult(i, RESULT_TAG);
        }
    };
    
    private View.OnClickListener soundListener = new View.OnClickListener(){
        @Override
        public void onClick(View v)
        {
            Intent i = new Intent(AddEditActivity.this, SoundVoiceActivity.class);
            i.putExtra(TAG_USE_PROFILE, mCurrentUseProfile);
            AddEditActivity.this.setVisible(false);
            startActivityForResult(i, RESULT_TAG);
        }
    };

    
    private View.OnClickListener acceptListener =
        new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                // 0 = OK, -1 = campos vacíos
                // valores de SettingsException = fallo
                int result = 0;
                
                String name = mNameEdit.getText().toString();
                
                if(name.length()>0)
                {
                    String oldname = mCurrentUseProfile.getName();
                    mCurrentUseProfile.setName(name);
                    
                    if(mMode == ADD_MODE){ // modo añadir
                        try{
                            mSettings.addUseProfile(mCurrentUseProfile);
                        } catch (SettingsException e){
                            result = e.type;
                        }
                    } else {
                        try{
                            mSettings.editUseProfile(oldname,
                                                     mCurrentUseProfile);
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
                    if(mMode == ADD_MODE){
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
    
    private View.OnClickListener cancelListener =
        new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                finish();
            }
        };

}
