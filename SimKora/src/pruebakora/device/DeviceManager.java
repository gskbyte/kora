/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pruebakora.device;

import java.util.HashMap;
import java.util.Set;

/**
 *
 * @author jose
 */
public class DeviceManager
{
    private static HashMap<String, Device> devices;

    public static void init()
    {

    }

    public static Set<String> getDeviceNames()
    {
        return devices.keySet();
    }

    public static Device getDevice(String name)
    {
        return devices.get(name);
    }
}
