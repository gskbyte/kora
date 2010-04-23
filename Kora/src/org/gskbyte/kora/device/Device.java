package org.gskbyte.kora.device;

import org.ugr.bluerose.events.Value;

import android.graphics.Bitmap;

public class Device extends DeviceSpec
{
	public interface DeviceChangeListener
	{
		public void onDeviceChange(Value newVal);
	}
	
	protected String mName;
	protected Value mCurrentValue;
	protected DeviceRepresentation mRepr;
	//protected Bitmap mCustomIcon; // alguna vez se usará
	protected DeviceChangeListener mListener;
	
	public Device(String name, DeviceSpec s, DeviceRepresentation dr)
	{
	    super(s);
	    mName = name;
	    mRepr = dr;
	    mCurrentValue = mMinValue; // valor provisional que debe rellenarse luego
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
	
	public Value getValue()
	{
		return mCurrentValue;
	}
	
	public void setValue(Value newValue)
	{
		mCurrentValue = newValue;
		if(mListener != null)
			mListener.onDeviceChange(newValue);
	}
		
	public void setChangeListener(DeviceChangeListener listener)
	{
		mListener = listener;
	}
	
	public void unsetListener()
	{
		mListener = null;
	}
}
