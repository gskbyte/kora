package org.gskbyte.kora.settingsActivities;

import org.gskbyte.kora.R;
import org.gskbyte.kora.settings.User;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class AddEditUserDialog extends AlertDialog
{
    private static final String TAG = "AddEditUserDialog";
    
    private static final int ADD_MODE = 0;
    private static final int EDIT_MODE = 1;
    
    private int mMode;
    private String mPreviousName;
    
    private UsersActivity mActivity;
    private Resources mResources;
    
    private ImageButton mPhotoButton;
    private EditText mNameEdit, mSchoolEdit, mAutoStartEdit;
    private Spinner mUseProfileSpinner, mDeviceProfileSpinner;
    private CheckBox mAutoStartCheckBox;
    
    public AddEditUserDialog(Context context, User user)
    {
        super(context);
        mActivity = (UsersActivity) context;
        mResources = context.getResources();
        
        View v = View.inflate(context, R.layout.add_edit_user_dialog, null);
        mPhotoButton = (ImageButton) v.findViewById(R.id.photoButton);
        mNameEdit = (EditText) v.findViewById(R.id.userNameEdit);
        mSchoolEdit = (EditText) v.findViewById(R.id.userSchoolEdit);
        mAutoStartEdit = (EditText) v.findViewById(R.id.autoStartEdit);
        
        mUseProfileSpinner = (Spinner) v.findViewById(R.id.useProfileSpinner);
        mDeviceProfileSpinner = (Spinner) v.findViewById(R.id.deviceProfileSpinner);
        
        mAutoStartCheckBox = (CheckBox) v.findViewById(R.id.autoStartCheckBox);

        mAutoStartCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener(){
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                {
                    mAutoStartEdit.setEnabled(isChecked);
                }
            });
        
        mAutoStartEdit.setOnFocusChangeListener(new OnFocusChangeListener(){
            public void onFocusChange(View v, boolean hasFocus)
                {
                    if(!hasFocus){
                        int seconds = Integer.parseInt(mAutoStartEdit.getText().toString());
                        if(seconds<5){
                            mAutoStartEdit.setText("5");
                            Toast.makeText(AddEditUserDialog.this.getContext(), 
                                    mResources.getString(R.string.minimumTime), 
                                    Toast.LENGTH_SHORT).show();
                        }
                    }                    
                }
                
            });
        
        
        setButton(BUTTON_NEGATIVE, mResources.getString(R.string.return_), new DialogInterface.OnClickListener() {
               public void onClick(DialogInterface dialog, int id) {
                    cancel();
               }
            });
        
        setButton(BUTTON_POSITIVE, mResources.getString(R.string.ok), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    User u = collectUserData();
                    if(mMode == ADD_MODE)
                        mActivity.addProfile(u);
                    else
                        mActivity.editProfile(mPreviousName, u);
                }
            });
        
        setUser(user);
        
        setView(v);
    }
    
    public void setUser(User user)
    {
        if(user == null){
            mMode = ADD_MODE;
            
            setIcon(R.drawable.action_add);
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
            
            setIcon(R.drawable.action_edit);
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
               // poner foto
               timeString = mAutoStartEdit.getText().toString(),
               useProfile = "Default",
               deviceProfile = "Default";
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
    
    
}
