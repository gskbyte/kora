package org.gskbyte.Kora;

import org.gskbyte.Kora.R;
import org.gskbyte.Kora.Settings.Device;
import org.gskbyte.Kora.Settings.SettingsDbAdapter;
import org.gskbyte.Kora.selectionActivity.DeviceAdapter;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class DeviceSelectionActivity extends Activity
{
    private static final String TAG = "DeviceSelectionActivity";

    GridView mGrid;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_selection_layout);
        
        mGrid = (GridView) findViewById(R.id.deviceGrid);
        
        DeviceAdapter da = new DeviceAdapter(this);
        da.addDevice(new Device("uno", null));
        da.addDevice(new Device("dos", null));
        da.addDevice(new Device("tres", null));
        da.addDevice(new Device("cuatro", null));
        
        mGrid.setAdapter(da);
        
    }
}