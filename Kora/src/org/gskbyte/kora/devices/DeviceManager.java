package org.gskbyte.kora.devices;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Vector;

import org.gskbyte.kora.R;
import org.ugr.bluerose.Initializer;
import org.ugr.bluerose.devices.TcpCompatibleDevice;
import org.ugr.bluerose.events.Event;
import org.ugr.bluerose.events.EventHandler;
import org.ugr.bluerose.events.Value;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;

public class DeviceManager
{
    public static final String TAG = "DeviceManager";
    
    private static final String DEVICE_REPS_FOLDER = "device_representations";
    
    protected static Context sContext;
    protected static boolean sSimulationMode;
    protected static String sAddress, sPort;
    protected static final String sFilename = "bluerose_config.xml";
    
    protected static Vector<Device> sDevices;
    protected static HashMap<String, Integer> sDevicesMap;
    protected static HashMap<String, DeviceRepresentation> sDeviceRepsMap;
        
    public static void init(Context ctx)
    {
        sContext = ctx;
        
        sDevices = new Vector<Device>();
        sDevicesMap = new HashMap<String, Integer>();
        sDeviceRepsMap = new HashMap<String, DeviceRepresentation>();
        
        /*
         * Cargar representaciones de dispositivos
         */
        Resources res = ctx.getResources();
        AssetManager am = ctx.getAssets();
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity)ctx).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        
        DeviceRepresentation.init(res, am);
        DeviceRepresentation.setMaxIconSize((metrics.widthPixels*2)/3, (metrics.heightPixels*2)/3);
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
    
    public static void loadPreferences()
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(sContext);
        boolean simulate = prefs.getBoolean("simulation", true);
        DeviceManager.setSimulationMode(simulate);
        String address = prefs.getString("ip", "192.168.2.105"),
               port =    prefs.getString("port", "10003");
        DeviceManager.setServerAddress(address, port);
    }
    
    public static void connect() throws Exception
    {
        sDevices.clear();
        sDevicesMap.clear();

        long startTime = System.currentTimeMillis(), current;
        
        Vector<DeviceSpec> specs = new Vector<DeviceSpec>();
        
        // Connect to BlueRose server
        if(sSimulationMode){
            specs = createFakeDevices();
        } else {
            Log.e(TAG, "Connecting to BlueRose");
            InputStream file = sContext.getResources().openRawResource(R.raw.bluerose_config);
            
            FileInputStream fIn = null;
            try {
                fIn = sContext.openFileInput(sFilename);
                //fIn.close();
            } catch (FileNotFoundException e) {
                try {
                    FileOutputStream fOut = sContext.openFileOutput(sFilename, Context.MODE_WORLD_READABLE);
                    byte[] bytes = new byte[80192];
                    int count = sContext.getResources().openRawResource(R.raw.bluerose_config).read(bytes);
                    fOut.write(bytes, 0, count);
                    fOut.close();
                    /* Esto se quita cuando BlueRose se arregle */
                    fIn = sContext.openFileInput(sFilename);
                } catch (Exception e1) {
                    Log.e(TAG, e1.getStackTrace()[0].toString());
                }
            }
            //org.ugr.bluerose.Initializer.initialize(new java.io.File(sFilename));
            org.ugr.bluerose.Initializer.initialize(fIn);
            //org.ugr.bluerose.Initializer.initializeNonWaitingClient(device);
            TcpCompatibleDevice device = new TcpCompatibleDevice();
            org.ugr.bluerose.Initializer.initializeClient(device);
            file.close();
            
            current = System.currentTimeMillis();
            
            DeviceListProxy dlp = new DeviceListProxy();
            specs = dlp.getDeviceSpecs();
            
            current = System.currentTimeMillis();
            Log.e(TAG, "Success. Connection time: " + (current-startTime)/1000.0);
        }
        
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
    		
    		Device d = new Device(s, dr);
    		sDevices.add(d);
    		sDevicesMap.put(d.getSystemName(), sDevices.size()-1);
    	}
    	
        /*
         * Listeners, se inician una vez tengo cargadas todas las representaciones
         */
    	if(!sSimulationMode){
            org.ugr.bluerose.events.EventHandler.addEventListener(new DeviceQueryListener());
            org.ugr.bluerose.events.EventHandler.addEventListener(new DeviceChangeListener());
    	}
    }
    
    public static void setSimulationMode(boolean simulate)
    {
        sSimulationMode = simulate;
    }
    
    public static void setServerAddress(String address, String port)
    {
        sAddress = address;
        sPort = port;
    }
    
    public static String getServerAddress()
    {
        return sAddress;
    }
    
    public static String getServerPort()
    {
        return sPort;
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
		Device dev = getDevice(deviceName);
		dev.setValue(value);
		if(!sSimulationMode){
            Event evt = new DeviceChangeEvent(deviceName, value);
    		EventHandler.publish(evt, false);
		}
	}
	
	protected static void setBlueRoseConfiguration()
	{
	    
	}
	
	protected static Vector<DeviceSpec> createFakeDevices()
	{
	    Value s1min = new Value(),
              s1max = new Value(),
              s1cur = new Value();
        s1min.setBoolean(false);
        s1max.setBoolean(true);
        s1cur.setBoolean(false);
        DeviceSpec s1 = new DeviceSpec("bombilla1",
                "Luz grande",
                "simpleLight",
                DeviceSpec.ACCESS_READ_WRITE,
                Value.BOOLEAN_TYPE,
                s1min,
                s1max,
                s1cur);
    
        Value s2min = new Value(),
              s2max = new Value(),
              s2cur = new Value();
        s2min.setFloat(0);
        s2max.setFloat(1);
        s2cur.setFloat(0);
        DeviceSpec s2 = new DeviceSpec("bombilla2",
              "Flexo",
              "adjustableLight",
              DeviceSpec.ACCESS_READ_WRITE,
              Value.FLOAT_TYPE,
              s2min,
              s2max,
              s2cur);
    
        Value s3min = new Value(),
              s3max = new Value(),
              s3cur = new Value();
        s3min.setFloat(0);
        s3max.setFloat(1);
        s3cur.setFloat(0);
        DeviceSpec s3 = new DeviceSpec("persiana1",
              "Persiana",
              "sunblind",
              DeviceSpec.ACCESS_READ_WRITE,
              Value.FLOAT_TYPE,
              s3min,
              s3max,
              s3cur);
    
        Value s4min = new Value(),
              s4max = new Value(),
              s4cur = new Value();
        s4min.setBoolean(false);
        s4max.setBoolean(true);
        s4cur.setBoolean(false);
        DeviceSpec s4 = new DeviceSpec("puerta1",
              "Puerta",
              "door",
              DeviceSpec.ACCESS_READ_WRITE,
              Value.BOOLEAN_TYPE,
              s4min,
              s4max,
              s4cur);
    
        Value s5min = new Value(),
              s5max = new Value(),
              s5cur = new Value();
        s5min.setBoolean(false);
        s5max.setBoolean(true);
        s5cur.setBoolean(false);
        DeviceSpec s5 = new DeviceSpec("puerta2",
              "Puerta 2",
              "door",
              DeviceSpec.ACCESS_READ_WRITE,
              Value.BOOLEAN_TYPE,
              s5min,
              s5max,
              s5cur);
        
        Vector<DeviceSpec> specs = new Vector<DeviceSpec>();
        specs.add(s1);
        specs.add(s2);
        specs.add(s3);
        specs.add(s4);
        specs.add(s5);
        
        return specs;
	}
}
