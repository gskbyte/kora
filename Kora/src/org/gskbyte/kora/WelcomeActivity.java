package org.gskbyte.kora;


import org.gskbyte.kora.R;
import org.gskbyte.kora.device.DeviceManager;
import org.gskbyte.kora.handling.DeviceSelectionActivity;
import org.gskbyte.kora.profiles.ProfilesManager;
import org.gskbyte.kora.profiles.User;
import org.gskbyte.kora.profiles.ProfilesManager.SettingsException;
import org.gskbyte.kora.profilesActivities.ProfilesContainerActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class WelcomeActivity extends Activity
{
	private static final String TAG = "WelcomeActivity";

	private static final int INFO_DIALOG_ID = 0;

	private Button mStartButton, mProfilesButton, mSettingsButton;
	private ImageView mInfoButton;
	private TextView mAutostartText;

	private CountDownTimer mTimer;

	private Resources mResources;
	private User mCurrentUser;
	
	private boolean mJustStarted = true;
	

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        
		setContentView(R.layout.main);

		mResources = getResources();
		
		mStartButton = (Button) findViewById(R.id.startButton);
        mProfilesButton = (Button) findViewById(R.id.profilesButton);
		mSettingsButton = (Button) findViewById(R.id.settingsButton);
		mInfoButton = (ImageView) findViewById(R.id.infoButton);
		mAutostartText = (TextView) findViewById(R.id.autostart);

		// Asociar eventos con botones
		mStartButton.setOnClickListener(startButtonListener);
		mProfilesButton.setOnClickListener(profilesButtonListener);
        mSettingsButton.setOnClickListener(settingsButtonListener);
		mInfoButton.setOnClickListener(infoButtonListener);
		
		// Cargar datos de programa (usuarios, perfiles, etc)
		try {
            ProfilesManager.init(this);
        } catch (SettingsException e) {
            Log.e(TAG, e.getMessage());
        }
        
        // Iniciar gestor de dispositivos
        DeviceManager.init(this);
	}
	
	public void onResume()
	{
        /* Set simulation mode, if set */
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean simulate = prefs.getBoolean("simulation", true);
        DeviceManager.setSimulationMode(simulate);
	    
        /* Load user and so */
        mCurrentUser = ProfilesManager.getCurrentUser();
	    // Iniciar cuenta atrás si el usuario quiere comienzo automático
        if (mCurrentUser.wantsAutoStart() && mJustStarted) {
            mJustStarted = false;
            int countdownMilliseconds = mCurrentUser.getAutoStartSeconds() * 1000;
            setAutostartText(mCurrentUser.getName(), mCurrentUser.getAutoStartSeconds());
            mTimer = new CountDownTimer(countdownMilliseconds, 1000) {
                String name = WelcomeActivity.this.mCurrentUser.getName();
                public void onTick(long millisLeft) {
                    WelcomeActivity.this.setAutostartText(name,
                                                       (int) (millisLeft/1000));
                }

                public void onFinish() {
                    startDeviceSelectionActivity();
                }
            };
            mTimer.start();
        } else {
            String text = mResources.getString(R.string.user) + ": " +
                          mCurrentUser.getName();
            mAutostartText.setText(text);
        }
        super.onResume();
	}
	
	private void setAutostartText(String user_name, int seconds_left)
	{
	    final String willStart = mResources.getString(R.string.autostartText1),
                     seconds = mResources.getString(R.string.autostartText2);
        String text = user_name + " " + willStart + " " +seconds_left +
                      " " + seconds;
        mAutostartText.setText(text);
	}
	
	public void onPause()
	{
	    stopCountDown();
	    super.onPause();
	}
	
	public void onDestroy()
	{
        ProfilesManager.finish();
        super.onDestroy();
	}
	
	private void stopCountDown()
	{
		if (mTimer != null)
			mTimer.cancel();
		String text = mResources.getString(R.string.user) + ": " +
        mCurrentUser.getName();
		mAutostartText.setText(text);
	}

	
	private void startDeviceSelectionActivity() {
		Intent i = new Intent(WelcomeActivity.this, DeviceSelectionActivity.class);
		startActivity(i);
	}
	
	
	private OnClickListener startButtonListener = new OnClickListener() {
		public void onClick(View v) {
			startDeviceSelectionActivity();
		}
	};

	
	private OnClickListener profilesButtonListener = new OnClickListener() {
		public void onClick(View v) {
			Intent i = new Intent(WelcomeActivity.this, ProfilesContainerActivity.class);
			startActivity(i);
		}
	};
	
	
    private OnClickListener settingsButtonListener = new OnClickListener() {
        public void onClick(View v) {
            Intent i = new Intent(WelcomeActivity.this, SettingsActivity.class);
            startActivity(i);
        }
    };

	private OnClickListener infoButtonListener = new OnClickListener() {
		public void onClick(View v) {
		    Intent i = new Intent(WelcomeActivity.this, InfoDialogActivity.class);
            startActivity(i);
		}
	};
	
}