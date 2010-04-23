package org.gskbyte.kora.device;

import org.ugr.bluerose.events.Value;

public class DeviceSpec
{
    public static final int ACCESS_READ = 1,
                            ACCESS_WRITE = 2,
                            ACCESS_READ_WRITE = 3; // READ | WRITE
    
    protected String mSystemName;
    protected String mReadableName;
    protected String mDeviceType;
    protected int mAccessType;
    
    protected int mValueType;
    protected Value mMinValue, mMaxValue;
    
    public DeviceSpec(String sysName, String readableName, String deviceType, int accessType, int valueType, Value min, Value max)
    {
        mSystemName = sysName;
        mReadableName = readableName;
        mDeviceType = deviceType;
        mAccessType = accessType;
        mValueType = valueType;
        mMinValue = min;
        mMaxValue = max;
    }
    
    public DeviceSpec(DeviceSpec other)
    {
        mSystemName = other.mSystemName;
        mAccessType = other.mAccessType;
        mValueType = other.mValueType;
        mMinValue = other.mMinValue;
        mMaxValue = other.mMaxValue;
    }
    
    public String getReadableName()
    {
    	return mReadableName;
    }
    
    public String getSystemName()
    {
    	return mSystemName;
    }
    
    public String getDeviceType()
    {
    	return mDeviceType;
    }
    
    public int getAccessType()
    {
    	return mAccessType;
    }
    
    public int getValueType()
    {
    	return mValueType;
    }
    
    public Value getMin()
    {
    	return mMinValue;
    }
    
    public Value getMax()
    {
    	return mMaxValue;
    }
}
