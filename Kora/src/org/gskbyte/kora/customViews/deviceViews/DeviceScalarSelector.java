package org.gskbyte.kora.customViews.deviceViews;

import org.gskbyte.kora.customViews.GridLayout;
import org.gskbyte.kora.customViews.KoraButton;
import org.gskbyte.kora.devices.Device;
import org.gskbyte.kora.devices.DeviceManager;
import org.gskbyte.kora.devices.DeviceRepresentation;
import org.gskbyte.kora.devices.Device.DeviceEventListener;
import org.gskbyte.kora.devices.DeviceRepresentation.ScalarControl;
import org.ugr.bluerose.events.Value;

import android.content.Context;
import android.view.View;

public class DeviceScalarSelector extends GridLayout implements DeviceEventListener
{
    protected Device mDevice;
    protected DeviceRepresentation mRepr;
    protected ScalarControl mControl;
    protected int mCurrentState = -1;
    protected KoraButton mDecreaseButton, mIncreaseButton;
    protected Value mDecreaseValue, mIncreaseValue;
    
    private class ResultUpdater implements Runnable
    {
        int mState;
        
        public ResultUpdater(int state){
            mState = state;
        }
        
        public void run(){
            DeviceScalarSelector.this.setState(mState);
        }
    }
    private final android.os.Handler mViewUpdaterHandler = new android.os.Handler();
    
    
    public DeviceScalarSelector(Context context, 
            DeviceViewAttributes attr1, DeviceViewAttributes attr2, 
            Device device, DeviceRepresentation repr, ScalarControl sc)
    {
        super(context);
        
        mDevice = device;
        mDevice.addChangeListener(this);
        mRepr = repr;
        mControl = sc;
        
        mDecreaseButton = new KoraButton(context,
                                       sc.decreaseTag,
                                       null,
                                       attr1);

        mIncreaseButton = new KoraButton(context,
                                       sc.increaseTag,
                                       null,
                                       attr2);
        
        mDecreaseButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v)
                {
                    DeviceManager.setValueForDevice(mDevice.getSystemName(), mDecreaseValue);
                }
            });
        
        mIncreaseButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                DeviceManager.setValueForDevice(mDevice.getSystemName(), mIncreaseValue);
            }
        });

        setState(mDevice.getState());
        
        setDimensions(1, 2);
        addView(mDecreaseButton);
        addView(mIncreaseButton);
    }

    protected void setState(int state)
    {
        if(state != mCurrentState){
            int nstates = mRepr.getStateCount();
            if(state>0 ){
                mDecreaseButton.setIcon(mRepr.getStateIcon(state-1));
                mDecreaseValue = Device.state2Value(mDevice, state-1);
            } else {
                mDecreaseButton.setIcon(mRepr.getStateIcon(0));
                mDecreaseValue = mDevice.getMin();
            }
            
            if(state<nstates-1){
                mIncreaseButton.setIcon(mRepr.getStateIcon(state+1));
                mIncreaseValue = Device.state2Value(mDevice, state+1);
            } else {
                mIncreaseButton.setIcon(mRepr.getStateIcon(nstates-1));
                mIncreaseValue = mDevice.getMax();
            }
            
            mCurrentState = state;
        }
        invalidate();
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