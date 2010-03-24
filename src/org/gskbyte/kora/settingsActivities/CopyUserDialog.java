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
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class CopyUserDialog extends AlertDialog
{
    private static final String TAG = "CopyUserDialog";

    private UsersActivity mActivity;
    private Resources mResources;
    
    private ImageButton mPhotoButton;
    private EditText mNameEdit, mSchoolEdit;
    private TextView mUseProfileText, mDeviceProfileText, mAutoStartText;
    private User ret;

    public CopyUserDialog(Context context, User user)
    {
        super(context);
        mActivity = (UsersActivity) context;
        mResources = context.getResources();
        
        View v = View.inflate(context, R.layout.copy_user_dialog, null);
        mPhotoButton = (ImageButton) v.findViewById(R.id.photoButton);
        mNameEdit = (EditText) v.findViewById(R.id.userNameEdit);
        mSchoolEdit = (EditText) v.findViewById(R.id.userSchoolEdit);
        
        mUseProfileText = (TextView) v.findViewById(R.id.useProfileText);
        mDeviceProfileText = (TextView) v.findViewById(R.id.deviceProfileText);
        mAutoStartText = (TextView) v.findViewById(R.id.autoStartText);
        
        setButton(BUTTON_NEGATIVE, mResources.getString(R.string.return_), new DialogInterface.OnClickListener() {
               public void onClick(DialogInterface dialog, int id) {
                    cancel();
               }
            });
        
        setButton(BUTTON_POSITIVE, mResources.getString(R.string.ok), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    ret.setName(mNameEdit.getText().toString());
                    ret.setSchool(mSchoolEdit.getText().toString());
                    // poner foto
                    
                    //mActivity.addProfile(ret);
                }
            });
        
        setUser(user);
        
        setView(v);
    }
    
    public void setUser(User user)
    {
        // Iniciar campos
        //mPhotoButton.setD
        mNameEdit.setText("");
        mSchoolEdit.setText(user.getSchool()); // rellenar o dejar blanco?
        
        mUseProfileText.setText(mResources.getString(R.string.useProfile) + ": " + user.getUseProfileName());
        mDeviceProfileText.setText(mResources.getString(R.string.deviceProfile) + ": " + user.getDeviceProfileName());
        String autoText = mResources.getString(R.string.autoStart) + ": ";
        if(user.wantsAutoStart()){
            autoText += mResources.getString(R.string.yes) +" (" + 
                        user.getAutoStartSeconds() + " " +
                        mResources.getString(R.string.seconds) +  ")";
        } else {
            autoText += mResources.getString(R.string.no);
        }
        mAutoStartText.setText(autoText);
        

        // Iniciar usuario
        // Quedan por rellenar foto, nombre y colegio
        ret = new User("", false, "", null, 
                       user.wantsAutoStart(), user.getAutoStartSeconds(),
                       user.getUseProfileName(), user.getDeviceProfileName());
    }
}
