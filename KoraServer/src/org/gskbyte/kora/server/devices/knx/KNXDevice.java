package org.gskbyte.kora.server.devices.knx;

import java.util.ArrayList;
import java.util.HashMap;

import org.gskbyte.kora.server.devices.Device;
import org.gskbyte.kora.server.devices.DeviceValue;
import org.ugr.bluerose.events.Value;

import tuwien.auto.calimero.GroupAddress;
import tuwien.auto.calimero.datapoint.Datapoint;
import tuwien.auto.calimero.datapoint.StateDP;
import tuwien.auto.calimero.exception.KNXFormatException;
import tuwien.auto.calimero.log.LogStreamWriter;
import tuwien.auto.calimero.tools.ProcComm;

public abstract class KNXDevice extends Device
{
    protected static int sCounter = 0;
    static ProcComm comm;
    // Mapa que mantiene direcciones asociadas a nombres de dispositivos.
    // Necesario para control de eventos. Ver clase KNXListener
    // Clave = dirección KNX, Valor = nombre de dispositivo en Kora
    static HashMap<String, String> addrMap = new HashMap<String, String>();
    
    public static void init(String localhostIp, String serverIp, String port) throws Exception
    {
        final ArrayList<String> args = new ArrayList<String>();
        
        args.add("-localhost");
        args.add(localhostIp);
        args.add(serverIp);
        args.add("-p");
        args.add(port);
        
        comm = new ProcComm(args.toArray(new String[args.size()]), new LogStreamWriter(System.out));
        comm.run(new KNXListener());
    }
    
    public static void close()
    {
        comm.quit();
    }

    protected final GroupAddress mAddress;
    
    protected KNXDevice(String systemName, String readableName,
            String deviceType, int accessMode, String address) throws KNXFormatException
    {
        super(systemName, readableName, deviceType, accessMode);
        mAddress = new GroupAddress(address);
        addrMap.put(address, systemName);
        
        ++sCounter;
    }
    
    public KNXDevice(String systemName, String readableName,
            String deviceType, int accessMode, String address, Value min, Value max) throws KNXFormatException
    {
        super(systemName, readableName, deviceType, accessMode);
        mAddress = new GroupAddress(address);
        addrMap.put(address, systemName);
        
        readValue(min, max);
        ++sCounter;
    }
    
    @Override
    public Object getValueObj()
    {
        final int am = getAccessMode();
        if(am == ACCESS_READ || am == ACCESS_READ_WRITE)
            return value.getCurrentObj();
        else
            return null;
    }

    @Override
    public Value getValue()
    {
        final int am = getAccessMode();
        if(am == ACCESS_READ || am == ACCESS_READ_WRITE)
            return value.getCurrent();
        else
            return null;
    }

    @Override
    public Value setValue(Value v) throws Exception
    {
        final int am = getAccessMode();
        if(am == ACCESS_READ || am == ACCESS_READ_WRITE){
            switch(v.type)
            {
            case Value.BOOLEAN_TYPE:
                final Datapoint dpb = new StateDP(mAddress, "", 0, "1.002");
                comm.write(dpb, v.getBoolean() ? "true" : "false");
                value.setCurrent(v);
                break;
            case Value.INTEGER_TYPE:
                final Datapoint dpi = new StateDP(mAddress, "", 0, "5.001");
                comm.write(dpi, v.getInteger()+"");
                value.setCurrent(v);
                break;
            case Value.FLOAT_TYPE:
                /** FLOAT! **/
                break;
            }
            return null;
            //return value.getCurrent();
        } else { 
            return null;
        }
    }
    
    abstract void readValue() throws Throwable;
    
    private void readValue(Value min, Value max)
    {
        switch(max.type)
        {
        case Value.BOOLEAN_TYPE:
            boolean b = true;
            System.out.println("Boolean initial value of "+getSystemName()+": "+b);
            value = new DeviceValue(b);
            break;

        case Value.INTEGER_TYPE:
            break;
            
        case Value.FLOAT_TYPE:
            break;
        }
    }
    
}
