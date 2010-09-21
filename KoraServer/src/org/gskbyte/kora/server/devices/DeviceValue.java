package org.gskbyte.kora.server.devices;

import org.ugr.bluerose.events.Value;

public class DeviceValue
{
    private Value mMin = new Value(), 
                  mMax = new Value();
    private Value mCurrent = new Value();
    
    public DeviceValue(boolean current)
    {
        mMin.setBoolean(false);
        mMax.setBoolean(true);
        mCurrent.setBoolean(current);
    }
    
    public DeviceValue(int min, int max, int current)
    {
        mMin.setInteger(min);
        mMax.setInteger(max);
        mCurrent.setInteger(current);
    }
    
    public DeviceValue(float min, float max, float current)
    {
        mMin.setFloat(min);
        mMax.setFloat(max);
        mCurrent.setFloat(current);
    }
    
    public DeviceValue(Value min, Value max, Value current)
    {
        mMin = min;
        mMax = max;
        mCurrent = current;
    }

    public int getType()
    {
        return mMin.type;
    }
    
    public Value getMin()
    {
        return mMin;
    }
    
    public Value getMax()
    {
        return mMax;
    }
    
    public Value getCurrent()
    {
        return mCurrent;
    }
    
    public Object getMinObj()
    {
        switch(mMin.type)
        {
        case Value.BOOLEAN_TYPE:
            return mMin.getBoolean();
        case Value.INTEGER_TYPE:
            return mMin.getInteger();
        case Value.FLOAT_TYPE:
            return mMin.getFloat();
        }
        return null;
    }
    
    public Object getMaxObj()
    {
        switch(mMax.type)
        {
        case Value.BOOLEAN_TYPE:
            return mMax.getBoolean();
        case Value.INTEGER_TYPE:
            return mMax.getInteger();
        case Value.FLOAT_TYPE:
            return mMax.getFloat();
        }
        return null;
    }
    
    public Object getCurrentObj()
    {
        switch(mCurrent.type)
        {
        case Value.BOOLEAN_TYPE:
            return mCurrent.getBoolean();
        case Value.INTEGER_TYPE:
            return mCurrent.getInteger();
        case Value.FLOAT_TYPE:
            return mCurrent.getFloat();
        }
        return null;
    }
    
    public boolean setCurrent(Value v)
    {
        if(mCurrent.type == v.type) {
            mCurrent = v;
            return true;
        } else {
            return false;
        }
    }
    
    public boolean setCurrent(boolean v)
    {
        if(mCurrent.type == Value.BOOLEAN_TYPE) {
            mCurrent.setBoolean(v);
            return true;
        } else {
            return false;
        }
    }
    
    public boolean setCurrent(int v)
    {
        if(mCurrent.type == Value.INTEGER_TYPE) {
            mCurrent.setInteger(v);
            return true;
        } else {
            return false;
        }
    }
    
    public boolean setCurrent(float v)
    {
        if(mCurrent.type == Value.FLOAT_TYPE) {
            mCurrent.setFloat(v);
            return true;
        } else {
            return false;
        }
    }
}
