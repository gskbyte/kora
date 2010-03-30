package org.gskbyte.kora.settingsActivities;

import org.gskbyte.kora.R;
import org.gskbyte.kora.settings.User;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

public class UserSelectionActivity extends Activity
{
    private static final String TAG = "AddEditUserActivity";
    
    Resources resources;
    Button chooseButton, copyButton, editButton, deleteButton;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_LEFT_ICON);
        setContentView(R.layout.add_edit_user_dialog);
           
        resources = getResources();
        //activity = (UsersActivity) context;
        
        chooseButton = (Button) findViewById(R.id.chooseButton);
        copyButton = (Button) findViewById(R.id.copyButton);
        editButton = (Button) findViewById(R.id.editButton);
        deleteButton = (Button) findViewById(R.id.deleteButton);
        /* 
        setUser(user);

        chooseButton.setOnClickListener(chooseListener);
        copyButton.setOnClickListener(copyListener);
        editButton.setOnClickListener(editListener);
        deleteButton.setOnClickListener(deleteListener);
  */      

        
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
        setTitle(resources.getString(R.string.user) + ": " + user.getName());        
        getWindow().setFeatureDrawable(Window.FEATURE_LEFT_ICON, user.getPhoto());
        
        boolean isCustom = user.isCustom();
        editButton.setEnabled(isCustom);
        deleteButton.setEnabled(isCustom);
    }
    
    /*
    private android.view.View.OnClickListener chooseListener = new android.view.View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                activity.chooseCurrentProfile();
                dismiss();
            }
        };
        
    private android.view.View.OnClickListener copyListener = new android.view.View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                activity.showDialog(UsersActivity.COPY_DIALOG_ID);
                dismiss();
            }
        };
    
    private android.view.View.OnClickListener editListener = new android.view.View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                activity.showDialog(UsersActivity.EDIT_DIALOG_ID);
                dismiss();
            }
        };
    
    private android.view.View.OnClickListener deleteListener = new android.view.View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                activity.showDialog(UsersActivity.CONFIRM_DELETE_DIALOG_ID);
                dismiss();
            }
        };
    */
}
