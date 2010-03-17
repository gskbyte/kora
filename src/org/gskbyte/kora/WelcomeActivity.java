package org.gskbyte.kora;

import org.gskbyte.kora.R;
import org.gskbyte.kora.settings.SettingsManager;
import org.gskbyte.kora.settings.User;
import org.gskbyte.kora.settings.SettingsManager.SettingsException;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class WelcomeActivity extends Activity {
	private static final String TAG = "WelcomeActivity";

	private static final int INFO_DIALOG_ID = 0;

	private Button startButton, settingsButton, infoButton;
	private TextView autostartText;

	private CountDownTimer timer;

	private SettingsManager mSettingsManager;

	private void init() {
		SettingsManager.init(this);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		startButton = (Button) findViewById(R.id.startButton);
		settingsButton = (Button) findViewById(R.id.settingsButton);
		infoButton = (Button) findViewById(R.id.infoButton);
		autostartText = (TextView) findViewById(R.id.autostart);

		// Asociar eventos con botones
		startButton.setOnClickListener(startButtonListener);
		settingsButton.setOnClickListener(settingsButtonListener);
		infoButton.setOnClickListener(infoButtonListener);

		// Cargar datos de programa (usuarios, perfiles, etc)
		init();

		loadCountdown();
	}
	
	private void loadCountdown() {

		User currentUser = null;

		try {
			mSettingsManager = SettingsManager.getInstance();
			currentUser = mSettingsManager.getCurrentUser();
		} catch (SettingsException e) {
			Log.e(TAG, e.getMessage());
		}

		// Cargar estos datos de donde corresponde
		// String userName = nombre,
		// willStart = getResources().getString(R.string.autostartText1),
		// seconds = getResources().getString(R.string.autostartText2);
		// int seconds_left = 10;
		// autostartText.setText(userName + " " + willStart + " " + seconds_left
		// + " " + seconds);

		int countdownMilliseconds = 10000;
		if (currentUser != null) {
			countdownMilliseconds = currentUser.getAutoStartSeconds() * 1000;
		}

		// Start the ControlActivity after a few seconds
		timer = new CountDownTimer(countdownMilliseconds, 1000) {
			public void onTick(long millisLeft) {
				/// PONER NOMBRE!!!
				String willStart = getResources().getString(R.string.autostartText1),
		        seconds = getResources().getString(R.string.autostartText2);

				autostartText.setText("Default" + " " + willStart + " " + millisLeft/1000 + " " + seconds);
			}

			public void onFinish() {
				startDeviceSelectionActivity();
			}
		};
		
		timer.start();
	}
	
	private void stopCountDown()
	{
		if (timer != null)
			timer.cancel();
		autostartText.setVisibility(View.INVISIBLE);
	}

	// Load ControlActivity
	private void startDeviceSelectionActivity() {
		stopCountDown();
		Intent i = new Intent(WelcomeActivity.this, DeviceSelectionActivity.class);
		startActivity(i);
	}

	// Listener del bot�n Start
	private OnClickListener startButtonListener = new OnClickListener() {
		public void onClick(View v) {
			stopCountDown();
			startDeviceSelectionActivity();
		}
	};

	// Listener del bot�n Settings
	private OnClickListener settingsButtonListener = new OnClickListener() {
		public void onClick(View v) {
			stopCountDown();
			Intent i = new Intent(WelcomeActivity.this, SettingsActivity.class);
			startActivity(i);
		}
	};

	// Listener del bot�n Info
	private OnClickListener infoButtonListener = new OnClickListener() {
		public void onClick(View v) {
			stopCountDown();
			showDialog(INFO_DIALOG_ID);
		}
	};

	// onCreateDialog
	protected Dialog onCreateDialog(int id) {
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