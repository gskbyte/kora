package org.gskbyte.kora.device;

public class DeviceSpec
{
    public static final int ACCESS_READ = 1,
                            ACCESS_WRITE = 2,
                            ACCESS_READ_WIRTE = 3; // READ | WRITE

    public static final int DEVICE_LIGHT = 0,
                            DEVICE_DOOR = 1,
                            DEVICE_SUNBLIND = 2,
                            DEVICE_THERMOMETER = 3;
    
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
}
