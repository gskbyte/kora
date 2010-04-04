package org.gskbyte.kora.settingsActivities.useProfiles;

import org.gskbyte.kora.R;
import org.gskbyte.kora.settings.SettingsManager;
import org.gskbyte.kora.settings.UseProfile;
import org.gskbyte.kora.settings.SettingsManager.SettingsException;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CopyActivity extends Activity
{
    private static final String TAG = "UserCopyActivity";
    private UseProfile mUseProfile;
    
    private Resources mResources;
    private SettingsManager mSettings;
    
    private EditText mNameEdit;
    private TextView mMainInteractionText;
    private TextView mLookAndFeelText, mLayoutText, mTextText, mImagesAndIconsText,
                     mPagingText;
    private TextView mVibrationText, mConfirmationText, mHighlightText,
                     mHighlightBorderText;
    private TextView mSoundModeText;
    private Button mAcceptButton, mCancelButton;
    
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_LEFT_ICON);
        setContentView(R.layout.use_profile_copy);
        getWindow().setFeatureDrawableResource(Window.FEATURE_LEFT_ICON,
                R.drawable.action_copy);
        setTitle(R.string.copyUseProfile);
        
        mResources = getResources();
        
        mNameEdit = (EditText) findViewById(R.id.useProfileNameEdit);
        mMainInteractionText = (TextView) findViewById(R.id.mainInteractionText);
        mLookAndFeelText = (TextView) findViewById(R.id.lookAndFeelText);
        mLayoutText = (TextView) findViewById(R.id.layoutText);
        mTextText = (TextView) findViewById(R.id.textText);
        mImagesAndIconsText = (TextView) findViewById(R.id.imagesAndIconsText);
        mPagingText = (TextView) findViewById(R.id.pagingText);
        mVibrationText = (TextView) findViewById(R.id.vibrationText);
        mConfirmationText = (TextView) findViewById(R.id.confirmationText);
        mHighlightText = (TextView) findViewById(R.id.highlightText);
        mHighlightBorderText = (TextView) findViewById(R.id.highlightBorderText);
        mSoundModeText = (TextView) findViewById(R.id.soundModeText);
        
        mAcceptButton = (Button) findViewById(R.id.acceptButton);
        mCancelButton = (Button) findViewById(R.id.cancelButton);

        mAcceptButton.setOnClickListener(acceptListener);
        mCancelButton.setOnClickListener(cancelListener);

        
        try {
            Bundle extras = getIntent().getExtras();
            
            mSettings = SettingsManager.getInstance();
            if(extras != null){
                String userName = extras.getString(UseProfilesActivity.TAG_USEPROFILE_NAME);
                mUseProfile = mSettings.getUseProfile(userName);
            } else {
                mUseProfile = null;
            }
        } catch (SettingsException e) {
            Log.e(TAG, e.getMessage());
            Toast.makeText(this, 
                    "ERROR LOADING USE PROFILE. Please contact author.",
                    Toast.LENGTH_LONG);
        }
        
        setView();
    }
    
    public void setView()
    {
        Resources r = mResources;
        
        mNameEdit.setText("");
        
        switch(mUseProfile.mainInteraction){
        case UseProfile.interaction.touch_mode:
            mMainInteractionText.setText(R.string.touchInteraction);
            break;
        case UseProfile.interaction.scan_mode:
            String t = r.getString(R.string.scanInteraction);
            t += " (" + mUseProfile.scanTimeMillis + " ms )";
            mMainInteractionText.setText(t);
            break;
        }
        
        switch(mUseProfile.viewMode){
        case UseProfile.visualization.view_standard:
            mLookAndFeelText.setText(R.string.defaultLAF);
            break;
        case UseProfile.visualization.view_plain_color:
            mLookAndFeelText.setText(R.string.plainColor);
            break;
        case UseProfile.visualization.view_plain_differenced_color:
            mLookAndFeelText.setText(R.string.differentPlainColor);
            break;
        case UseProfile.visualization.view_hi_contrast_color:
            mLookAndFeelText.setText(R.string.highContrastColor);
            break;
        case UseProfile.visualization.view_black_and_white:
            mLookAndFeelText.setText(R.string.blackAndWhite);
            break;
        }
        
        mLayoutText.setText(mUseProfile.rows + " x "+ mUseProfile.columns);
        
        if(mUseProfile.showText == true)
            mTextText.setText(R.string.yes);
        else
            mTextText.setText(R.string.no);
        
        switch(mUseProfile.iconMode){
        case UseProfile.visualization.icon_pictogram:
            mImagesAndIconsText.setText(R.string.icon);
            break;
        case UseProfile.visualization.icon_high_contrast:
            mImagesAndIconsText.setText(R.string.highContrastIcon);
            break;
        case UseProfile.visualization.icon_photo:
            mImagesAndIconsText.setText(R.string.photoIcon);
            break;
        case UseProfile.visualization.icon_animation:
            mImagesAndIconsText.setText(R.string.animation);
            break;
        }

        if(mUseProfile.paginationMode ==
            UseProfile.visualization.pagination_standard){
            if(mUseProfile.mainInteraction == UseProfile.interaction.touch_mode)
                mPagingText.setText(R.string.standardPaging);
            else
                mPagingText.setText(R.string.automaticPaging);
        } else {
            mPagingText.setText(R.string.lastButton);
        }
        
        if(mUseProfile.vibration)
            mVibrationText.setText(R.string.yes);
        else
            mVibrationText.setText(R.string.no);
        
        if(mUseProfile.confirmation){
            String s = r.getString(R.string.yes);
            s += " (" + mUseProfile.confirmationTimeMillis + " ms)";
            mConfirmationText.setText(s);
        }
        else
            mConfirmationText.setText(R.string.no);
        
        switch(mUseProfile.contentHighlight){
        case UseProfile.feedback.content_highlight_none:
            mHighlightText.setText(R.string.noHighlight);
            break;
        case UseProfile.feedback.content_highlight_standard:
            mHighlightText.setText(R.string.standardHighlight);
            break;
        case UseProfile.feedback.content_highlight_zoom:
            mHighlightText.setText(R.string.zoomIn);
            break;
        case UseProfile.feedback.content_highlight_increase_brightness:
            mHighlightText.setText(R.string.increaseBrightness);
            break;
        }

        if(mUseProfile.borderHighlight)
            mHighlightBorderText.setText(R.string.yes);
        else
            mHighlightBorderText.setText(R.string.no);
        
        switch(mUseProfile.soundMode){
        case UseProfile.sound.no_sounds:
            mSoundModeText.setText(R.string.noSound);
            break;
        case UseProfile.sound.simple_sounds:
            mSoundModeText.setText(R.string.simpleSounds);
            break;
        case UseProfile.sound.voice_sounds:
            mSoundModeText.setText(R.string.voice);
            break;
        }
    }
    
    
    private View.OnClickListener acceptListener =
        new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                // 0 = OK, -1 = campos vacíos
                // valores de SettingsException = fallo
                int result = 0;
                
                String name = mNameEdit.getText().toString();
                mUseProfile.isDefault = false;
                
                if(name.length()>0)
                {
                    mUseProfile.setName(name);
                    try{
                        mSettings.addUseProfile(mUseProfile);
                    } catch (SettingsManager.SettingsException e){
                        result = e.type;
                    }
                } else {
                    result = -1;
                }
                
                String s;
                switch(result)
                {
                case 0:
                    s = mResources.getString(R.string.addUseProfileOk) + ": " + 
                        name;
                    break;
                case -1:
                    s = mResources.getString(R.string.emptyUseProfileNameFail);
                    break;
                case SettingsException.EXISTS:
                    s = mResources.getString(R.string.existingUseProfileFail) + ": " +
                        name;
                    break;
                default:
                    s = mResources.getString(R.string.settingsError);
                    break;
                }
                
                Toast.makeText(CopyActivity.this, s, Toast.LENGTH_SHORT).show();
                if(result==0)
                    finish();
            }
        };
        
    private View.OnClickListener cancelListener =
        new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                finish();
            }
        };
}
