package org.gskbyte.kora.settingsActivities.users;

import org.gskbyte.kora.R;
import org.gskbyte.kora.settings.SettingsManager;
import org.gskbyte.kora.settings.User;
import org.gskbyte.kora.settings.SettingsManager.SettingsException;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class CopyActivity extends Activity
{
    private static final String TAG = "UserCopyActivity";
    private User mCurrentUser;
    
    private Resources mResources;
    private SettingsManager mSettings;
    
    private ImageButton mPhotoButton;
    private EditText mNameEdit, mSchoolEdit;
    private TextView mUseProfileText, mDeviceProfileText, mAutoStartText;
    private Button mAcceptButton, mCancelButton;

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
        
        mAcceptButton = (Button) findViewById(R.id.acceptButton);
        mCancelButton = (Button) findViewById(R.id.cancelButton);

        mAcceptButton.setOnClickListener(acceptListener);
        mCancelButton.setOnClickListener(cancelListener);

        
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
        mNameEdit.setText("");
        mSchoolEdit.setText(mCurrentUser.getSchool()); // rellenar o dejar blanco?
        
        mUseProfileText.setText(mResources.getString(R.string.useProfile) +
                                ": " + mCurrentUser.getUseProfileName());
        mDeviceProfileText.setText(mResources.getString(R.string.deviceProfile) +
                                   ": " + mCurrentUser.getDeviceProfileName());
        String autoText = mResources.getString(R.string.autoStart) + ": ";
        if(mCurrentUser.wantsAutoStart()){
            autoText += mResources.getString(R.string.yes) +" (" + 
                        mCurrentUser.getAutoStartSeconds() + " " +
                        mResources.getString(R.string.seconds) +  ")";
        } else {
            autoText += mResources.getString(R.string.no);
        }
        mAutoStartText.setText(autoText);
    }
    
    
    private View.OnClickListener acceptListener =
        new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                // 0 = OK, -1 = campos vacíos
                // valores de SettingsException = fallo
                int result = 0;
                
                String name = mNameEdit.getText().toString(),
                       school = mSchoolEdit.getText().toString();
                
                if(name.length()>0 && school.length()>0)
                {
                    User u = new User(name, false, school, null, 
                            mCurrentUser.wantsAutoStart(),
                            mCurrentUser.getAutoStartSeconds(),
                            mCurrentUser.getUseProfileName(),
                            mCurrentUser.getDeviceProfileName());
                    
                    try{
                        mSettings.addUser(u);
                        Toast.makeText(CopyActivity.this, 
                                mResources.getString(R.string.addUserOk) +
                                ": "+u.getName(), Toast.LENGTH_SHORT).show();
                    } catch (SettingsManager.SettingsException e){
                        result = e.type;
                    }
                } else {
                    result = -1;
                }
                
                String s;
                switch(result)
                {
                case 0:
                    s = mResources.getString(R.string.addUserOk) + ": " + 
                        name;
                    break;
                case -1:
                    s = mResources.getString(R.string.emptyUserNameAndSchoolFail);
                    break;
                case SettingsException.EXISTS:
                    s = mResources.getString(R.string.existingUserFail) + ": " +
                        name;
                    break;
                default:
                    s = mResources.getString(R.string.settingsError);
                    break;
                }
                
                Toast.makeText(CopyActivity.this, s, Toast.LENGTH_SHORT).show();
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
