package org.gskbyte.kora.settingsActivities;

import org.gskbyte.kora.R;
import org.gskbyte.kora.settings.SettingsManager;
import org.gskbyte.kora.settings.User;
import org.gskbyte.kora.settings.SettingsManager.SettingsException;

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

public class UserSelectionActivity extends Activity
{
    private static final String TAG = "UserSelectionActivity";
    
    private Resources mResources;
    private SettingsManager mSettings;
    private User mCurrentUser;
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
            String userName = extras.getString(UsersActivity.TAG_USER_NAME);
            mSettings = SettingsManager.getInstance();
            mCurrentUser = mSettings.getUser(userName);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            Toast.makeText(this, 
                    "ERROR LOADING USER. Please contact author.",
                    Toast.LENGTH_LONG);
        }
        
        setView();
    }
    
    public void onResume()
    {
        super.onResume();
        Toast.makeText(this, 
                "CACA.",
                Toast.LENGTH_LONG);
        /* Actualizar vista por si algo cambia */
    }

    public void setView()
    {
        setTitle(mResources.getString(R.string.user) + ": " +
                 mCurrentUser.getName());        
        getWindow().setFeatureDrawable(Window.FEATURE_LEFT_ICON,
                                       mCurrentUser.getPhoto());
        
        boolean isCustom = mCurrentUser.isCustom();
        mEditButton.setEnabled(isCustom);
        mDeleteButton.setEnabled(isCustom);
    }
    
    
    private android.view.View.OnClickListener chooseListener =
        new android.view.View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                try {
                    mSettings.setCurrentUser(mCurrentUser.getName());
                } catch (SettingsException e) {
                    Log.e(TAG, e.getMessage());
                    Toast.makeText(UserSelectionActivity.this, 
                        "USER NOT FOUND: "+mCurrentUser.getName(),
                        Toast.LENGTH_LONG).show();
                }
                finish();
            }
        };
    private android.view.View.OnClickListener copyListener = new android.view.View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(UserSelectionActivity.this,
                        UserCopyActivity.class);
                intent.putExtra(UsersActivity.TAG_USER_NAME,
                        mCurrentUser.getName());
                UserSelectionActivity.this.startActivity(intent);
                finish();
            }
        };
    
    private android.view.View.OnClickListener editListener = 
        new android.view.View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(UserSelectionActivity.this,
                        UserAddEditActivity.class);
                intent.putExtra(UsersActivity.TAG_USER_NAME,
                        mCurrentUser.getName());
                UserSelectionActivity.this.startActivity(intent);
                finish();
            }
        };
    
    private android.view.View.OnClickListener deleteListener = new android.view.View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(mDeleteDialog==null){
                    AlertDialog.Builder builder = new AlertDialog.Builder(UserSelectionActivity.this);
                    builder.setMessage(
                            mResources.getString(R.string.userDeletionQuestion)+
                            " " + mCurrentUser.getName() + "?")
                    .setIcon(mResources.getDrawable(R.drawable.icon_important))
                    .setCancelable(false)
                    .setPositiveButton(R.string.yes, 
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    try{
                                        mSettings.removeUser(mCurrentUser.getName());
                                        Toast.makeText(UserSelectionActivity.this, 
                                                mResources.getString(R.string.deleteUserOk) + ":"  +
                                                mCurrentUser.getName(), Toast.LENGTH_SHORT).show();
                                    }catch (SettingsManager.SettingsException e){
                                        Toast.makeText(UserSelectionActivity.this,
                                                mResources.getString(R.string.deleteUserFail) + ":"  +
                                                mCurrentUser.getName(), Toast.LENGTH_SHORT).show();
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
