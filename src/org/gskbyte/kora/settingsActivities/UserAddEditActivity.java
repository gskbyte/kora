package org.gskbyte.kora.settingsActivities;

import org.gskbyte.kora.R;
import org.gskbyte.kora.settings.SettingsManager;
import org.gskbyte.kora.settings.User;
import org.gskbyte.kora.settings.SettingsManager.SettingsException;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class UserAddEditActivity extends Activity
{
    private static final String TAG = "UserAddEditActivity";
    
    public static final int PHOTO_REQUEST_CODE = 1;
    private User mCurrentUser;
    
    private Resources mResources;
    private SettingsManager mSettings;
    
    private ImageButton mPhotoButton;
    private EditText mNameEdit, mSchoolEdit, mAutoStartEdit;
    private Spinner mUseProfileSpinner, mDeviceProfileSpinner;
    private CheckBox mAutoStartCheckBox;
    private Button mAcceptButton, mCancelButton;
    
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_LEFT_ICON);
        setContentView(R.layout.user_add_edit);
        
        mResources = getResources();
        
        mPhotoButton = (ImageButton) findViewById(R.id.photoButton);
        mNameEdit = (EditText) findViewById(R.id.userNameEdit);
        mSchoolEdit = (EditText) findViewById(R.id.userSchoolEdit);
        
        mUseProfileSpinner = (Spinner) findViewById(R.id.useProfileSpinner);
        mDeviceProfileSpinner = (Spinner) findViewById(R.id.deviceProfileSpinner);
        
        mAutoStartCheckBox = (CheckBox) findViewById(R.id.autoStartCheckBox);
        mAutoStartEdit = (EditText) findViewById(R.id.autoStartEdit);
        mAcceptButton = (Button) findViewById(R.id.acceptButton);
        mCancelButton = (Button) findViewById(R.id.cancelButton);
        
        mPhotoButton.setOnClickListener(photoButtonListener);
        mAutoStartCheckBox.setOnCheckedChangeListener(autoStartCheckListener);
        mAutoStartEdit.setOnFocusChangeListener(autoStartEditListener);
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
        if(mCurrentUser == null){
            getWindow().setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.action_add);
            setTitle(R.string.addUser);
            
            //mPhotoButton.setD
            mNameEdit.setText("");
            mSchoolEdit.setText("");

            populateSpinners();
            
        } else {
            getWindow().setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.action_edit);
            setTitle(mResources.getString(R.string.editUser)+ ": " +
                    mCurrentUser.getName());
            
            //mPhotoButton.set
            mNameEdit.setText(mCurrentUser.getName());
            mSchoolEdit.setText(mCurrentUser.getSchool());
            mAutoStartEdit.setText( Integer.toString(mCurrentUser.getAutoStartSeconds()) );
            
            populateSpinners();
            // indicar perfil!
            
            mAutoStartCheckBox.setChecked(mCurrentUser.wantsAutoStart());
        }
    }
    
    protected void populateSpinners()
    {
        
    }
    
    /* Listeners */
    
    private View.OnClickListener photoButtonListener = new View.OnClickListener(){
        @Override
        public void onClick(View v)
        {
            Intent intent = new Intent(Intent.ACTION_PICK);
            // tells your intent to get the contents
            // opens the URI for your image directory on your sdcard
            intent.setType("image/*"); 
            //AddEditUserActivity.this.startActivityForResult(intent,
            //                                 UsersActivity.PHOTO_REQUEST_CODE);
        }
    };
    
    private OnCheckedChangeListener autoStartCheckListener = new OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                mAutoStartEdit.setEnabled(isChecked);
            }
        };
    
    private OnFocusChangeListener autoStartEditListener = new OnFocusChangeListener(){
            public void onFocusChange(View v, boolean hasFocus)
            {
                if(!hasFocus){
                    int seconds = 10;
                    if(mAutoStartEdit.getText().length()>0){
                        seconds = Integer.parseInt(mAutoStartEdit.getText().toString());
                    } else {
                        mAutoStartEdit.setText(10);
                    }
                    
                    if(seconds<5){
                        mAutoStartEdit.setText("5");
                        Toast.makeText(UserAddEditActivity.this, 
                                mResources.getString(R.string.minimumTime), 
                                Toast.LENGTH_SHORT).show();
                    }
                }                    
            }
        };

    private View.OnClickListener acceptListener = new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                // 0 = OK, -1 = campos vacíos
                // valores de SettingsException = fallo
                int result = 0;
                
                String name = mNameEdit.getText().toString(),
                       school = mSchoolEdit.getText().toString(),
                       timeString = mAutoStartEdit.getText().toString(),
                       useProfile = "Default",
                       deviceProfile = "Default";
                //Drawable photo = mPhotoButton.getDrawable();
                boolean autoStart = mAutoStartCheckBox.isChecked();
                int seconds = 10;
                if(autoStart && timeString.length()>0){
                    seconds = Integer.parseInt(timeString);
                }

                
                if(name.length()>0 && school.length()>0)
                {
                    User u = new User(name, false, school, null,
                                      autoStart, seconds,
                                      useProfile, deviceProfile);
                    
                    if(mCurrentUser == null){ // modo añadir
                        try{
                            mSettings.addUser(u);
                        } catch (SettingsException e){
                            result = e.type;
                        }
                    } else {
                        try{
                            mSettings.editUser(mCurrentUser.getName(), u);
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
                    if(mCurrentUser == null){
                        s = mResources.getString(R.string.addUserOk) + ": " + 
                            name;
                    } else {
                        s = mResources.getString(R.string.editUserOk) + " " +
                            name;
                    }
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
                
                Toast.makeText(UserAddEditActivity.this, s, Toast.LENGTH_SHORT).show();
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
