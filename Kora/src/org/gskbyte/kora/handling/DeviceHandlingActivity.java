package org.gskbyte.kora.handling;

import java.util.Set;
import java.util.Vector;

import org.gskbyte.kora.R;
import org.gskbyte.kora.customViews.GridLayout;
import org.gskbyte.kora.customViews.KoraButton;
import org.gskbyte.kora.customViews.deviceViews.DeviceBinaryButton;
import org.gskbyte.kora.customViews.deviceViews.DeviceBinarySelector;
import org.gskbyte.kora.customViews.deviceViews.DeviceSlider;
import org.gskbyte.kora.customViews.deviceViews.DeviceViewAttributes;
import org.gskbyte.kora.devices.Device;
import org.gskbyte.kora.devices.DeviceManager;
import org.gskbyte.kora.devices.DeviceRepresentation;
import org.gskbyte.kora.devices.Device.DeviceEventListener;
import org.gskbyte.kora.devices.DeviceRepresentation.BinaryControl;
import org.gskbyte.kora.devices.DeviceRepresentation.Control;
import org.gskbyte.kora.devices.DeviceRepresentation.ScalarControl;
import org.gskbyte.kora.profiles.ProfilesManager;
import org.gskbyte.kora.profiles.UseProfile;
import org.ugr.bluerose.events.Value;

import android.app.Activity;
import android.content.pm.ActivityInfo;
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
    private Device mDevice;
    
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
        mBackButton.setOnClickListener(backListener);
        
        // Cargar dispositivo a manejar, y representación adecuada
        Bundle extras = getIntent().getExtras();
        mDeviceName = extras.getString(TAG_DEVICE_NAME);
        mDevice = DeviceManager.getDevice(mDeviceName);
        mRepr = mDevice.getRepresentation();
        
        // Configurar la vista según el perfil de usuario
        configureView();
        
        // Rellenar la rejilla
        fillView();
    }
    
    // Gran parte de los atributos los pillo de la actividad anterior
    private void configureView()
    {
        mGrid.setDimensions(mDevice.getRepresentation().getControlsCount(), 1);
        mAttr = ViewManager.getAttributes();
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
        mBackButton.setAttributes(ViewManager.getAttributes(ViewManager.COLOR_INDEX_BACK, true));
        mBackButton.setIcon(iconBackId);

        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_BEHIND);
        //setRequestedOrientation(5);
    }
    
    private void fillView()
    {
        mControls = new Vector<DeviceEventListener>();
        
        Set<String> controls = mRepr.getControlNames();
        int btn_index = 0;
        for(String s: controls){
            Control dc = mRepr.getControl(s);
            DeviceEventListener l = null;
            if(dc.getClass() == BinaryControl.class){
                switch(dc.access){
                case Control.ACCESS_READ:
                    break;
                case Control.ACCESS_WRITE:
                    l = new DeviceBinarySelector(this,
                            ViewManager.getAttributes(btn_index),
                            ViewManager.getAttributes(btn_index+1),
                            mDevice, mDevice.getRepresentation(), (BinaryControl)dc);
                    ++btn_index;
                    break;
                case Control.ACCESS_READ_WRITE:
                    l = new DeviceBinaryButton(this,
                            ViewManager.getAttributes(btn_index),
                            mDevice, mDevice.getRepresentation(), (BinaryControl)dc);
                    break;
                }
            } else if (dc.getClass() == ScalarControl.class) {
                l = new DeviceSlider(this,
                        ViewManager.getAttributes(btn_index, true),
                        mDevice, mDevice.getRepresentation(), (ScalarControl)dc);
                /*l = new DeviceScalarSelector(this,
                        ViewManager.getAttributes(btn_index, true),
                        ViewManager.getAttributes(btn_index+1, true),
                        mDevice, mDevice.getRepresentation(), (ScalarControl)dc);*/
                ++btn_index;
                
            }
            
            if(l!=null){
                mControls.add(l);
                mGrid.addView((View)l);
                ++btn_index;
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
