package org.gskbyte.kora.settingsActivities.useProfiles;

import org.gskbyte.kora.R;
import org.gskbyte.kora.customViews.koraSeekBar.KoraArraySeekBar;
import org.gskbyte.kora.customViews.koraSeekBar.KoraIntegerSeekBar;
import org.gskbyte.kora.settings.UseProfile;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class FeedbackActivity extends Activity
{
    private static final String TAG = "FeedbackActivity";
    
    private Resources mResources;
    private UseProfile mUseProfile;
    
    private CheckBox mVibrationCheckBox;
    
    private CheckBox mConfirmationCheckBox;
    private EditText mConfirmationMillisEdit;
    
    private RadioButton mNoHighlightRadio, mStandardRadio, mZoomRadio, mBrightnessRadio;
    private CheckBox mHighlightBorderCheckBox;
    
    private Button mAcceptButton, mCancelButton;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_LEFT_ICON);
        setContentView(R.layout.use_profile_feedback);
        getWindow().setFeatureDrawableResource(Window.FEATURE_LEFT_ICON,
                                               R.drawable.icon_feedback);
        
        /* Load resources and views */
        mResources = getResources();
        
        mVibrationCheckBox = (CheckBox) findViewById(R.id.vibrationCheckBox);
        
        mConfirmationCheckBox = (CheckBox) findViewById(R.id.confirmationCheckBox);
        mConfirmationMillisEdit = (EditText) findViewById(R.id.confirmationTimeEdit);
        
        mNoHighlightRadio =  (RadioButton) findViewById(R.id.noHighlightRadio);
        mStandardRadio = (RadioButton) findViewById(R.id.standardHighlightRadio);
        mZoomRadio = (RadioButton) findViewById(R.id.zoomInRadio);
        mBrightnessRadio = (RadioButton) findViewById(R.id.increaseBrightnessRadio);
        mHighlightBorderCheckBox = (CheckBox) findViewById(R.id.highlightBorderCheckBox);
        
        mAcceptButton = (Button) findViewById(R.id.acceptButton);
        mCancelButton = (Button) findViewById(R.id.cancelButton);
        
        /* Add listeners */
        mConfirmationCheckBox.setOnCheckedChangeListener(confirmationListener);
        mAcceptButton.setOnClickListener(acceptListener);
        mCancelButton.setOnClickListener(cancelListener);
    }
    
    private void setView()
    {
        mVibrationCheckBox.setChecked(mUseProfile.vibration);
        
        mConfirmationCheckBox.setChecked(mUseProfile.confirmation);
        mConfirmationMillisEdit.setText(String.valueOf(mUseProfile.confirmationTimeMillis));
        
        switch(mUseProfile.contentHighlight){
        case UseProfile.feedback.content_highlight_none:
            mNoHighlightRadio.setChecked(true);
            break;
        case UseProfile.feedback.content_highlight_standard:
            mStandardRadio.setChecked(true);
            break;
        case UseProfile.feedback.content_highlight_zoom:
            mZoomRadio.setChecked(true);
            break;
        case UseProfile.feedback.content_highlight_increase_brightness:
            mBrightnessRadio.setChecked(true);
            break;
        }
        mHighlightBorderCheckBox.setChecked(mUseProfile.borderHighlight);
    }
    
    private void captureData()
    {
        mUseProfile.vibration = mVibrationCheckBox.isChecked();
        
        mUseProfile.confirmation = mConfirmationCheckBox.isChecked();
        mUseProfile.confirmationTimeMillis =
            Integer.parseInt(mConfirmationMillisEdit.getText().toString());
        
        if(mNoHighlightRadio.isChecked()) {
            mUseProfile.contentHighlight = UseProfile.feedback.content_highlight_none;
        } else if(mStandardRadio.isChecked()) {
            mUseProfile.contentHighlight = UseProfile.feedback.content_highlight_standard;
        } else if(mZoomRadio.isChecked()) {
            mUseProfile.contentHighlight = UseProfile.feedback.content_highlight_zoom;
        } else { // mBrightnessRadio
            mUseProfile.contentHighlight = UseProfile.feedback.content_highlight_increase_brightness;
        }
        mUseProfile.borderHighlight = mHighlightBorderCheckBox.isChecked();
    }
    
    private OnCheckedChangeListener confirmationListener =
        new OnCheckedChangeListener(){
        @Override
        public void onCheckedChanged(CompoundButton buttonView,
                boolean isChecked)
        {
            mConfirmationMillisEdit.setEnabled(isChecked);
        }
};
    
    private View.OnClickListener acceptListener =
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
