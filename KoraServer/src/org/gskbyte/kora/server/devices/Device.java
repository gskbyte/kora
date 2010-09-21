package org.gskbyte.kora.server.devices;

import org.ugr.bluerose.ByteStreamReader;
import org.ugr.bluerose.ByteStreamWriter;
import org.ugr.bluerose.Marshallable;
import org.ugr.bluerose.events.Value;

public abstract class Device implements Marshallable, Comparable<Device>
{
    public static final int ACCESS_READ = 1,
                            ACCESS_WRITE = 2,
                            ACCESS_READ_WRITE = 3; // READ | WRITE
    
    private String mSystemName;
    private String mReadableName;
    private String mDeviceType;
    private int mAccessMode;
    protected DeviceValue value;
    
    public Device(String systemName, String readableName, String deviceType,
                  int accessMode)
    {
        mSystemName = systemName;
        mReadableName = readableName;
        mAccessMode = accessMode;
        mDeviceType = deviceType;
    }
    
    public final String getSystemName()
    {
        return mSystemName;
    }
    
    public final String getReadableName()
    {
        return mReadableName;
    }
    
    public final String getDeviceType()
    {
        return mDeviceType;
    }
    
    public final int getAccessMode()
    {
        return mAccessMode;
    }
    
    public DeviceValue getDeviceValue()
    {
        return value;
    }
    
    public Value getMaxValue()
    {
        return value.getMax();
    }
    
    public Value getMinValue()
    {
        return value.getMin();
    }

    public abstract Object getValueObj();
    public abstract Value getValue();
    public abstract Value setValue(Value v) throws Exception;
    
    public void marshall(ByteStreamWriter writer)
    {
        writer.writeString(mSystemName);
        writer.writeUTF8String(mReadableName);
        //writer.writeString(mReadableName);
        writer.writeString(mDeviceType);
        writer.writeInteger(mAccessMode);
        writer.writeInteger(value.getCurrent().type);

        value.getMin().marshall(writer);
        value.getMax().marshall(writer);
        value.getCurrent().marshall(writer);
    }

    @Override
    public void unmarshall(ByteStreamReader reader)
    {
        mSystemName = reader.readString();
        mReadableName = reader.readUTF8String();
        //mReadableName = reader.readString();
        mDeviceType = reader.readString();
        mAccessMode = reader.readInteger();

        final Value min = new Value();
        min.unmarshall(reader);

        final Value max = new Value();
        max.unmarshall(reader);


        final Value cur = new Value();
        cur.unmarshall(reader);
        
        
        value = new DeviceValue(min, max, cur);
    }
    
    public int compareTo(Device device)
    {
        return mReadableName.compareTo(device.mReadableName);
    }
}