package org.gskbyte.kora.device;

import org.ugr.bluerose.events.Value;

import android.graphics.Bitmap;

public class Device extends DeviceSpec
{
	protected String mName;
	protected Value mCurrentValue;
	protected DeviceRepresentation mRepr;
	//protected Bitmap mCustomIcon; // alguna vez se usará
	
	public Device(String name, DeviceSpec s, DeviceRepresentation dr)
	{
	    super(s);
	    mName = name;
	    mRepr = dr;
	}
	
	public String getName()
	{
	    return mName;
	}
	
	public Bitmap getIcon(int which)
	{
		return mRepr.getIcon(which);
	}
	
	public DeviceRepresentation getDeviceRepresentation()
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
	}
	
	// operaciones para escribir en el dispositivo
}
