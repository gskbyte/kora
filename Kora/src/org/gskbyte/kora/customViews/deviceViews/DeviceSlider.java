package org.gskbyte.kora.customViews.deviceViews;

import java.util.Vector;

import org.gskbyte.kora.customViews.koraSlider.KoraSlider;
import org.gskbyte.kora.device.Device;
import org.gskbyte.kora.device.DeviceControl;
import org.gskbyte.kora.device.DeviceManager;
import org.gskbyte.kora.device.Device.DeviceEventListener;
import org.ugr.bluerose.Comparison;
import org.ugr.bluerose.events.Value;

import android.content.Context;
import android.view.View;

public class DeviceSlider extends KoraSlider implements DeviceEventListener
{
    Device mDevice;
    DeviceControl mControl;
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
    
    
    public DeviceSlider(Context context, DeviceViewAttributes attr, String deviceName, DeviceControl dc)
    {
        super(context);

        // establecer atributos iniciales de representación
        attr.textMaxSize = Attributes.TEXT_LARGE;
        Vector<State> states = new Vector<State>();
        for(int i=0; i<dc.getStateCount(); ++i){
            states.add(new State(dc.getStateName(i), dc.getIcon(attr.icon, i)));
        }
        
        init(states, attr);
        
        mDevice = DeviceManager.getDevice(deviceName);
        mDevice.setChangeListener(this);
        mControl = dc;
        
        setState(value2State(mDevice.getValue()));
        
        View.OnClickListener l = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int state =getCurrentState();
                Value val = state2Value(state);
                DeviceManager.setValueForDevice(mDevice.getSystemName(), val);
                setState(state);
            }
        };
        
        setOnClickListener(l);
    }
    
    protected int value2State(Value v)
    {
        int nstates = mControl.getStateCount();
        int ret = 0;
        switch(v.type){
        case Value.BOOLEAN_TYPE:
            ret = (v.getBoolean()) ? nstates-1 : 0;
            break;
        case Value.INTEGER_TYPE:
            int max = mDevice.getMax().getInteger(),
                min = mDevice.getMin().getInteger();
            
            int stepSize = (max - min)/nstates;
            ret = v.getInteger()/stepSize;
            break;
        case Value.FLOAT_TYPE:
            float maxf = mDevice.getMax().getFloat(),
                  minf = mDevice.getMin().getFloat();
            float nstatesf = nstates-1;
            float statef = v.getFloat();
            float stepSizef = (maxf - minf)/nstatesf;
            ret = (int)(statef/stepSizef);
            break;
        }
        return ret;
    }
    
    protected Value state2Value(int state)
    {
        Value ret = new Value();
        int nstates = mControl.getStateCount();
        switch(mDevice.getValueType()){
        case Value.BOOLEAN_TYPE:
            ret.setBoolean(state == 0 ? false : true);
            break;
        case Value.INTEGER_TYPE:
            int max = mDevice.getMax().getInteger(),
                min = mDevice.getMin().getInteger();
            
            ret.setInteger(((max-min)/(nstates-1))*state);
            break;
        case Value.FLOAT_TYPE:
            float maxf = mDevice.getMax().getFloat(),
                  minf = mDevice.getMin().getFloat();
            float nstatesf = nstates-1;
            float statef = state;
            
            ret.setFloat(((maxf-minf)/nstatesf)*statef);
            break;
        }
       
        return ret; 
    }
    
    @Override
    public void onDeviceChange(String deviceName, Value newVal)
    {
        if(deviceName.equals(mDevice.getSystemName()))
            mViewUpdaterHandler.post(new ResultUpdater(value2State(newVal)));
    }

    @Override
    public void unregister()
    {
        mDevice.unsetListener();
    }

}
