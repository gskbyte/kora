package org.gskbyte.kora.device;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Vector;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

public class DeviceManager
{
    public static final String TAG = "DeviceManager";
    
    private static final String DEVICE_REPS_FOLDER = "device_representations";
    
    protected static Context sContext;
    
    protected static Vector<Device> sDevices = 
        new Vector<Device>();
    protected static HashMap<String, Integer> sDeviceMap = 
        new HashMap<String, Integer>();
    protected static HashMap<String, DeviceRepresentation> sDeviceRepsMap = 
        new HashMap<String, DeviceRepresentation>();
    
    // almacenar representaciones
    
    public static void init(Context c)
    {
        sContext = c;
        
        // cargar representaciones de dispositivos
        AssetManager am = c.getAssets();
        DeviceRepresentation.setAssetManager(am);
        try {
            String [] folders = am.list(DEVICE_REPS_FOLDER);
            for(String f : folders){
                String file = DEVICE_REPS_FOLDER+"/"+f+"/"+"description.xml";
                try{
                    InputStream is = am.open(file);
                    DeviceRepresentation dr = new DeviceRepresentation(is, DEVICE_REPS_FOLDER+"/"+f+"/");
                    //sDeviceRepsMap.put(dr.getSystemName(), dr);
                    int i = 5;
                    i = 4;
                } catch (IOException e) {
                    Log.e(TAG, "Can't open device representation: "+ f +
                               ". Is description.xml missing?");
                } catch (Exception e) {
                    Log.e(TAG, "Error parsing file: "+ file);
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "Can't open device representations folder." +
                       " Critical error.");
        }
        
        
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
