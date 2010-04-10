package org.gskbyte.kora.settingsActivities.users;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

import org.gskbyte.kora.R;
import org.gskbyte.kora.settings.SettingsManager;
import org.gskbyte.kora.settings.User;
import org.gskbyte.kora.settings.SettingsManager.SettingsException;
import org.gskbyte.kora.settingsActivities.ProfilesActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnFocusChangeListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class AddEditActivity extends Activity
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
    
    private String mPhotoPath;
    
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
            Bundle extras = getIntent().getExtras();
            if(extras != null){
                String userName = extras.getString(ProfilesActivity.TAG_USER_NAME);
                mCurrentUser = mSettings.getUser(userName);
                mPhotoPath = mCurrentUser.getPhotoPath();
            } else {
                //mCurrentUser = null;
                //mPhotoPath = null;
            }
        } catch (SettingsException e) {
            Log.e(TAG, e.getMessage());
            Toast.makeText(this, 
                    "ERROR LOADING SETTINGS. Please contact author.",
                    Toast.LENGTH_LONG);
        }
        setView();
    }
    
    protected void onActivityResult(int requestCode, int resultCode,
            Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        
        // Simplemente, sustituir el perfil de uso por el que me pasan
        if(requestCode == PHOTO_REQUEST_CODE){
            if(resultCode == Activity.RESULT_OK){
                Uri u = data.getData();
                Cursor c = managedQuery(u, null, null, null, null);
                if (c.moveToFirst()) {
                    String s = c.getString(1);

                    try {
                        Bitmap b = BitmapFactory.decodeFile(s);
                        Matrix matrix = new Matrix();
                        
                        int width  = b.getWidth(),
                            height = b.getHeight();
                        
                        matrix.postScale(128f/width, 128f/height);
                        b = Bitmap.createBitmap(b, 0, 0, width, height, matrix, true);
                        
                        boolean slash_found=false;
                        int dot_pos = -1;
                        int i = s.length()-1;
                        while(!slash_found){
                            switch(s.charAt(i)){
                            case '/':
                                slash_found=true;
                                break;
                            case '.':
                                dot_pos = i;
                            default:
                                --i;
                                break;
                            }
                        }
                        String filename = s.substring(i+1, dot_pos)+".png";
                        FileOutputStream fOut = openFileOutput(filename, MODE_WORLD_READABLE); 
                        
                        if(fOut!=null)
                            b.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                        else
                            Toast.makeText(this, "null", Toast.LENGTH_LONG).show();
                        
                        BitmapDrawable bmd = new BitmapDrawable(b);
                        mPhotoButton.setImageDrawable(bmd);
                        mPhotoPath = filename;
                        fOut.close();
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage());
                    }
                }
            }
        }
    }
    
    public void setView()
    {
        if(mCurrentUser == null){
            getWindow().setFeatureDrawableResource(Window.FEATURE_LEFT_ICON,
                                                   R.drawable.action_add);
            setTitle(R.string.addUser);
            
            mPhotoButton.setImageDrawable(User.getDefaultPhoto());
            mNameEdit.setText("");
            mSchoolEdit.setText("");

            populateSpinners();
            
        } else {
            getWindow().setFeatureDrawableResource(Window.FEATURE_LEFT_ICON,
                                                   R.drawable.action_edit);
            setTitle(mResources.getString(R.string.editUser)+ ": " +
                    mCurrentUser.getName());
            
            mPhotoButton.setImageDrawable(mCurrentUser.getPhoto());
            mNameEdit.setText(mCurrentUser.getName());
            mSchoolEdit.setText(mCurrentUser.getSchool());
            mAutoStartEdit.setText( Integer.toString(mCurrentUser.getAutoStartSeconds()) );
            
            populateSpinners();
            
            mAutoStartCheckBox.setChecked(mCurrentUser.wantsAutoStart());
        }
    }
    
    void populateSpinners()
    {
        // Pillar todos los perfiles de uso y de dispositivos
        List<String> useProfilesList = mSettings.getUseProfilesList();
        ArrayAdapter<String> uPAdapter =
            new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
                    useProfilesList);
        uPAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        mUseProfileSpinner.setAdapter(uPAdapter);
        
        List<String> deviceProfilesList = mSettings.getDeviceProfilesList();
        ArrayAdapter<String> dPAdapter =
            new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
                    deviceProfilesList);
        dPAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mDeviceProfileSpinner.setAdapter(dPAdapter);
        
        // Establecer el elemento elegido
        String useProfileName, deviceProfileName;
        if(mCurrentUser==null){
            useProfileName = deviceProfileName = "Default";
        } else {
            useProfileName = mCurrentUser.getUseProfileName();
            deviceProfileName = mCurrentUser.getDeviceProfileName();
        }
        
        int useProfilePos = 0;
        for(String s : useProfilesList){
            if(s.equals(useProfileName))
                break;
            ++useProfilePos;
        }
        mUseProfileSpinner.setSelection(useProfilePos);
        
        int deviceProfilePos = 0;
        for(String s : deviceProfilesList){
            if(s.equals(deviceProfileName))
                break;
            ++deviceProfilePos;
        }
        mDeviceProfileSpinner.setSelection(deviceProfilePos);
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
            startActivityForResult(intent, AddEditActivity.PHOTO_REQUEST_CODE);
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
                        Toast.makeText(AddEditActivity.this, 
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
                       useProfile = (String)mUseProfileSpinner.getAdapter().getItem(mUseProfileSpinner.getSelectedItemPosition()),
                       deviceProfile = (String)mDeviceProfileSpinner.getAdapter().getItem(mDeviceProfileSpinner.getSelectedItemPosition());
                boolean autoStart = mAutoStartCheckBox.isChecked();
                int seconds = 10;
                if(autoStart && timeString.length()>0){
                    seconds = Integer.parseInt(timeString);
                }
                
                if(name.length()>0 && school.length()>0)
                {
                    User u = new User(name, false, school, mPhotoPath,
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
