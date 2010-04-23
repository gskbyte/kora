package org.gskbyte.kora.settingsActivities.useProfiles;

import org.gskbyte.kora.R;
import org.gskbyte.kora.settings.UseProfile;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public abstract class ProfilePropertiesActivity extends Activity
{
    private static final String TAG = "ProfilePropertiesActivity";
    
    protected Resources mResources;
    protected UseProfile mUseProfile;
    
    protected Button mAcceptButton, mCancelButton;
    
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        /* Load resources and views */
        mResources = getResources();
    }
    
    protected void initButtonBar()
    {

        mAcceptButton = (Button) findViewById(R.id.acceptButton);
        mCancelButton = (Button) findViewById(R.id.cancelButton);
        
        /* Add listeners */
        mAcceptButton.setOnClickListener(acceptListener);
        mCancelButton.setOnClickListener(cancelListener);
    }
    
    public void onStart()
    {
        super.onStart();
        
        /* Take the UseProfile and adjust view */
        try {
            Bundle extras = getIntent().getExtras();
            mUseProfile = (UseProfile) extras.getSerializable(AddEditActivity.TAG_USE_PROFILE);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            Toast.makeText(this, 
                    "ERROR GETTING USE PROFILE. Please contact author.",
                    Toast.LENGTH_LONG);
            finish();
        }
        
        setView();
    }
    
    protected abstract void setView();
    
    protected abstract void captureData();
    
    protected View.OnClickListener acceptListener =
        new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                /* Data capturing */
                captureData();
                Intent intent = new Intent();
                intent.putExtra(AddEditActivity.TAG_USE_PROFILE, mUseProfile);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        };
    
    protected View.OnClickListener cancelListener = 
        new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        };

}
