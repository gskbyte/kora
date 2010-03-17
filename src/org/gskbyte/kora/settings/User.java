package org.gskbyte.kora.settings;

import android.graphics.drawable.Drawable;

public class User extends Profile<String>
{
    private static Drawable defaultPhoto = null;
    
    private String school = "";
    private Drawable photo = null;
    private String photoPath = "";
    private boolean autoStart = true;
    private int autoStartTimeSeconds = 10;
    
    String useProfileName;
    String deviceProfileName;
    
    public User(String name, boolean isDefault, 
                String school, String photoPath,
                boolean autoStart, int autoStartTimeSeconds,
                String useProfileName, String deviceProfileName)
    {
        this.id = name;
        this.isDefault = isDefault;
        
        this.school = school;
        if(photoPath == null || photoPath.length()==0){
            this.photoPath = "";
            this.photo = defaultPhoto;
        }
        else
            ;//this.photo = photo;
        this.autoStart = autoStart;
        this.autoStartTimeSeconds = autoStartTimeSeconds;
        this.useProfileName = useProfileName;
        this.deviceProfileName = deviceProfileName;
    }
    
    public static void setDefaultPhoto(Drawable photo)
    {
        defaultPhoto = photo;
    }
    
    public String getName()
    {
        return this.id;
    }

    public void setName(String name)
    {
        this.id = name;
    }

    public Drawable getPhoto()
    {
        return photo;
    }

    public void setPhoto(Drawable photo)
    {
        this.photo = photo;
    }

    public String getPhotoPath()
    {
        return this.photoPath;
    }
    
    public void setPhoto(String path)
    {
        photoPath = path;
        // CARGAR FOTO
    }
    
    public String getSchool()
    {
        return school;
    }

    public void setSchool(String school)
    {
        this.school = school;
    }

    public boolean wantsAutoStart()
    {
        return autoStart;
    }

    public void setAutoStart(boolean autoStart)
    {
        this.autoStart = autoStart;
    }

    public int getAutoStartSeconds()
    {
        return autoStartTimeSeconds;
    }

    public void setAutoStartTimeSeconds(int autoStartTimeSeconds)
    {
        this.autoStartTimeSeconds = autoStartTimeSeconds;
    }

    public String getUseProfileName()
    {
        return useProfileName;
    }

    public void setUseProfileName(String useProfileName)
    {
        this.useProfileName = useProfileName;
    }
    
    public String getDeviceProfileName()
    {
        return deviceProfileName;
    }

    public void setDeviceProfileName(String deviceProfileName)
    {
        this.deviceProfileName = deviceProfileName;
    }

    @Override
    public int compareTo(Object another)
    {
        if(another == null)
            return 1;
        if(another.getClass() != User.class)
            return 1;
        
        return id.compareTo(((User)another).id);
    }
}
