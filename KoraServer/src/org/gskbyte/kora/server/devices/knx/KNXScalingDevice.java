package org.gskbyte.kora.server.devices.knx;

import org.gskbyte.kora.server.devices.DeviceValue;

import tuwien.auto.calimero.datapoint.StateDP;

/*
 * Clase que soporta dispositivos KNX con valores de 0 a 100.
 * Lo suyo sería tener una clase KNXIntegerDevice, pero no he tenido tiempo.
 * Mirar los tipos enteros en el paquete tuwien.auto.calimero.dptxlator para construirla.
 * Este código dtpID (5.001) está sacado de DPTXlator8BitUnsigned.
 * Suerte al siguiente ;).
 * */
public class KNXScalingDevice extends KNXDevice
{
    // Ver constructor de StateDP en biblioteca Calimero.
    // El dptID identifica el tipo de dato que usa calimero para los dispositivos.
    // 5.001 es DPT_SCALING, de 0 a 100. Ver clase DPTXlator8BitUnsigned
    public static final String dptID = "5.001";
    private final StateDP mDp = new StateDP(mAddress, "", 0, dptID);

    public KNXScalingDevice(String systemName, String readableName,
            String deviceType, int accessMode, String address)
            throws Exception
    {
        super(systemName, readableName, deviceType, accessMode, address);
        readValue();
    }
    
    void readValue() throws Exception 
    {
        String s = comm.read(mDp);
        s = s.split(" ")[0];
        int i = Integer.parseInt(s);
        value = new DeviceValue(0, 100, i);
    }
    
    public int get()
    {
        return value.getCurrent().getInteger();
    }
    
    public void set(int i) throws Exception
    {
        comm.write(mDp, i+"");
        value.setCurrent(i);
    }

}
