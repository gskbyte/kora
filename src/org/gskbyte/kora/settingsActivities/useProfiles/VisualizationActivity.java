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
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class VisualizationActivity extends Activity
{
    private static final String TAG = "VisualizationActivity";
    
    private Resources mResources;
    private UseProfile mUseProfile;
    
    private RadioButton defaultLAFRadio, plainRadio, differentPlainRadio,
                        highContrastRadio, blackAndWhiteRadio;
    
    private KoraIntegerSeekBar rowsSeekBar, columnsSeekBar;
    
    private CheckBox showTextCheckBox;
    private KoraArraySeekBar textSizeSeekBar;
    private RadioButton sansRadio, calligraphicRadio, comicRadio,
                        textBlackRadio, textWhiteRadio, textCustomColorRadio;
    
    private RadioButton iconRadio, iconHighContrastRadio, iconPhotoRadio,
                        iconAnimationRadio;
    
    private RadioButton pagingStandardAutomaticRadio, pagingLastButtonRadio;
    
    private Button mAcceptButton, mCancelButton;

    
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_LEFT_ICON);
        setContentView(R.layout.use_profile_visualization);
        getWindow().setFeatureDrawableResource(Window.FEATURE_LEFT_ICON,
                                               R.drawable.icon_visualization);
        
        /* Load resources and views */
        mResources = getResources();
        
        defaultLAFRadio = (RadioButton) findViewById(R.id.defaultLAFRadio);
        plainRadio = (RadioButton) findViewById(R.id.plainRadio);
        differentPlainRadio = (RadioButton) findViewById(R.id.differentPlainRadio);
        highContrastRadio = (RadioButton) findViewById(R.id.highContrastRadio);
        blackAndWhiteRadio = (RadioButton) findViewById(R.id.blackAndWhiteRadio);
        
        rowsSeekBar = (KoraIntegerSeekBar) findViewById(R.id.rowsSeekBar);
        columnsSeekBar = (KoraIntegerSeekBar) findViewById(R.id.columnsSeekBar);
        
        showTextCheckBox = (CheckBox) findViewById(R.id.showTextCheckBox);
        textSizeSeekBar = (KoraArraySeekBar) findViewById(R.id.textSizeSeekBar);
        sansRadio = (RadioButton) findViewById(R.id.sansRadio);
        calligraphicRadio = (RadioButton) findViewById(R.id.calligraphicRadio);
        comicRadio = (RadioButton) findViewById(R.id.comicRadio);
        textBlackRadio = (RadioButton) findViewById(R.id.textBlackRadio);
        textWhiteRadio = (RadioButton) findViewById(R.id.textWhiteRadio);
        textCustomColorRadio = (RadioButton) findViewById(R.id.textCustomColorRadio);
        
        iconRadio = (RadioButton) findViewById(R.id.iconRadio);
        iconHighContrastRadio = (RadioButton) findViewById(R.id.iconHighContrastRadio);
        iconPhotoRadio = (RadioButton) findViewById(R.id.iconPhotoRadio);
        iconAnimationRadio = (RadioButton) findViewById(R.id.iconAnimationRadio);
        
        pagingStandardAutomaticRadio = (RadioButton) findViewById(R.id.pagingStandardAutomaticRadio);
        pagingLastButtonRadio = (RadioButton) findViewById(R.id.pagingLastButtonRadio);
        
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
    
    private void setView()
    {
        
    }
    
    private View.OnClickListener acceptListener =
        new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                /* Data capturing */
                
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
