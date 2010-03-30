package org.gskbyte.kora.settingsActivities;

import org.gskbyte.kora.R;
import org.gskbyte.kora.settings.SettingsManager;
import org.gskbyte.kora.settings.User;
import org.gskbyte.kora.settings.SettingsManager.SettingsException;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class UserCopyActivity extends Activity
{
    private static final String TAG = "UserCopyActivity";
    private User mCurrentUser;
    
    private Resources mResources;
    private SettingsManager mSettings;
    
    private ImageButton mPhotoButton;
    private EditText mNameEdit, mSchoolEdit;
    private TextView mUseProfileText, mDeviceProfileText, mAutoStartText;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_LEFT_ICON);
        setContentView(R.layout.user_copy);
        
        mResources = getResources();
        
        mPhotoButton = (ImageButton) findViewById(R.id.photoButton);
        mNameEdit = (EditText) findViewById(R.id.userNameEdit);
        mSchoolEdit = (EditText) findViewById(R.id.userSchoolEdit);
        
        mUseProfileText = (TextView) findViewById(R.id.useProfileText);
        mDeviceProfileText = (TextView) findViewById(R.id.deviceProfileText);
        mAutoStartText = (TextView) findViewById(R.id.autoStartText);

        
        try {
            Bundle extras = getIntent().getExtras();
            
            mSettings = SettingsManager.getInstance();
            if(extras != null){
                String userName = extras.getString(UsersActivity.TAG_USER_NAME);
                mCurrentUser = mSettings.getUser(userName);
            } else {
                mCurrentUser = null;
            }
        } catch (SettingsException e) {
            Log.e(TAG, e.getMessage());
            Toast.makeText(this, 
                    "ERROR LOADING USER. Please contact author.",
                    Toast.LENGTH_LONG);
        }
        
        setView();
    }
    public void setView()
    {
    }
    
}
