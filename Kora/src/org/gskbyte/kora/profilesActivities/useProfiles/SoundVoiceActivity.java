package org.gskbyte.kora.profilesActivities.useProfiles;

import org.gskbyte.kora.R;
import org.gskbyte.kora.customViews.detailedSeekBar.ArraySeekBar;
import org.gskbyte.kora.customViews.detailedSeekBar.IntegerSeekBar;
import org.gskbyte.kora.profiles.UseProfile;

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
import android.widget.RadioButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class SoundVoiceActivity extends ProfilePropertiesActivity
{
    private static final String TAG = "SoundVoiceActivity";
    
    RadioButton noSoundRadio, simpleSoundsRadio, voiceRadio;
    
    CheckBox onItemSelectionCheckBox, onActionCheckBox;
    
    RadioButton defaultVoiceRadio, customVoiceRadio;
    
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_LEFT_ICON);
        setContentView(R.layout.use_profile_sound);
        getWindow().setFeatureDrawableResource(Window.FEATURE_LEFT_ICON,
                                               R.drawable.icon_sound);
        
        initButtonBar();
        
        /* Load views */
        noSoundRadio = (RadioButton) findViewById(R.id.noSoundRadio);
        simpleSoundsRadio = (RadioButton) findViewById(R.id.simpleSoundsRadio);
        voiceRadio = (RadioButton) findViewById(R.id.voiceRadio);
        
        onItemSelectionCheckBox = (CheckBox) findViewById(R.id.onItemSelectionCheckBox);
        onActionCheckBox = (CheckBox) findViewById(R.id.onActionCheckBox);
        
        defaultVoiceRadio = (RadioButton) findViewById(R.id.defaultVoiceRadio);
        customVoiceRadio = (RadioButton) findViewById(R.id.customVoiceRadio);
        
        /* Add listeners */
        voiceRadio.setOnCheckedChangeListener(voiceListener);
    }
    
    protected void setView()
    {
        /* Set main sound mode */
        switch(mUseProfile.soundMode){
        case UseProfile.sound.no_sounds:
            noSoundRadio.setChecked(true);
            break;
        case UseProfile.sound.simple_sounds:
            simpleSoundsRadio.setChecked(true);
            break;
        case UseProfile.sound.voice_sounds:
            voiceRadio.setChecked(true);
            break;
        }
        
        onItemSelectionCheckBox.setChecked(mUseProfile.soundOnSelection);
        onActionCheckBox.setChecked(mUseProfile.soundOnAction);
        
        switch(mUseProfile.voiceMode){
        case UseProfile.sound.voice_default:
            defaultVoiceRadio.setChecked(true);
            break;
        case UseProfile.sound.voice_custom:
            customVoiceRadio.setChecked(true);
            break;
        }
    }
    
    protected void captureData()
    {
        if(noSoundRadio.isChecked()) {
            mUseProfile.soundMode = UseProfile.sound.no_sounds;
        } else if(simpleSoundsRadio.isChecked()) {
            mUseProfile.soundMode = UseProfile.sound.simple_sounds;
        } else {
            mUseProfile.soundMode = UseProfile.sound.voice_sounds;
        }
        
        mUseProfile.soundOnSelection = onItemSelectionCheckBox.isChecked();
        mUseProfile.soundOnAction = onActionCheckBox.isChecked();
        
        if(defaultVoiceRadio.isChecked()) {
            mUseProfile.voiceMode = UseProfile.sound.voice_default;
        } else {
            mUseProfile.voiceMode = UseProfile.sound.voice_custom;
        }
    }
    
    private OnCheckedChangeListener voiceListener =
        new OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                    boolean isChecked)
            {
                defaultVoiceRadio.setEnabled(isChecked);
                customVoiceRadio.setEnabled(isChecked);
            }
        };
}
