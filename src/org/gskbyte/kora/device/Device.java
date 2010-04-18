package org.gskbyte.kora.device;

import android.graphics.Bitmap;

public class Device extends DeviceSpec
{
	private String mName;
	protected Bitmap mIcon;
	
	// representación de controles?
	
	public Device(String name, DeviceSpec s, DeviceRepresentation dr)
	{
	    super(s);
	    mName = name;
	}
	
	public String getName()
	{
	    return mName;
	}
	
	
}
