package org.gskbyte.kora.device;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Vector;

import org.gskbyte.kora.R;
import org.ugr.bluerose.Initializer;
import org.ugr.bluerose.devices.TcpCompatibleDevice;
import org.ugr.bluerose.events.EventHandler;
import org.ugr.bluerose.events.Value;

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
    protected static DeviceChangeListener sDeviceChangeListener;
    protected static DeviceQueryListener sDeviceQueryListener;
        
    public static void init(Context ctx)
    {
        sContext = ctx;
        
        sDevices = new Vector<Device>();
        sDevicesMap = new HashMap<String, Integer>();
        sDeviceRepsMap = new HashMap<String, DeviceRepresentation>();
        

        /*
         * Cargar representaciones de dispositivos
         */
        
        AssetManager am = ctx.getAssets();
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
        
        /*
         * Listeners 
         */
        
        sDeviceChangeListener = new DeviceChangeListener();
        sDeviceQueryListener = new DeviceQueryListener();
    }
    
    public static void connect()
    {
        sDevices.clear();
        sDevicesMap.clear();
        
        // Conectar con BlueRose
        TcpCompatibleDevice device = new TcpCompatibleDevice();

        try {
        	Log.e(TAG, "Iniciando conexión con BlueRose...");
        	InputStream file = sContext.getResources().openRawResource(R.raw.bluerose_config);
            org.ugr.bluerose.Initializer.initialize(file);
            org.ugr.bluerose.Initializer.initializeClient(device);
        	file.close();
        	Log.e(TAG, "´Exito conectando con BlueRose");
        } catch (Exception ex) {
        	Log.e(TAG, "ERROR CONECTANDO CON BLUEROSE");
        }
        
        // Pedir lista de especificaciones de dispositivos
    	
    	Vector<DeviceSpec> specs = new Vector<DeviceSpec>();
    	
    	Value s1min = new Value(),
    	      s1max = new Value();
    	s1min.setBoolean(false);
    	s1max.setBoolean(true);
    	DeviceSpec s1 = new DeviceSpec("roomLight",
    			"Luz del pasillo",
    			"simpleLight",
    			DeviceSpec.ACCESS_READ_WRITE,
    			Value.BOOLEAN_TYPE,
    			s1min,
    			s1max);
    	
    	Value s2min = new Value(),
              s2max = new Value();
        s2min.setInteger(10);
        s2max.setInteger(0);
    	DeviceSpec s2 = new DeviceSpec("tableLight",
    			"Luz de la mesita",
    			"adjustableLight",
    			DeviceSpec.ACCESS_READ_WRITE,
    			Value.INTEGER_TYPE,
    			s2min,
    			s2max);

    	Value s3min = new Value(),
              s3max = new Value();
        s3min.setInteger(10);
        s3max.setInteger(0);
    	DeviceSpec s3 = new DeviceSpec("otro",
    			"Cacharro",
    			"other",
    			DeviceSpec.ACCESS_READ_WRITE,
    			Value.INTEGER_TYPE,
    			s3min,
    			s3max);
    	
    	specs.add(s1);
    	specs.add(s3);
    	specs.add(s2);
    	
    	// Asociar representaciones y crear dispositivos
    	
    	for(DeviceSpec s : specs){
    		DeviceRepresentation dr = sDeviceRepsMap.get(s.getDeviceType());
    		if(dr==null){ // si no tengo representación para este cacharrico
    			// Si no existe representación adecuada, coger el defaultScalar o el defaultBinary
    			if(s.getAccessType() == Value.BOOLEAN_TYPE){
    				dr = sDeviceRepsMap.get("defaultBinary");
    			} else {
    				dr = sDeviceRepsMap.get("defaultScalar");
    			}
    		}
    		
    		Device d = new Device(s.getReadableName(), s, dr);
    		sDevices.add(d);
    		sDevicesMap.put(d.getSystemName(), sDevices.size()-1);
    		
    	}
    }
    
    public static void disconnect()
    {
        Initializer.destroy();
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

	public static void setValueForDevice(String deviceName, Value value)
	{
		EventHandler.publish( new DeviceChangeEvent(deviceName, value) );
	}
}
