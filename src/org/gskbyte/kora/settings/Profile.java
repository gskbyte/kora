package org.gskbyte.kora.settings;

public abstract class Profile<Id> implements Comparable
{
    Id id;
    protected boolean isDefault;
    
    public Id getId()
    {
        return id;
    }
    
    public boolean isDefault()
    {
        return isDefault;
    }
    
    public boolean isCustom()
    {
        return !isDefault;
    }
}
