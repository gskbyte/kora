package org.gskbyte.kora.settingsActivities.useProfiles;

import org.gskbyte.kora.R;
import org.gskbyte.kora.customViews.detailedSeekBar.ArraySeekBar;
import org.gskbyte.kora.customViews.detailedSeekBar.IntegerSeekBar;
import org.gskbyte.kora.settings.UseProfile;
import org.gskbyte.kora.customViews.ColorButton;
import org.gskbyte.kora.customViews.ColorPickerDialog;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class VisualizationActivity extends ProfilePropertiesActivity
{
    private static final String TAG = "VisualizationActivity";
    
    private RadioButton mDefaultLAFRadio, mPlainRadio, mDifferentPlainRadio,
                        mHighContrastRadio, mBlackAndWhiteRadio;
    private ColorButton mBackgroundColorButton;
    private IntegerSeekBar mRowsSeekBar, mColumnsSeekBar;
    private ArraySeekBar mMarginSeekBar;
    private RadioButton mBothOrientationsRadio, mVerticalRadio, mHorizontalRadio;
    
    private CheckBox mShowTextCheckBox;
    private ArraySeekBar mTextSizeSeekBar;
    private RadioButton mSansRadio, mMasalleraRadio, mMonofurRadio,
                        mTextBlackRadio, mTextWhiteRadio, mTextCustomColorRadio;
    private CheckBox mCapsCheckBox;
    private ColorButton mTextColorButton;
    
    private RadioButton mIconRadio, mIconHighContrastRadio, iconBlackWhiteRadio,
    					mIconPhotoRadio, mIconAnimationRadio;
    private CheckBox mCustomImageCheckBox;

    
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_LEFT_ICON);
        setContentView(R.layout.use_profile_visualization);
        getWindow().setFeatureDrawableResource(Window.FEATURE_LEFT_ICON,
                                               R.drawable.icon_visualization);
        
        initButtonBar();
        
        /* Load views */
        mDefaultLAFRadio = (RadioButton) findViewById(R.id.defaultLAFRadio);
        mPlainRadio = (RadioButton) findViewById(R.id.plainRadio);
        mDifferentPlainRadio = (RadioButton) findViewById(R.id.differentPlainRadio);
        mHighContrastRadio = (RadioButton) findViewById(R.id.highContrastRadio);
        mBlackAndWhiteRadio = (RadioButton) findViewById(R.id.blackAndWhiteRadio);
        mBackgroundColorButton = (ColorButton) findViewById(R.id.backgroundColorButton);
        
        mRowsSeekBar = (IntegerSeekBar) findViewById(R.id.rowsSeekBar);
        mColumnsSeekBar = (IntegerSeekBar) findViewById(R.id.columnsSeekBar);
        mMarginSeekBar = (ArraySeekBar) findViewById(R.id.marginSeekBar);
        mBothOrientationsRadio = (RadioButton) findViewById(R.id.bothOrientationsRadio);
        mVerticalRadio = (RadioButton) findViewById(R.id.verticalRadio);
        mHorizontalRadio = (RadioButton) findViewById(R.id.horizontalRadio);
        
        mShowTextCheckBox = (CheckBox) findViewById(R.id.showTextCheckBox);
        mTextSizeSeekBar = (ArraySeekBar) findViewById(R.id.textSizeSeekBar);
        mSansRadio = (RadioButton) findViewById(R.id.sansRadio);
        mMasalleraRadio = (RadioButton) findViewById(R.id.monofurRadio);
        mMonofurRadio = (RadioButton) findViewById(R.id.masalleraRadio);
        mTextBlackRadio = (RadioButton) findViewById(R.id.textBlackRadio);
        mTextWhiteRadio = (RadioButton) findViewById(R.id.textWhiteRadio);
        mTextCustomColorRadio = (RadioButton) findViewById(R.id.textCustomColorRadio);
        mTextColorButton = (ColorButton) findViewById(R.id.textColorButton);
        mCapsCheckBox = (CheckBox) findViewById(R.id.capsCheckBox);
        
        mIconRadio = (RadioButton) findViewById(R.id.iconRadio);
        mIconHighContrastRadio = (RadioButton) findViewById(R.id.iconHighContrastRadio);
        iconBlackWhiteRadio = (RadioButton) findViewById(R.id.iconBlackWhiteRadio);
        mIconPhotoRadio = (RadioButton) findViewById(R.id.iconPhotoRadio);
        mIconAnimationRadio = (RadioButton) findViewById(R.id.iconAnimationRadio);
        mCustomImageCheckBox = (CheckBox) findViewById(R.id.customImageCheckBox);
        
        /* Add listeners */
        mPlainRadio.setOnCheckedChangeListener(backgroundColorListener);
        mShowTextCheckBox.setOnCheckedChangeListener(showTextListener);
        mTextCustomColorRadio.setOnCheckedChangeListener(textColorListener);
    }
    
    protected void setView()
    {
        /* Set visualization mode */
        switch(mUseProfile.viewMode){
        case UseProfile.visualization.view_standard:
            mDefaultLAFRadio.setChecked(true);
            break;
        case UseProfile.visualization.view_plain_color:
            mPlainRadio.setChecked(true);
            break;
        case UseProfile.visualization.view_plain_differenced_color:
            mDifferentPlainRadio.setChecked(true);
            break;
        case UseProfile.visualization.view_hi_contrast_color:
            mHighContrastRadio.setChecked(true);
            break;
        case UseProfile.visualization.view_black_and_white:
            mBlackAndWhiteRadio.setChecked(true);
            break;
        }
        mBackgroundColorButton.setColor(mUseProfile.backgroundColor);
        mRowsSeekBar.setValue(mUseProfile.rows);
        mColumnsSeekBar.setValue(mUseProfile.columns);
        mMarginSeekBar.setIndex(mUseProfile.margin);
        switch(mUseProfile.orientations){
        case UseProfile.visualization.orientation_both:
            mBothOrientationsRadio.setChecked(true);
            break;
        case UseProfile.visualization.orientation_vertical:
            mVerticalRadio.setChecked(true);
            break;
        case UseProfile.visualization.orientation_horizontal:
            mHorizontalRadio.setChecked(true);
            break;
        }
        
        /* Set text mode */
        mShowTextCheckBox.setChecked(mUseProfile.showText);
        mTextSizeSeekBar.setIndex(mUseProfile.textSize);
        switch(mUseProfile.typography){
        case UseProfile.visualization.font_sans:
            mSansRadio.setChecked(true);
            break;
        case UseProfile.visualization.font_masallera:
            mMasalleraRadio.setChecked(true);
            break;
        case UseProfile.visualization.font_monofur:
            mMonofurRadio.setChecked(true);
            break;
        }
        switch(mUseProfile.textColor){
        case 0xFF000000:
            mTextBlackRadio.setChecked(true);
            break;
        case 0xFFFFFFFF:
            mTextWhiteRadio.setChecked(true);
            break;
        default:
            mTextCustomColorRadio.setChecked(true);
            mTextColorButton.setColor(mUseProfile.textColor);
            break;
        }
        mCapsCheckBox.setChecked(mUseProfile.typographyCaps);
        
        /* Set icon mode */
        switch(mUseProfile.iconMode){
        case UseProfile.visualization.icon_pictogram:
            mIconRadio.setChecked(true);
            break;
        case UseProfile.visualization.icon_high_contrast:
            mIconHighContrastRadio.setChecked(true);
            break;
        case UseProfile.visualization.icon_black_white:
        	iconBlackWhiteRadio.setChecked(true);
        	break;
        case UseProfile.visualization.icon_photo:
            mIconPhotoRadio.setChecked(true);
            break;
        case UseProfile.visualization.icon_animation:
            mIconAnimationRadio.setChecked(true);
            break;
        }
        mCustomImageCheckBox.setChecked(mUseProfile.customImage);
    }
    
    protected void captureData()
    {
        // Visualization mode
        if(mDefaultLAFRadio.isChecked()){
            mUseProfile.viewMode = UseProfile.visualization.view_standard;
        } else if (mPlainRadio.isChecked()) {
            mUseProfile.viewMode = UseProfile.visualization.view_plain_color;
        } else if (mDifferentPlainRadio.isChecked()) {
            mUseProfile.viewMode = UseProfile.visualization.view_plain_differenced_color;
        } else if (mHighContrastRadio.isChecked()) {
            mUseProfile.viewMode = UseProfile.visualization.view_hi_contrast_color;
        } else {
            mUseProfile.viewMode = UseProfile.visualization.view_black_and_white;
        }
        
        mUseProfile.backgroundColor = mBackgroundColorButton.getColor();
        mUseProfile.rows = mRowsSeekBar.getValue();
        mUseProfile.columns = mColumnsSeekBar.getValue();
        mUseProfile.margin = mMarginSeekBar.getIndex();
        if(mBothOrientationsRadio.isChecked()){
            mUseProfile.orientations = UseProfile.visualization.orientation_both;
        } else if(mVerticalRadio.isChecked()) {
            mUseProfile.orientations = UseProfile.visualization.orientation_vertical;
        } else {
            mUseProfile.orientations = UseProfile.visualization.orientation_horizontal;
        }
        
        // Text mode
        mUseProfile.showText = mShowTextCheckBox.isChecked();
        mUseProfile.textSize = mTextSizeSeekBar.getIndex();
        /*
        switch(mTextSizeSeekBar.getIndex()){
        case 0:
            mUseProfile.fontSize = UseProfile.visualization.text_size_small;
            break;
        case 1:
            mUseProfile.fontSize = UseProfile.visualization.text_size_medium;
            break;
        case 2:
            mUseProfile.fontSize = UseProfile.visualization.text_size_large;
            break;
        }*/
        
        if(mSansRadio.isChecked()) {
            mUseProfile.typography = UseProfile.visualization.font_sans;
        } else if(mMasalleraRadio.isChecked()) {
            mUseProfile.typography = UseProfile.visualization.font_masallera;
        } else {
            mUseProfile.typography = UseProfile.visualization.font_monofur;
        }
        
        if(mTextBlackRadio.isChecked()) {
            mUseProfile.textColor = 0xFF000000;
        } else if(mTextWhiteRadio.isChecked()) {
            mUseProfile.textColor = 0xFFFFFFFF;
        } else {
            mUseProfile.textColor = mTextColorButton.getColor();
        }
        
        mUseProfile.typographyCaps = mCapsCheckBox.isChecked();
        
        // Icon mode
        if(mIconRadio.isChecked()) {
            mUseProfile.iconMode = UseProfile.visualization.icon_pictogram;
        } else if(mIconHighContrastRadio.isChecked()) {
            mUseProfile.iconMode = UseProfile.visualization.icon_high_contrast;
        } else if(iconBlackWhiteRadio.isChecked()) {
        	mUseProfile.iconMode = UseProfile.visualization.icon_black_white;
        } else if(mIconPhotoRadio.isChecked()) {
            mUseProfile.iconMode = UseProfile.visualization.icon_photo;
        } else {
            mUseProfile.iconMode = UseProfile.visualization.icon_animation;
        }
        
        mUseProfile.customImage = mCustomImageCheckBox.isChecked();
    }
    
    private OnCheckedChangeListener backgroundColorListener =
        new OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                    boolean isChecked)
            {
                if(isChecked) {
                    mBackgroundColorButton.setVisibility(View.VISIBLE);
                } else {
                    mBackgroundColorButton.setVisibility(View.GONE);
                }
            }
        };
    
    private OnCheckedChangeListener showTextListener =
        new OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                    boolean isChecked)
            {
                // Mierda, no puedo aplicarlo al layout
                mTextSizeSeekBar.setEnabled(isChecked);
                mSansRadio.setEnabled(isChecked);
                mMasalleraRadio.setEnabled(isChecked);
                mMonofurRadio.setEnabled(isChecked);
                mTextBlackRadio.setEnabled(isChecked);
                mTextWhiteRadio.setEnabled(isChecked);
                mTextCustomColorRadio.setEnabled(isChecked);
                mCapsCheckBox.setEnabled(isChecked);
            }
        };
        
    private OnCheckedChangeListener textColorListener =
        new OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                    boolean isChecked)
            {
                if(isChecked) {
                    mTextColorButton.setVisibility(View.VISIBLE);
                } else {
                    mTextColorButton.setVisibility(View.GONE);
                }
            }
        };
}
