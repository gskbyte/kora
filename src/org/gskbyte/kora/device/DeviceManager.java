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
    
    protected static Vector<Device> sDevices;
    protected static HashMap<String, Integer> sDevicesMap;
    protected static HashMap<String, DeviceRepresentation> sDeviceRepsMap;
        
    public static void init(Context c)
    {
        sContext = c;
        
        sDeviceRepsMap = new HashMap<String, DeviceRepresentation>();
        
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
                    sDeviceRepsMap.put(dr.getName(), dr);
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
        sDevices = new Vector<Device>();
        sDevicesMap = new HashMap<String, Integer>();
        
        // conectar a BlueRose, pedir lista de DeviceSpec, y crear devices en consecuencia
    	
    	Vector<DeviceSpec> specs = new Vector<DeviceSpec>();
    	
    	DeviceSpec s1 = new DeviceSpec("roomLight",
    			"Luz del pasillo",
    			"simpleLight",
    			DeviceSpec.ACCESS_READ_WRITE,
    			DeviceSpec.VALUE_BOOLEAN,
    			new Boolean(false),
    			new Boolean(true));
    	
    	DeviceSpec s2 = new DeviceSpec("tableLight",
    			"Luz de la mesita",
    			"adjustableLight",
    			DeviceSpec.ACCESS_READ_WRITE,
    			DeviceSpec.VALUE_INTEGER,
    			new Integer(10),
    			new Integer(0));

    	DeviceSpec s3 = new DeviceSpec("otro",
    			"Cacharro",
    			"other",
    			DeviceSpec.ACCESS_READ_WRITE,
    			DeviceSpec.VALUE_INTEGER,
    			new Integer(10),
    			new Integer(0));
    	
    	specs.add(s1);
    	specs.add(s3);
    	specs.add(s2);
    	
    	for(DeviceSpec s : specs){
    		DeviceRepresentation dr = sDeviceRepsMap.get(s.getDeviceType());
    		if(dr==null){ // si no tengo representación para este cacharrico
    			// Si no existe representación adecuada, coger el defaultScalar o el defaultBinary
    			if(s.getAccessType() == DeviceSpec.VALUE_BOOLEAN){
    				dr = sDeviceRepsMap.get("defaultBinary");
    			} else {
    				dr = sDeviceRepsMap.get("defaultScalar");
    			}
    		}
    		
    		// CAMBIAR ESTO       VVVVVVVVVVVVVVVVV
    		Device d = new Device(s.getName(), s, dr);
    		sDevices.add(d);
    		sDevicesMap.put(d.getSystemName(), sDevices.size()-1);
    	}
    }
    
    public static void disconnect()
    {
        // desconectar de BlueRose
    }
    
    public static int getNumberOfDevices()
    {
        return sDevices.size();
    }
    
    public static DeviceRepresentation getDeviceRepresentation(String name)
    {
    	return sDeviceRepsMap.get(name);
    }
    
    public static Device getDevice(int index)
    {
        return sDevices.elementAt(index);
    }
    
    public static Device getDevice(String sysName)
    {
    	int index = sDevicesMap.get(sysName);
    	if(index>=0)
    		return sDevices.elementAt(index);
    	else
    		return null;
    }
    
    public static String getDeviceSystemName(int index)
    {
    	return sDevices.get(index).getSystemName();
    }
}
