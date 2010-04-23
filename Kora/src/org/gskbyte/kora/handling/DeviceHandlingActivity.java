package org.gskbyte.kora.handling;

import java.util.Set;

import org.gskbyte.kora.R;
import org.gskbyte.kora.customViews.GridLayout;
import org.gskbyte.kora.customViews.deviceViews.DeviceBinaryButton;
import org.gskbyte.kora.customViews.deviceViews.DeviceSelectionButton;
import org.gskbyte.kora.customViews.koraButton.KoraButton;
import org.gskbyte.kora.device.Device;
import org.gskbyte.kora.device.DeviceControl;
import org.gskbyte.kora.device.DeviceManager;
import org.gskbyte.kora.device.DeviceRepresentation;
import org.gskbyte.kora.settings.SettingsManager;
import org.gskbyte.kora.settings.UseProfile;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;

public class DeviceHandlingActivity extends Activity
{
    private static final String TAG = "DeviceHandlingActivity";
    public static final String TAG_DEVICE_NAME = "deviceName";
    private GridLayout mGrid;
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_selection_layout);
        
        // Cargar componentes de la vista
        mGrid = (GridLayout) findViewById(R.id.deviceGrid);
        mGrid.setDimensions(3, 3);
        
        UseProfile up = SettingsManager.getCurrentUseProfile();
    	DeviceBinaryButton.Attributes mAttr = new DeviceBinaryButton.Attributes();
        switch(up.iconMode){
    	case UseProfile.visualization.icon_high_contrast:
    		((DeviceBinaryButton.Attributes)mAttr).icon = DeviceRepresentation.ICON_HIGH_CONTRAST;
    		break;
    	case UseProfile.visualization.icon_black_white:
    		((DeviceBinaryButton.Attributes)mAttr).icon = DeviceRepresentation.ICON_BLACK_WHITE;
    		break;
    	case UseProfile.visualization.icon_photo:
    		((DeviceBinaryButton.Attributes)mAttr).icon = DeviceRepresentation.ICON_PHOTO;
    		break;
    	case UseProfile.visualization.icon_animation:
    		((DeviceBinaryButton.Attributes)mAttr).icon = DeviceRepresentation.ICON_ANIMATION;
    		break;
    	case UseProfile.visualization.icon_pictogram:
		default:
    		((DeviceBinaryButton.Attributes)mAttr).icon = DeviceRepresentation.ICON_DEFAULT;
    		break;
    	}
        
        Bundle extras = getIntent().getExtras();
        String deviceName = extras.getString(TAG_DEVICE_NAME);
        
        Device d = DeviceManager.getDevice(deviceName);
        DeviceRepresentation dr = d.getRepresentation();
        
        Set<String> controls = dr.getDeviceControlNames();
        for(String s: controls){
        	DeviceControl dc = dr.getControl(s);
        	DeviceBinaryButton bt = new DeviceBinaryButton(this, mAttr, d.getSystemName(), dc.getName());
    		mGrid.addView(bt);
    		
        	/*
        	int nicons = dc.getStateCount();
        	for(int i=0; i<nicons; ++i){
        		Bitmap b = dc.getIcon(mAttr.icon, i);
        		KoraButton bt = new KoraButton(this, "prueba", b, mAttr);
        		mGrid.addView(bt);
        	}*/
        }
        
        
        /*
        KoraButton.Attributes attr = new KoraButton.Attributes();
        DeviceControl dc = dr.getDeviceControl("intensity");
        Bitmap b = dc.getIcon(DeviceRepresentation.ICON_HIGH_CONTRAST, 4);
        Bitmap b2 = dc.getIcon(DeviceRepresentation.ICON_BLACK_WHITE, 4);
        Bitmap b3 = dc.getIcon(DeviceRepresentation.ICON_DEFAULT, 2);
        Bitmap b4 = dc.getIcon(DeviceRepresentation.ICON_PHOTO, 4);
        
        KoraButton bt = new KoraButton(this, "prueba", b, attr);
        KoraButton bt2 = new KoraButton(this, "prueba2", b2, attr);
        KoraButton bt3 = new KoraButton(this, "prueba3", b3, attr);
        KoraButton bt4 = new KoraButton(this, "prueba4", b4, attr);
        
        mGrid.addView(bt);
        mGrid.addView(bt2);
        mGrid.addView(bt3);
        mGrid.addView(bt4);
        */
    }
}
