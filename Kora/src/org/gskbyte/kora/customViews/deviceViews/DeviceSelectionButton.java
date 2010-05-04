package org.gskbyte.kora.customViews.deviceViews;

import org.gskbyte.kora.customViews.KoraButton;
import org.gskbyte.kora.devices.Device;
import org.gskbyte.kora.devices.DeviceRepresentation;
import org.gskbyte.kora.devices.Device.DeviceEventListener;
import org.gskbyte.kora.handling.DeviceHandlingActivity;
import org.ugr.bluerose.events.Value;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;

public class DeviceSelectionButton extends KoraButton implements DeviceEventListener
{
    Device mDevice;
    DeviceRepresentation mRepr;
    int mCurrentState;
	
    /* Necesario para actualizar la vista en la hebra principal */
    private class ResultUpdater implements Runnable
    {
        int mStateIndex;
        
        public ResultUpdater(int state_index){
            mStateIndex = state_index;
        }
        
        public void run(){
            DeviceSelectionButton.this.setIcon(mRepr.getStateIcon(mStateIndex));
        }
    }
    private final android.os.Handler mViewUpdaterHandler = new android.os.Handler();
    
	public DeviceSelectionButton(Context context, DeviceViewAttributes attr,
						         Device device)
	{
		super(context);
		
		// Get device and add listener to call handling activity
		mDevice = device;
		device.addChangeListener(this);
		
		// Init view
		mRepr = device.getRepresentation();
		Value v = device.getValue();
		mCurrentState = mDevice.getState();
        Bitmap icon = mRepr.getStateIcon(mCurrentState);
        init(mDevice.getReadableName(), icon, attr);
		
		View.OnClickListener l = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), DeviceHandlingActivity.class);
                i.putExtra(DeviceHandlingActivity.TAG_DEVICE_NAME, mDevice.getSystemName());
                
                getContext().startActivity(i);
            }
        };
        setOnClickListener(l);
	}
	
    @Override
    public void onDeviceChange(String deviceName, Value newVal)
    {
        if(deviceName.equals(mDevice.getSystemName())){
            int state = mDevice.getState();
            if(mCurrentState != state){
                mViewUpdaterHandler.post( new ResultUpdater(state) );
                mCurrentState = state;
            }
        }
    }
    @Override
    public void unregister()
    {
        mDevice.removeListener(this);
    }
}
