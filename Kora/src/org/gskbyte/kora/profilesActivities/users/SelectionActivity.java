package org.gskbyte.kora.profilesActivities.users;

import org.gskbyte.kora.R;
import org.gskbyte.kora.profiles.ProfilesManager;
import org.gskbyte.kora.profiles.User;
import org.gskbyte.kora.profiles.ProfilesManager.SettingsException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

public class SelectionActivity extends Activity
{
    private static final String TAG = "UserSelectionActivity";
    
    private Resources mResources;
    private User mSelectedUser;
    private Button mChooseButton, mCopyButton, mEditButton, mDeleteButton,
                   mReturnButton;
    
    private Dialog mDeleteDialog = null;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_LEFT_ICON);
        setContentView(R.layout.user_select);
        
        mResources = getResources();

        mChooseButton = (Button) findViewById(R.id.chooseButton);
        mCopyButton = (Button) findViewById(R.id.copyButton);
        mEditButton = (Button) findViewById(R.id.editButton);
        mDeleteButton = (Button) findViewById(R.id.deleteButton);
        mReturnButton = (Button) findViewById(R.id.returnButton);

        mChooseButton.setOnClickListener(chooseListener);
        mCopyButton.setOnClickListener(copyListener);
        mEditButton.setOnClickListener(editListener);
        mDeleteButton.setOnClickListener(deleteListener);
        mReturnButton.setOnClickListener(returnListener);
    }
    
    public void onStart()
    {
        super.onStart();
        try {
            Bundle extras = getIntent().getExtras();
            String userName = extras.getString(UsersActivity.TAG_USER_NAME);
            mSelectedUser = ProfilesManager.getUser(userName);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            Toast.makeText(this, 
                    "ERROR LOADING USER. Please contact author.",
                    Toast.LENGTH_LONG);
        }
        
        setView();
    }
    
    public void setView()
    {
        setTitle(mResources.getString(R.string.user) + ": " +
                 mSelectedUser.getName());        
        getWindow().setFeatureDrawable(Window.FEATURE_LEFT_ICON,
                                       mSelectedUser.getPhoto());
        
        boolean isCustom = mSelectedUser.isCustom();
        mEditButton.setEnabled(isCustom);
        mDeleteButton.setEnabled(isCustom);
    }
    
    
    private android.view.View.OnClickListener chooseListener =
        new android.view.View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                try {
                	ProfilesManager.setCurrentUser(mSelectedUser.getName());
                } catch (SettingsException e) {
                    Log.e(TAG, e.getMessage());
                    Toast.makeText(SelectionActivity.this, 
                        "USER NOT FOUND: "+mSelectedUser.getName(),
                        Toast.LENGTH_LONG).show();
                }
                finish();
            }
        };
    private android.view.View.OnClickListener copyListener = new android.view.View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(SelectionActivity.this,
                        CopyActivity.class);
                intent.putExtra(UsersActivity.TAG_USER_NAME,
                        mSelectedUser.getName());
                SelectionActivity.this.startActivity(intent);
                finish();
            }
        };
    
    private android.view.View.OnClickListener editListener = 
        new android.view.View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(SelectionActivity.this,
                        AddEditActivity.class);
                intent.putExtra(UsersActivity.TAG_USER_NAME,
                        mSelectedUser.getName());
                SelectionActivity.this.startActivity(intent);
                finish();
            }
        };
    
    private android.view.View.OnClickListener deleteListener = new android.view.View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(mDeleteDialog==null){
                    AlertDialog.Builder builder = new AlertDialog.Builder(SelectionActivity.this);
                    builder.setMessage(
                            mResources.getString(R.string.userDeletionQuestion)+
                            " " + mSelectedUser.getName() + "?")
                    .setIcon(mResources.getDrawable(R.drawable.icon_important))
                    .setCancelable(false)
                    .setPositiveButton(R.string.yes, 
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    try{
                                    	ProfilesManager.removeUser(mSelectedUser.getName());
                                        Toast.makeText(SelectionActivity.this, 
                                                mResources.getString(R.string.deleteUserOk) + ":"  +
                                                mSelectedUser.getName(), Toast.LENGTH_SHORT).show();
                                    }catch (ProfilesManager.SettingsException e){
                                        Toast.makeText(SelectionActivity.this,
                                                mResources.getString(R.string.deleteUserFail) + ":"  +
                                                mSelectedUser.getName(), Toast.LENGTH_SHORT).show();
                                    }
                                    finish();
                                }
                    })
                    .setNegativeButton(R.string.no,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    finish();
                                }
                    });
                    mDeleteDialog = builder.create();
                }
                
                mDeleteDialog.show();
            }
        };
        
   private android.view.View.OnClickListener returnListener = 
        new android.view.View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        };
}
