package org.gskbyte.kora.devices;

import org.ugr.bluerose.events.Value;

import android.graphics.Bitmap;

public class Device extends DeviceSpec
{
	public interface DeviceEventListener
	{
		public void onDeviceChange(String deviceName, Value newVal);
		public void unregister();
	}
	
	protected String mName;
	protected DeviceRepresentation mRepr;
	//protected Bitmap mCustomIcon; // alguna vez se usará
	protected DeviceEventListener mListener;
	
	public Device(String name, DeviceSpec s, DeviceRepresentation dr)
	{
	    super(s);
	    mName = name;
	    mRepr = dr;
	}
	
	public String getReadableName()
	{
	    return mName;
	}
	
	public Bitmap getIcon(int which)
	{
		return mRepr.getIcon(which);
	}
	
	public DeviceRepresentation getRepresentation()
	{
		return mRepr;
	}
	
	public void setValue(Value newValue)
	{
		mCurrentValue = newValue;
        if(mListener != null)
            mListener.onDeviceChange(mSystemName, newValue);
	}
	
	public void setChangeListener(DeviceEventListener listener)
	{
		mListener = listener;
	}
	
	public void unsetListener()
	{
		mListener = null;
	}
}
