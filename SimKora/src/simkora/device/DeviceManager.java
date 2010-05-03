/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package simkora.device;

import java.util.HashMap;
import java.util.Set;
import java.util.Vector;
import org.ugr.bluerose.events.Value;

/**
 *
 * @author jose
 */
public class DeviceManager
{
    private static HashMap<String, Integer> deviceIndex = new HashMap<String, Integer>();
    private static Vector<DeviceSpec> devices = new Vector<DeviceSpec>();

    public static void init()
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


        devices.add(s1);
        deviceIndex.put(s1.getSystemName(), 0);
        devices.add(s2);
        deviceIndex.put(s2.getSystemName(), 1);
        devices.add(s3);
        deviceIndex.put(s3.getSystemName(), 2);
        devices.add(s4);
        deviceIndex.put(s4.getSystemName(), 3);
        devices.add(s5);
        deviceIndex.put(s5.getSystemName(), 4);
    }

    public static Vector<DeviceSpec> getAllDevices()
    {
        return devices;
    }

    public static Set<String> getDeviceNames()
    {
        return deviceIndex.keySet();
    }

    public static DeviceSpec getDevice(String name)
    {
        int index = deviceIndex.get(name);
        return devices.get(index);
    }
}
