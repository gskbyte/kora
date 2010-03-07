package org.gskbyte.Kora;


import org.gskbyte.Kora.R;
import org.gskbyte.Kora.Settings.*;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class WelcomeActivity extends Activity
{
    private static final String TAG = "WelcomeActivity";

    static final int ID_DIALOG_INFO = 1;
    
    private Button startButton, settingsButton, infoButton;
    private TextView autostartText;
    
    private void init()
    {
        SettingsManager.init(this);
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        startButton = (Button)findViewById(R.id.startButton);
        settingsButton = (Button)findViewById(R.id.settingsButton);
        infoButton = (Button) findViewById(R.id.infoButton);
        autostartText =  (TextView)findViewById(R.id.autostart);
        
        // Asociar eventos con botones
        startButton.setOnClickListener(startButtonListener);
        settingsButton.setOnClickListener(settingsButtonListener);
        infoButton.setOnClickListener(infoButtonListener);
        
        // Cargar datos de programa (usuarios, perfiles, etc)
        init();
        
        SharedPreferences settings = this.getSharedPreferences("global_settings", 0);
        String nombre = settings.getString("mCurrentUser", "Default---");
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("mCurrentUser", "random");
        editor.commit();
        
        // Cargar estos datos de donde corresponde
        String userName = nombre,
            willStart = getResources().getString(R.string.autostartText1),
            seconds = getResources().getString(R.string.autostartText2);
        int seconds_left  = 10;
        autostartText.setText(userName + " " + willStart + " " + seconds_left + " " + seconds);
        
    }
    
    protected Dialog onCreateDialog(int id)
    {
        Dialog dialog;
        switch(id)
        {
        case ID_DIALOG_INFO:
        	String appName = getResources().getString(R.string.appName),
        	ok = getResources().getString(R.string.ok),
        	version = getResources().getString(R.string.version) + ": " +
        	          getResources().getString(R.string.version_value),
        	author = getResources().getString(R.string.author) + ": " +
        	         getResources().getString(R.string.author_value),
        	license = getResources().getString(R.string.license);
        	
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(version + "\n" + author + "\n" + license)
                   .setIcon(getResources().getDrawable(R.drawable.icon_info))
                   .setTitle(appName)
                   .setNeutralButton(ok , new DialogInterface.OnClickListener() {
                       public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                       }
                   });
            dialog = builder.create();
            break;
        default:
            dialog = null;
        }

        return dialog;
    }
    
    private OnClickListener startButtonListener = new OnClickListener() {
        public void onClick(View v) {
            Intent i = new Intent(WelcomeActivity.this, ControlActivity.class);
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
    	public void onClick(View v){
    		showDialog(WelcomeActivity.ID_DIALOG_INFO);
    	}
    };
}