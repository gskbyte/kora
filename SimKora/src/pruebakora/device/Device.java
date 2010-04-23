/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pruebakora.device;

/**
 *
 * @author jose
 */
public abstract class Device {
    public static final int TYPE_BOOL = 0,
                            TYPE_INTEGER = 1;

    public String systemName;
    public String name;
    protected int rangeMin, rangeMax;
    protected int value;
}
