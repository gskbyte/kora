package org.gskbyte.kora.profilesActivities.useProfiles;

import org.gskbyte.kora.R;
import org.gskbyte.kora.customViews.detailedSeekBar.FloatSeekBar;
import org.gskbyte.kora.profiles.UseProfile;

import android.os.Bundle;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class InteractionActivity extends ProfilePropertiesActivity
{
    private static final String TAG = "InteractionActivity";
    
    private RadioButton mTouchRadio,
                    mMultitouchRadio, mPressAndDragRadio, mSimpleRadio,
                    mScanRadio, mSimpleScanRadio, mRowColumnScanRadio;
    
    private FloatSeekBar mScanSecondsSeekBar;
    
    private RadioButton mPagingStandardAutomaticRadio, mPagingLastButtonRadio;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_LEFT_ICON);
        setContentView(R.layout.use_profile_interaction);
        getWindow().setFeatureDrawableResource(Window.FEATURE_LEFT_ICON,
                                               R.drawable.icon_interaction);

        initButtonBar();
        
        /* Load views */
        mTouchRadio = (RadioButton) findViewById(R.id.touchInteractionRadio);
        mMultitouchRadio = (RadioButton) findViewById(R.id.multitouchRadio);
        mPressAndDragRadio = (RadioButton) findViewById(R.id.pressAndDragRadio);
        mSimpleRadio = (RadioButton) findViewById(R.id.simpleTouchRadio);
        mScanRadio = (RadioButton) findViewById(R.id.scanInteractionRadio);
        mSimpleScanRadio = (RadioButton) findViewById(R.id.simpleScanRadio);
        mRowColumnScanRadio = (RadioButton) findViewById(R.id.rowColumnScanRadio);
        mScanSecondsSeekBar = (FloatSeekBar) findViewById(R.id.scanTimeSeekBar);
        mPagingStandardAutomaticRadio = (RadioButton) findViewById(R.id.pagingStandardAutomaticRadio);
        mPagingLastButtonRadio = (RadioButton) findViewById(R.id.pagingLastButtonRadio);

        /* Add listeners */
        mTouchRadio.setOnCheckedChangeListener(touchListener);
    }
    
    protected void setView()
    {
        /* Set main interaction mode */
        if(mUseProfile.mainInteraction == UseProfile.interaction.touch_mode){
            touchListener.onCheckedChanged(mTouchRadio, true);
        } else {
            mScanRadio.setChecked(true);
            touchListener.onCheckedChanged(mTouchRadio, false);
        }
        
        /* Set interaction mode */
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
        
        switch(mUseProfile.scanMode){
        case UseProfile.interaction.simple_scan:
            mSimpleScanRadio.setChecked(true);
            break;
        case UseProfile.interaction.row_column_scan:
            mRowColumnScanRadio.setChecked(true);
            break;
        }

        /* Set scan interaction mode */
        mScanSecondsSeekBar.setValue(mUseProfile.scanTimeMillis/1000f);
        
        /* Set paging mode */
        if(mUseProfile.mainInteraction == UseProfile.interaction.touch_mode)
            mPagingStandardAutomaticRadio.setText(mResources.getString(R.string.standardPaging));
        else
            mPagingStandardAutomaticRadio.setText(mResources.getString(R.string.automaticPaging));
        switch(mUseProfile.paginationMode){
        case UseProfile.interaction.pagination_standard:
            mPagingStandardAutomaticRadio.setChecked(true);
            break;
        case UseProfile.interaction.pagination_buttons:
            mPagingLastButtonRadio.setChecked(true);
            break;
        }
    }
    
    protected void captureData()
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
        
        if(mSimpleScanRadio.isChecked()) {
            mUseProfile.scanMode =
                UseProfile.interaction.simple_scan;
        } else {
            mUseProfile.scanMode =
                UseProfile.interaction.row_column_scan;
        }
        
        mUseProfile.scanTimeMillis = (int) (mScanSecondsSeekBar.getValue()*1000f);
        
        // Paging mode
        if(mPagingStandardAutomaticRadio.isChecked()) {
            mUseProfile.paginationMode = UseProfile.interaction.pagination_standard;
        } else {
            mUseProfile.paginationMode = UseProfile.interaction.pagination_buttons;
        }
    }
    
    private OnCheckedChangeListener touchListener =
        new OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                    boolean isChecked)
            {
                mMultitouchRadio.setEnabled(isChecked);
                mPressAndDragRadio.setEnabled(isChecked);
                mSimpleRadio.setEnabled(isChecked);

                mScanSecondsSeekBar.setEnabled(!isChecked);
                
                mSimpleScanRadio.setEnabled(!isChecked);
                mRowColumnScanRadio.setEnabled(!isChecked);
            }
        };
}
