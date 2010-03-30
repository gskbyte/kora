package org.gskbyte.kora.settingsActivities;

import org.gskbyte.kora.R;
import org.gskbyte.kora.settings.User;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
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
    private static final String TAG = "AddEditUserActivity";
    
    public static final String ARGUMENT_USER_TAG = "org.gskbyte.kora.AddEditUser";
    public static final String RESULT_TAG = "org.gskbyte.kora.AddEditUser";

    
    public static final int PHOTO_REQUEST_CODE = 1;
    
    private static final int ADD_MODE = 0;
    private static final int EDIT_MODE = 1;
    
    private int mMode;
    private String mPreviousName;
    
    private Resources mResources;
    
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
        Toast.makeText(this, "CREATE", Toast.LENGTH_SHORT).show();
        
        //savedInstanceState.getSerializable(key)
    }
    
    public void onStart()
    {
        super.onStart();
        Toast.makeText(this, "START", Toast.LENGTH_LONG).show();

        setUser(null);
    }
    
    public void setUser(User user)
    {
        if(user == null){
            mMode = ADD_MODE;
            
            getWindow().setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.action_add);
            setTitle(R.string.addUser);
            
            //mPhotoButton.setD
            mNameEdit.setText("");
            mSchoolEdit.setText("");
            //mAutoStartEdit.setText("5");

            populateSpinners();
            
            //mAutoStartCheckBox.setChecked(true);
            //mAutoStartEdit.setText("5");
            
        } else {
            mMode = EDIT_MODE;
            mPreviousName = user.getName();
            
            getWindow().setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.action_edit);
            setTitle(R.string.editUser);
            
            //mPhotoButton.set
            mNameEdit.setText(user.getName());
            mSchoolEdit.setText(user.getSchool());
            mAutoStartEdit.setText( Integer.toString(user.getAutoStartSeconds()) );
            
            populateSpinners();
            
            mAutoStartCheckBox.setChecked(user.wantsAutoStart());
        }
    }
    
    protected User collectUserData()
    {
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
        
        User ret = new User(name, false,
                            school, null,
                            autoStart, seconds,
                            useProfile, deviceProfile);
        return ret;
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
                Intent i = new Intent();
                User u = collectUserData();
                i.putExtra(RESULT_TAG, u);
                setResult(RESULT_OK, i);
                finish();
            }
        };
    private View.OnClickListener cancelListener = new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                setResult(RESULT_CANCELED);
                finish();
            }
        };

}
