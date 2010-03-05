package org.gskbyte.Kora;

import org.gskbyte.Kora.R;
import org.gskbyte.Kora.Settings.SettingsDbAdapter;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ControlActivity extends Activity
{
    private static final String TAG = "ControlActivity";
    
    TextView name, school;
    Button add;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_list);
        
        name = (TextView) findViewById(R.id.title);
        school = (TextView) findViewById(R.id.text);
        add = (Button) findViewById(R.id.addButton);
        
        add.setOnClickListener(listener);
        
    }
    
    private OnClickListener listener = new OnClickListener() {


        @Override
        public void onClick(View v)
        {
            changeText("título", "nombre");
        }
        
    };
    
    protected void changeText(String t, String t2){
        name.setText(t);
        school.setText(t2);
    }
}