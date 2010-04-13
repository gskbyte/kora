package org.gskbyte.kora;

import java.io.File;

import org.gskbyte.kora.R;
import org.gskbyte.kora.handling.DeviceSelectionActivity;
import org.gskbyte.kora.settings.SettingsManager;
import org.gskbyte.kora.settings.User;
import org.gskbyte.kora.settings.SettingsManager.SettingsException;
import org.gskbyte.kora.settingsActivities.SettingsActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class WelcomeActivity extends Activity {
	private static final String TAG = "WelcomeActivity";

	private static final int INFO_DIALOG_ID = 0;

	private Button mStartButton, mSettingsButton;
	private ImageView mInfoButton;
	private TextView mAutostartText;

	private CountDownTimer mTimer;

	private Resources mResources;
	private SettingsManager mSettingsManager;
	private User mCurrentUser;
	
	private boolean mJustStarted = true;
	

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        
		setContentView(R.layout.main);

		mResources = getResources();
		
		mStartButton = (Button) findViewById(R.id.startButton);
		mSettingsButton = (Button) findViewById(R.id.settingsButton);
		mInfoButton = (ImageView) findViewById(R.id.infoButton);
		mAutostartText = (TextView) findViewById(R.id.autostart);

		// Asociar eventos con botones
		mStartButton.setOnClickListener(startButtonListener);
		mSettingsButton.setOnClickListener(settingsButtonListener);
		mInfoButton.setOnClickListener(infoButtonListener);
		
		// Cargar datos de programa (usuarios, perfiles, etc)
		try {
            SettingsManager.init(this);
            mSettingsManager = SettingsManager.getInstance();
        } catch (SettingsException e) {
            Log.e(TAG, e.getMessage());
        }
	}
	
	public void onResume()
	{
        mCurrentUser = mSettingsManager.getCurrentUser();
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
        SettingsManager.finish();
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

	// Load ControlActivity
	private void startDeviceSelectionActivity() {
		Intent i = new Intent(WelcomeActivity.this, DeviceSelectionActivity.class);
		startActivity(i);
	}

	// Listener del bot�n Start
	private OnClickListener startButtonListener = new OnClickListener() {
		public void onClick(View v) {
			startDeviceSelectionActivity();
		}
	};

	// Listener del bot�n Settings
	private OnClickListener settingsButtonListener = new OnClickListener() {
		public void onClick(View v) {
			Intent i = new Intent(WelcomeActivity.this, SettingsActivity.class);
			startActivity(i);
		}
	};

	// Listener del bot�n Info
	private OnClickListener infoButtonListener = new OnClickListener() {
		public void onClick(View v) {
			showDialog(INFO_DIALOG_ID);
		}
	};

	// onCreateDialog
	protected Dialog onCreateDialog(int id) {
        stopCountDown();
        
		Dialog dialog = null;

		switch (id) {
		case INFO_DIALOG_ID:
			dialog = new InfoDialog(this);
			break;
		default:
			dialog = null;
		}

		return dialog;
	}

	// onPrepareDialog
	protected void onPrepareDialog(int id, Dialog dialog) {
		switch (id) {
		case INFO_DIALOG_ID:
			break;
		default:
			dialog = null;
		}
	}
}