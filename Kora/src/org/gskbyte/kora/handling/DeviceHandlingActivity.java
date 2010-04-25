package org.gskbyte.kora.handling;

import java.util.Set;

import org.gskbyte.kora.R;
import org.gskbyte.kora.customViews.GridLayout;
import org.gskbyte.kora.customViews.KoraView;
import org.gskbyte.kora.customViews.deviceViews.DeviceBinarySelector;
import org.gskbyte.kora.customViews.koraButton.KoraButton;
import org.gskbyte.kora.device.Device;
import org.gskbyte.kora.device.DeviceControl;
import org.gskbyte.kora.device.DeviceManager;
import org.gskbyte.kora.device.DeviceRepresentation;
import org.gskbyte.kora.settings.SettingsManager;
import org.gskbyte.kora.settings.UseProfile;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class DeviceHandlingActivity extends Activity
{
    private static final String TAG = "DeviceHandlingActivity";
    public static final String TAG_DEVICE_NAME = "deviceName";
    public static final String TAG_ATTRIBUTES = "attributes";
    
    private GridLayout mGrid;
    private KoraButton mBackButton;
    
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_handling_layout);
        
        // Cargar componentes de la vista
        mGrid = (GridLayout) findViewById(R.id.deviceGrid);
        mBackButton = (KoraButton) findViewById(R.id.back);
        mGrid.setDimensions(2, 1);
        mBackButton.setOnClickListener(backListener);
        
        Bundle extras = getIntent().getExtras();
        String deviceName = extras.getString(TAG_DEVICE_NAME);
        KoraView.Attributes attr = DeviceSelectionActivity.getAttributes();
        
        Device d = DeviceManager.getDevice(deviceName);
        DeviceRepresentation dr = d.getRepresentation();

        DeviceBinarySelector.Attributes mAttr = new DeviceBinarySelector.Attributes(attr);
        UseProfile up = SettingsManager.getCurrentUseProfile();
        switch(up.iconMode){
        case UseProfile.visualization.icon_high_contrast:
            ((DeviceBinarySelector.Attributes)mAttr).icon = DeviceRepresentation.ICON_HIGH_CONTRAST;
            break;
        case UseProfile.visualization.icon_black_white:
            ((DeviceBinarySelector.Attributes)mAttr).icon = DeviceRepresentation.ICON_BLACK_WHITE;
            break;
        case UseProfile.visualization.icon_photo:
            ((DeviceBinarySelector.Attributes)mAttr).icon = DeviceRepresentation.ICON_PHOTO;
            break;
        case UseProfile.visualization.icon_animation:
            ((DeviceBinarySelector.Attributes)mAttr).icon = DeviceRepresentation.ICON_ANIMATION;
            break;
        case UseProfile.visualization.icon_pictogram:
        default:
            ((DeviceBinarySelector.Attributes)mAttr).icon = DeviceRepresentation.ICON_DEFAULT;
            break;
        }
        
        //mAttr.icon = 
        Set<String> controls = dr.getDeviceControlNames();
        for(String s: controls){
        	DeviceControl dc = dr.getControl(s);
        	if(dc.getType()==DeviceControl.TYPE_BINARY){
        	    DeviceBinarySelector bs = new DeviceBinarySelector(this, mAttr, deviceName, dc);
                mGrid.addView(bs);
        	}
        }
        
        
    }
    
    View.OnClickListener backListener = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                DeviceHandlingActivity.this.finish();
            }
        };
}
