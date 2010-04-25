package org.gskbyte.kora.handling;

import java.util.Set;
import java.util.Vector;

import org.gskbyte.kora.R;
import org.gskbyte.kora.customViews.GridLayout;
import org.gskbyte.kora.customViews.deviceViews.DeviceBinaryButton;
import org.gskbyte.kora.customViews.deviceViews.DeviceBinarySelector;
import org.gskbyte.kora.customViews.deviceViews.DeviceSlider;
import org.gskbyte.kora.customViews.deviceViews.DeviceViewAttributes;
import org.gskbyte.kora.customViews.koraButton.KoraButton;
import org.gskbyte.kora.device.Device;
import org.gskbyte.kora.device.DeviceControl;
import org.gskbyte.kora.device.DeviceManager;
import org.gskbyte.kora.device.DeviceRepresentation;
import org.gskbyte.kora.device.Device.DeviceEventListener;

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
    
    private String mDeviceName;
    
    private DeviceViewAttributes mAttr;
    private DeviceRepresentation mRepr;
    
    private Vector<DeviceEventListener> mControls;
    
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_handling_layout);
        
        // Cargar componentes de la vista
        mGrid = (GridLayout) findViewById(R.id.deviceGrid);
        mBackButton = (KoraButton) findViewById(R.id.back);
        mGrid.setDimensions(2, 1);
        mBackButton.setOnClickListener(backListener);
        
        // Cargar dispositivo a manejar, y representación adecuada
        Bundle extras = getIntent().getExtras();
        mDeviceName = extras.getString(TAG_DEVICE_NAME);
        Device d = DeviceManager.getDevice(mDeviceName);
        mRepr = d.getRepresentation();
        
        // Configurar la vista según el perfil de usuario
        configureView();
        
        // Rellenar la rejilla
        fillView();
    }
    
    public void onDestroy()
    {
        super.onDestroy();
        for(DeviceEventListener dl : mControls)
            dl.unregister();
    }
    
    // Gran parte de los atributos los pillo de la actividad anterior
    private void configureView()
    {
        mAttr = DeviceSelectionActivity.getAttributes();
        int iconBackId;
        switch(mAttr.icon){
        case DeviceRepresentation.ICON_HIGH_CONTRAST:
            iconBackId = R.drawable.icon_back_hc;
            break;
        case DeviceRepresentation.ICON_BLACK_WHITE:
            iconBackId = R.drawable.icon_back_bw;
            break;
        // Para el resto de casos es siempre así
        default:
            iconBackId = R.drawable.icon_back;
            break;
        }
        mBackButton.setIcon(iconBackId);
    }
    
    private void fillView()
    {
        mControls = new Vector<DeviceEventListener>();
        
        Set<String> controls = mRepr.getDeviceControlNames();
        for(String s: controls){
            DeviceControl dc = mRepr.getControl(s);
            DeviceEventListener l = null;
            if(dc.getType()==DeviceControl.TYPE_BINARY){
                switch(dc.getAccessMode()){
                case DeviceControl.ACCESS_READ:
                    break;
                case DeviceControl.ACCESS_WRITE:
                    l = new DeviceBinarySelector(this, mAttr, mDeviceName, dc);
                    break;
                case DeviceControl.ACCESS_READ_WRITE:
                    l = new DeviceBinaryButton(this, mAttr, mDeviceName, dc);
                    break;
                }
            } else {
                switch(dc.getAccessMode()){
                case DeviceControl.ACCESS_READ:
                    break;
                case DeviceControl.ACCESS_WRITE:
                    break;
                case DeviceControl.ACCESS_READ_WRITE:
                    l = new DeviceSlider(this, mAttr, mDeviceName, dc);
                    break;
                }
            }
            if(l!=null){
                mControls.add(l);
                mGrid.addView((View)l);
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
