package org.gskbyte.kora;

import org.gskbyte.kora.R;
import org.gskbyte.kora.customViews.DeviceAdapter;
import org.gskbyte.kora.customViews.DeviceSelectionWidget;
import org.gskbyte.kora.customViews.GridLayout;
import org.gskbyte.kora.device.Device;
import org.gskbyte.kora.settings.SettingsDbAdapter;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

public class DeviceSelectionActivity extends Activity
{
    private static final String TAG = "DeviceSelectionActivity";

    GridLayout mGrid;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
        setContentView(R.layout.device_grid_layout);
        
        layout = (RelativeLayout) findViewById(R.id.relativeLayout);
        Button b = (Button) findViewById(R.id.Button03);
        

        WindowManager w = getWindowManager(); 
        Display d = w.getDefaultDisplay(); 
        int width = d.getWidth(); 
        int height = d.getHeight(); 
        
        b.setText(String.valueOf(width)+" x "+String.valueOf(height));
        */
        

        /*
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_layout);
        linear = (LinearLayout) findViewById(R.id.testLayout);

        Resources r = getResources();
        Device d = new Device("Heater", r.getDrawable(R.drawable.icon_device_heater_128));
        DeviceSelectionWidget w = new DeviceSelectionWidget(this, R.drawable.icon_device_heater_128, "Heater");
        linear.addView(w);
*/

        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_selection_layout);
        /*
        mGrid = (GridLayout) findViewById(R.id.deviceGrid);
        mGrid.setClipToPadding(false);
        
        int nchildren = mGrid.getChildCount();
        */
        /*
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_selection_layout);
        
        mGrid = (GridView) findViewById(R.id.deviceGrid);
        //mGrid.setNumColumns(3);
        //int height = mGrid.getMeasuredHeight();
        //int width = mGrid.getMeasuredWidth();
        DeviceAdapter da = new DeviceAdapter(this);
        
        
        Resources r = getResources();
        da.addDevice(new Device("Heater", r.getDrawable(R.drawable.icon_device_heater_128)));
        da.addDevice(new Device("Light", r.getDrawable(R.drawable.icon_device_light_128)));
        da.addDevice(new Device("Sunblind", r.getDrawable(R.drawable.icon_device_sunblind_128)));
        da.addDevice(new Device("Music", r.getDrawable(R.drawable.icon_device_music_128)));
        
        mGrid.setAdapter(da);
        */
    }
    
    
}