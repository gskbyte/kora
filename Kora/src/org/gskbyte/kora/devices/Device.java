package org.gskbyte.kora.devices;

import java.util.Vector;

import org.ugr.bluerose.events.Value;

public class Device extends DeviceSpec
{
	public interface DeviceEventListener
	{
		public void onDeviceChange(String deviceName, Value newVal);
		public void unregister();
	}
	
	protected DeviceRepresentation mRepr;
	protected Vector<DeviceEventListener> mListeners = new Vector<DeviceEventListener>();
	
	public Device(DeviceSpec s, DeviceRepresentation repr)
	{
	    super(s);
	    mRepr = repr;
	}
	
	void setValue(Value newValue)
	{
		mCurrentValue = newValue;
		for(DeviceEventListener dl : mListeners)
            dl.onDeviceChange(mSystemName, newValue);
	}
	
	public DeviceRepresentation getRepresentation()
	{
	    return mRepr;
	}
	
	public void addChangeListener(DeviceEventListener listener)
	{
		mListeners.add(listener);
	}
	
	public void removeListener(DeviceEventListener listener)
	{
		mListeners.remove(listener);
	}
	
	public int getState()
    {
	    return value2State(this, mCurrentValue);
    }
	
	public static int value2State(Device d, Value v)
	{
	    int nstates = d.mRepr.getStateCount();
        int ret = 0;
        switch(v.type){
        case Value.BOOLEAN_TYPE:
            ret = (v.getBoolean()) ? nstates-1 : 0;
            break;
        case Value.INTEGER_TYPE:
            int max = d.mMaxValue.getInteger(),
                min = d.mMinValue.getInteger();
            
            int stepSize = (max - min)/(nstates-1);
            ret = v.getInteger()/stepSize;
            break;
        case Value.FLOAT_TYPE:
            float maxf = d.mMaxValue.getFloat(),
                  minf = d.mMinValue.getFloat();
            float nstatesf = nstates-1;
            float statef = v.getFloat();
            float stepSizef = (maxf - minf)/nstatesf;
            ret = (int)(statef/stepSizef);
            break;
            /// IMPLEMENTAR RESTO DE CASOS!!!
        }
        return ret;
	}
	
	public static Value state2Value(Device d, int state)
	{
	    Value ret = new Value();
        int nstates = d.mRepr.getStateCount();
        switch(d.mValueType){
        case Value.BOOLEAN_TYPE:
            ret.setBoolean(state == 0 ? false : true);
            break;
        case Value.INTEGER_TYPE:
            int max = d.mMaxValue.getInteger(),
                min = d.mMinValue.getInteger();
            
            ret.setInteger(((max-min)/(nstates-1))*state);
            break;
        case Value.FLOAT_TYPE:
            float maxf = d.mMaxValue.getFloat(),
                  minf = d.mMinValue.getFloat();
            float nstatesf = nstates-1;
            float statef = state;
            
            ret.setFloat(((maxf-minf)/nstatesf)*statef);
            break;
            /// RESTO DE CASOS
        }
       
        return ret; 
	}
}
