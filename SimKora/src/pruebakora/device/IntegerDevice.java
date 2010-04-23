/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pruebakora.device;

/**
 *
 * @author jose
 */
public class IntegerDevice extends Device
{
    public IntegerDevice(int min, int max, int initialValue)
    {
        rangeMin = 0;
        rangeMax = 1;
        value = initialValue;
    }
    public int getValue()
    {
        return value;
    }

    public void setValue(int v)
    {
        value = v;
    }
}
