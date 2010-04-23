/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pruebakora.device;

/**
 *
 * @author jose
 */
public class BooleanDevice extends Device
{
    public void setBoolean(boolean initialValue)
    {
        rangeMin = 0;
        rangeMax = 1;
        value = initialValue ? 1 : 0;
    }

    public boolean getValue()
    {
        return value==0 ? false : true;
    }

    public void setValue(boolean v)
    {
        value = v ? 1 : 0;
    }
}
