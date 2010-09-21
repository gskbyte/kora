package org.gskbyte.kora.server.devices;

import java.util.Collection;
import java.util.TreeMap;

public class DeviceManager
{
    private static DeviceManager sInstance;
    private static TreeMap<String, Device> devices = new TreeMap<String, Device>();
    
    public static DeviceManager instance()
    {
        if(sInstance == null)
            sInstance = new DeviceManager();
        return sInstance;
    }
    
    public DeviceManager()
    {
        
    }
    
    public void put(Device device)
    {
        devices.put(device.getSystemName(), device);
    }
    
    public Collection<Device> getAll()
    {
        return devices.values();
    }
    
    public Collection<String> getDeviceNames()
    {
        return devices.keySet();
    }

    public Device getDevice(String name)
    {
        return devices.get(name);
    }
}
