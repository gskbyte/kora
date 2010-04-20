package org.gskbyte.kora.handling;

import org.gskbyte.kora.R;
import org.gskbyte.kora.customViews.GridLayout;
import org.gskbyte.kora.customViews.koraButton.KoraButton;
import org.gskbyte.kora.device.DeviceControl;
import org.gskbyte.kora.device.DeviceManager;
import org.gskbyte.kora.device.DeviceRepresentation;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;

public class DeviceHandlingActivity extends Activity
{
    private static final String TAG = "DeviceHandlingActivity";
    private GridLayout mGrid;
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_selection_layout);
        
        // Cargar componentes de la vista
        mGrid = (GridLayout) findViewById(R.id.deviceGrid);
        mGrid.setDimensions(2, 2);
        
        
        KoraButton.Attributes attr = new KoraButton.Attributes();
        DeviceRepresentation dr = DeviceManager.getDeviceRepresentation("adjustableLight");
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
    }
}
