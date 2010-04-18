package org.gskbyte.kora.device;

public class DeviceSpec
{
    public static final int ACCESS_READ = 1,
                            ACCESS_WRITE = 2,
                            ACCESS_READ_WRITE = 3; // READ | WRITE

    public static final int DEVICE_SCALAR = 0,
    						DEVICE_BINARY = 1,
    						DEVICE_LIGHT = 2,
    						DEVICE_ADJUSTABLE_LIGHT = 3,
                            DEVICE_DOOR = 4,
                            DEVICE_SUNBLIND = 5,
                            DEVICE_THERMOMETER = 6;
    
    public static final int VALUE_BOOLEAN = 1,
                            VALUE_INT     = 2,
                            VALUE_LONG    = 3,
                            VALUE_FLOAT   = 4,
                            VALUE_STRING  = 5;
    
    protected String mSystemName;
    protected int mDeviceType;
    protected int mAccessType;
    
    protected int mValueType;
    protected Object mMinValue, mMaxValue;
    
    public DeviceSpec(String sysName, int deviceType, int accessType, int valueType, Object min, Object max)
    {
        mSystemName = sysName;
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
        mMinValue = other.mValueType;
        mMaxValue = other.mMaxValue;
    }
    
    // getters y setters
    
    public String getSystemName()
    {
    	return mSystemName;
    }
    
    public int getDeviceType()
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
    
    public Object getMix()
    {
    	return mMinValue;
    }
    
    public Object getMax()
    {
    	return mMaxValue;
    }
}
