package org.gskbyte.kora.customViews.deviceViews;

import java.util.Vector;

import org.gskbyte.kora.customViews.KoraSlider;
import org.gskbyte.kora.devices.Device;
import org.gskbyte.kora.devices.DeviceManager;
import org.gskbyte.kora.devices.DeviceRepresentation;
import org.gskbyte.kora.devices.Device.DeviceEventListener;
import org.gskbyte.kora.devices.DeviceRepresentation.Control;
import org.ugr.bluerose.events.Value;

import android.content.Context;
import android.view.View;

public class DeviceSlider extends KoraSlider implements DeviceEventListener
{
    Device mDevice;
    DeviceRepresentation mRepr;
    DeviceRepresentation.Control mControl;
    Value mCurrentValue;
    
    /* Necesario para actualizar la vista en la hebra principal */
    private class ResultUpdater implements Runnable
    {
        int mState;
        
        public ResultUpdater(int state){
            mState = state;
        }
        
        public void run(){
            DeviceSlider.this.setState(mState);
        }
    }
    private final android.os.Handler mViewUpdaterHandler = new android.os.Handler();
    
    
    public DeviceSlider(Context context, DeviceViewAttributes attr,
                        Device device, DeviceRepresentation dr, Control control)
    {
        super(context);

        mDevice = device;
        mDevice.addChangeListener(this);
        
        // establecer atributos iniciales de representación
        mRepr = dr;
        mControl = control;
        
        // Generar estados de la barra
        Vector<State> states = new Vector<State>();
        for(int i=0; i<dr.getStateCount(); ++i){
            states.add(new State(dr.getStateTag(i), dr.getStateIcon(i)));
        }

        attr.textMaxSize = Attributes.TEXT_LARGE;
        init(states, attr);
        
        setState(mDevice.getState());
        
        View.OnClickListener l = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int state =getCurrentState();
                Value val = Device.state2Value(mDevice, state);
                DeviceManager.setValueForDevice(mDevice.getSystemName(), val);
                setState(state);
            }
        };
        
        setOnClickListener(l);
    }
    
    @Override
    public void onDeviceChange(String deviceName, Value newVal)
    {
        if(deviceName.equals(mDevice.getSystemName()))
            mViewUpdaterHandler.post(new ResultUpdater(mDevice.getState()));
    }

    @Override
    public void unregister()
    {
        mDevice.removeListener(this);
    }
}
