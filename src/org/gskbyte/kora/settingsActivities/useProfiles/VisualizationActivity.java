package org.gskbyte.kora.settingsActivities.useProfiles;

import org.gskbyte.kora.R;
import org.gskbyte.kora.customViews.koraSeekBar.KoraArraySeekBar;
import org.gskbyte.kora.customViews.koraSeekBar.KoraIntegerSeekBar;
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
    private KoraIntegerSeekBar mRowsSeekBar, mColumnsSeekBar;
    
    private CheckBox mShowTextCheckBox;
    private KoraArraySeekBar mTextSizeSeekBar;
    private RadioButton mSansRadio, mCalligraphicRadio, mCapsRadio,
                        mTextBlackRadio, mTextWhiteRadio, mTextCustomColorRadio;
    private ColorButton mTextColorButton;
    
    private RadioButton mIconRadio, mIconHighContrastRadio, mIconPhotoRadio,
                        mIconAnimationRadio;
    
    private RadioButton mPagingStandardAutomaticRadio, mPagingLastButtonRadio;

    
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
        
        mRowsSeekBar = (KoraIntegerSeekBar) findViewById(R.id.rowsSeekBar);
        mColumnsSeekBar = (KoraIntegerSeekBar) findViewById(R.id.columnsSeekBar);
        
        mShowTextCheckBox = (CheckBox) findViewById(R.id.showTextCheckBox);
        mTextSizeSeekBar = (KoraArraySeekBar) findViewById(R.id.textSizeSeekBar);
        mSansRadio = (RadioButton) findViewById(R.id.sansRadio);
        mCalligraphicRadio = (RadioButton) findViewById(R.id.calligraphicRadio);
        mCapsRadio = (RadioButton) findViewById(R.id.capsRadio);
        mTextBlackRadio = (RadioButton) findViewById(R.id.textBlackRadio);
        mTextWhiteRadio = (RadioButton) findViewById(R.id.textWhiteRadio);
        mTextCustomColorRadio = (RadioButton) findViewById(R.id.textCustomColorRadio);
        mTextColorButton = (ColorButton) findViewById(R.id.textColorButton);
        
        mIconRadio = (RadioButton) findViewById(R.id.iconRadio);
        mIconHighContrastRadio = (RadioButton) findViewById(R.id.iconHighContrastRadio);
        mIconPhotoRadio = (RadioButton) findViewById(R.id.iconPhotoRadio);
        mIconAnimationRadio = (RadioButton) findViewById(R.id.iconAnimationRadio);
        
        mPagingStandardAutomaticRadio = (RadioButton) findViewById(R.id.pagingStandardAutomaticRadio);
        mPagingLastButtonRadio = (RadioButton) findViewById(R.id.pagingLastButtonRadio);
        
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
            mTextColorButton.setColor(mUseProfile.backgroundColor);
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
        mRowsSeekBar.setValue(mUseProfile.rows);
        mColumnsSeekBar.setValue(mUseProfile.columns);
        
        /* Set text mode */
        mShowTextCheckBox.setChecked(mUseProfile.showText);
        switch(mUseProfile.fontSize){
        case UseProfile.visualization.text_size_small:
            mTextSizeSeekBar.setIndex(0);
            break;
        case UseProfile.visualization.text_size_medium:
            mTextSizeSeekBar.setIndex(1);
            break;
        case UseProfile.visualization.text_size_large:
            mTextSizeSeekBar.setIndex(2);
            break;
        }
        switch(mUseProfile.typography){
        case UseProfile.visualization.text_font_sans:
            mSansRadio.setChecked(true);
            break;
        case UseProfile.visualization.calligraphic_font:
            mCalligraphicRadio.setChecked(true);
            break;
        case UseProfile.visualization.caps_font:
            mCapsRadio.setChecked(true);
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
        
        /* Set icon mode */
        switch(mUseProfile.iconMode){
        case UseProfile.visualization.icon_pictogram:
            mIconRadio.setChecked(true);
            break;
        case UseProfile.visualization.icon_high_contrast:
            mIconHighContrastRadio.setChecked(true);
            break;
        case UseProfile.visualization.icon_photo:
            mIconPhotoRadio.setChecked(true);
            break;
        case UseProfile.visualization.icon_animation:
            mIconAnimationRadio.setChecked(true);
            break;
        }
        
        /* Set paging mode */
        if(mUseProfile.mainInteraction == UseProfile.interaction.touch_mode)
            mPagingStandardAutomaticRadio.setText(mResources.getString(R.string.standardPaging));
        else
            mPagingStandardAutomaticRadio.setText(mResources.getString(R.string.automaticPaging));
        switch(mUseProfile.paginationMode){
        case UseProfile.visualization.pagination_standard:
            mIconRadio.setChecked(true);
            break;
        case UseProfile.visualization.pagination_buttons:
            mIconHighContrastRadio.setChecked(true);
            break;
        }
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
            mUseProfile.backgroundColor = mBackgroundColorButton.getColor();
        } else if (mHighContrastRadio.isChecked()) {
            mUseProfile.viewMode = UseProfile.visualization.view_hi_contrast_color;
        } else {
            mUseProfile.viewMode = UseProfile.visualization.view_black_and_white;
        }
        mUseProfile.rows = mRowsSeekBar.getValue();
        mUseProfile.columns = mColumnsSeekBar.getValue();
        
        // Text mode
        mUseProfile.showText = mShowTextCheckBox.isChecked();
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
        }
        
        if(mTextBlackRadio.isChecked()) {
            mUseProfile.textColor = 0xFF000000;
        } else if(mTextWhiteRadio.isChecked()) {
            mUseProfile.textColor = 0xFFFFFFFF;
        } else {
            mUseProfile.textColor = mTextColorButton.getColor();
        }
        
        // Icon mode
        if(mIconRadio.isChecked()) {
            mUseProfile.iconMode = UseProfile.visualization.icon_pictogram;
        } else if(mIconHighContrastRadio.isChecked()) {
            mUseProfile.iconMode = UseProfile.visualization.icon_high_contrast;
        } else if(mIconPhotoRadio.isChecked()) {
            mUseProfile.iconMode = UseProfile.visualization.icon_photo;
        } else {
            mUseProfile.iconMode = UseProfile.visualization.icon_animation;
        }
        
        // Paging mode
        if(mPagingStandardAutomaticRadio.isChecked()) {
            mUseProfile.paginationMode = UseProfile.visualization.pagination_standard;
        } else {
            mUseProfile.paginationMode = UseProfile.visualization.pagination_buttons;
        }
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
                mCalligraphicRadio.setEnabled(isChecked);
                mCapsRadio.setEnabled(isChecked);
                mTextBlackRadio.setEnabled(isChecked);
                mTextWhiteRadio.setEnabled(isChecked);
                mTextCustomColorRadio.setEnabled(isChecked);
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
