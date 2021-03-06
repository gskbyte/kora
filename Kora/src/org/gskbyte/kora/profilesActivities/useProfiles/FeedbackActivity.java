package org.gskbyte.kora.profilesActivities.useProfiles;

import org.gskbyte.kora.R;
import org.gskbyte.kora.customViews.detailedSeekBar.FloatSeekBar;
import org.gskbyte.kora.profiles.UseProfile;

import android.os.Bundle;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class FeedbackActivity extends ProfilePropertiesActivity
{
    private static final String TAG = "FeedbackActivity";
    
    private CheckBox mVibrationCheckBox;
    
    private CheckBox mConfirmationCheckBox;
    private FloatSeekBar mConfirmationSeekBar;
    
    private RadioButton mNoHighlightRadio, mStandardRadio, mZoomRadio, mBrightnessRadio;
    private CheckBox mHighlightBorderCheckBox;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_LEFT_ICON);
        setContentView(R.layout.use_profile_feedback);
        getWindow().setFeatureDrawableResource(Window.FEATURE_LEFT_ICON,
                                               R.drawable.icon_feedback);
        
        initButtonBar();
        
        /* Load views */
        mVibrationCheckBox = (CheckBox) findViewById(R.id.vibrationCheckBox);
        
        mConfirmationCheckBox = (CheckBox) findViewById(R.id.confirmationCheckBox);
        mConfirmationSeekBar = (FloatSeekBar) findViewById(R.id.confirmationTimeSeekBar);
        
        mNoHighlightRadio =  (RadioButton) findViewById(R.id.noHighlightRadio);
        mStandardRadio = (RadioButton) findViewById(R.id.standardHighlightRadio);
        mZoomRadio = (RadioButton) findViewById(R.id.zoomInRadio);
        mBrightnessRadio = (RadioButton) findViewById(R.id.increaseBrightnessRadio);
        mHighlightBorderCheckBox = (CheckBox) findViewById(R.id.highlightBorderCheckBox);
        
        /* Add listeners */
        mConfirmationCheckBox.setOnCheckedChangeListener(confirmationListener);
    }
    
    protected void setView()
    {
        mVibrationCheckBox.setChecked(mUseProfile.vibration);
        
        mConfirmationCheckBox.setChecked(mUseProfile.confirmation);
        mConfirmationSeekBar.setValue(mUseProfile.confirmationTimeMillis/1000f);
        
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
    
    protected void captureData()
    {
        mUseProfile.vibration = mVibrationCheckBox.isChecked();
        
        mUseProfile.confirmation = mConfirmationCheckBox.isChecked();
        mUseProfile.confirmationTimeMillis = (int)(mConfirmationSeekBar.getValue()*1000f);
        
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
            mConfirmationSeekBar.setEnabled(isChecked);
        }
    };
}
