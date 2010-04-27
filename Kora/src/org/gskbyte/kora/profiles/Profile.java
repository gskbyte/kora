package org.gskbyte.kora.profiles;

import java.io.Serializable;

import android.os.Parcelable;

public abstract class Profile implements Comparable<Profile>, Serializable
{
    public String name;
    public boolean isDefault = false;
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name;;
    }
    
    public boolean isDefault()
    {
        return isDefault;
    }
    
    public boolean isCustom()
    {
        return !isDefault;
    }
    
    public int compareTo(Profile another)
    {
        if(another == null)
            return 1;
        if(another.getClass() != this.getClass())
            return 1;
        
        return name.compareTo(((Profile)another).name);
    }
}
