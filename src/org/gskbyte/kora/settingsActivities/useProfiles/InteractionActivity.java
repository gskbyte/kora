package org.gskbyte.kora.settingsActivities.useProfiles;

import org.gskbyte.kora.R;
import org.gskbyte.kora.settings.UseProfile;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class InteractionActivity extends Activity
{
    private static final String TAG = "InteractionActivity";
    
    private Resources mResources;
    private UseProfile mUseProfile;
    
    private RadioButton mTouchRadio,
                    mMultitouchRadio, mPressAndDragRadio, mSimpleRadio,
                mScanRadio;
    
    private TextView mScanSecondsText;
    private EditText mScanSecondsEdit;
    private Button mAcceptButton, mCancelButton;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_LEFT_ICON);
        setContentView(R.layout.use_profile_interaction);
        getWindow().setFeatureDrawableResource(Window.FEATURE_LEFT_ICON,
                                               R.drawable.icon_interaction);
        
        /* Load resources and views */
        mResources = getResources();

        mTouchRadio = (RadioButton) findViewById(R.id.touchInteractionRadio);
        mMultitouchRadio = (RadioButton) findViewById(R.id.multitouchRadio);
        mPressAndDragRadio = (RadioButton) findViewById(R.id.pressAndDragRadio);
        mSimpleRadio = (RadioButton) findViewById(R.id.simpleTouchRadio);
        mScanRadio = (RadioButton) findViewById(R.id.scanInteractionRadio);
        mScanSecondsText = (TextView) findViewById(R.id.scanSecondsText);
        mScanSecondsEdit = (EditText) findViewById(R.id.scanSecondsEdit);
        
        mAcceptButton = (Button) findViewById(R.id.acceptButton);
        mCancelButton = (Button) findViewById(R.id.cancelButton);
        
        /* Add listeners */
        mTouchRadio.setOnCheckedChangeListener(mTouchListener);
        
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
    
    private void setView()
    {
        /* Set main interaction mode */
        if(mUseProfile.mainInteraction == UseProfile.interaction.touch_mode){
            mTouchListener.onCheckedChanged(mTouchRadio, true);
        } else {
            mScanRadio.setChecked(true);
            mTouchListener.onCheckedChanged(mTouchRadio, false);
        }
        
        /* Set touch interaction mode */
        switch(mUseProfile.touchMode){
        case UseProfile.interaction.multitouch_and_drag:
            mMultitouchRadio.setChecked(true);
            break;
        case UseProfile.interaction.press_and_drag:
            mPressAndDragRadio.setChecked(true);
            break;
        case UseProfile.interaction.simple_press:
            mSimpleRadio.setChecked(true);
            break;
        }

        /* Set scan interaction mode */
        mScanSecondsEdit.setText(String.valueOf(mUseProfile.scanTimeMillis));
    }
    
    private OnCheckedChangeListener mTouchListener =
        new OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                    boolean isChecked)
            {
                mMultitouchRadio.setEnabled(isChecked);
                mPressAndDragRadio.setEnabled(isChecked);
                mSimpleRadio.setEnabled(isChecked);

                mScanSecondsText.setEnabled(!isChecked);
                mScanSecondsEdit.setEnabled(!isChecked);
            }
    };
    
    private View.OnClickListener acceptListener =
        new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                if(mTouchRadio.isChecked()){
                    mUseProfile.mainInteraction =
                        UseProfile.interaction.touch_mode;
                } else {
                    mUseProfile.mainInteraction =
                        UseProfile.interaction.scan_mode;
                }
                
                if(mMultitouchRadio.isChecked()) {
                    mUseProfile.touchMode =
                        UseProfile.interaction.multitouch_and_drag;
                } else if(mPressAndDragRadio.isChecked()) {
                    mUseProfile.touchMode =
                        UseProfile.interaction.press_and_drag;
                } else {
                    mUseProfile.touchMode =
                        UseProfile.interaction.simple_press;
                }
                String timeString = mScanSecondsEdit.getText().toString();
                mUseProfile.scanTimeMillis = Integer.parseInt(timeString);
                
                Intent intent = new Intent();
                intent.putExtra(AddEditActivity.TAG_USE_PROFILE, mUseProfile);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        };
    
    private View.OnClickListener cancelListener = 
        new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        };
        
    
}
