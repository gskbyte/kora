package org.gskbyte.kora.device;

import java.util.HashMap;
import java.util.Vector;

public class DeviceManager
{
    public static Vector<Device> sDevices = new Vector<Device>();
    public static HashMap<String, Integer> sDeviceMap = new HashMap<String, Integer>();
    
    // almacenar representaciones
    
    public static void init()
    {
        // cargar representaciones de dispositivos
    }
    
    public static void connect()
    {
        // conectar a BlueRose, pedir lista de DeviceSpec, y crear devices en consecuencia
    }
    
    public static void disconnect()
    {
        // desconectar de BlueRose
    }
    
    public static int getNumberOfDevices()
    {
        return sDevices.size();
    }
    
    public static Device getDevice(int index)
    {
        return null;
    }
    
    public static Device getDevice(String sysName)
    {
        return null;
    }
}
