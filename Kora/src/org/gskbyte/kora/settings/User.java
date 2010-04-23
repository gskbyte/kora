package org.gskbyte.kora.settings;

import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class User extends Profile implements Serializable
{
    transient private static final long serialVersionUID = -486208937377205579L;
    transient private static Drawable sDefaultPhoto = null;
    transient private static Context sContext = null;
    
    private String mSchool = "";
    transient private Drawable mPhoto = null;
    private String mPhotoPath = "";
    private boolean mAutoStart = true;
    private int mAutoStartTimeSeconds = 10;
    
    private String mUseProfileName;
    private String mDeviceProfileName;
    
    public User(String name, boolean isDefault, 
                String school, String photoPath,
                boolean autoStart, int autoStartTimeSeconds,
                String useProfileName, String deviceProfileName)
    {
        this.name = name;
        this.isDefault = isDefault;
        
        this.mSchool = school;
        this.mPhotoPath = photoPath;
        this.mAutoStart = autoStart;
        this.mAutoStartTimeSeconds = autoStartTimeSeconds;
        this.mUseProfileName = useProfileName;
        this.mDeviceProfileName = deviceProfileName;
    }
    
    public static Drawable getDefaultPhoto()
    {
        return sDefaultPhoto;
    }
    
    public static void setDefaultPhoto(Drawable photo, Context ctx)
    {
        sDefaultPhoto = photo;
        sContext = ctx;
    }

    public Drawable getPhoto()
    {
        if(mPhoto==null){
            if(mPhotoPath == null || mPhotoPath.length()==0){
                return sDefaultPhoto;
            } else {
                try {
                    FileInputStream fIn = sContext.openFileInput(mPhotoPath);
                    Bitmap b = BitmapFactory.decodeStream(fIn);
                    mPhoto = new BitmapDrawable(b);
                    fIn.close();
                } catch (Exception e) {
                    mPhotoPath = "";
                    mPhoto = sDefaultPhoto;
                }
            }
        }
        
        return mPhoto;
    }

    public String getPhotoPath()
    {
        return this.mPhotoPath;
    }
    
    public void setPhoto(String path)
    {
        mPhotoPath = path;
    }
    
    public String getSchool()
    {
        return mSchool;
    }

    public void setSchool(String school)
    {
        this.mSchool = school;
    }

    public boolean wantsAutoStart()
    {
        return mAutoStart;
    }

    public void setAutoStart(boolean autoStart)
    {
        this.mAutoStart = autoStart;
    }

    public int getAutoStartSeconds()
    {
        return mAutoStartTimeSeconds;
    }

    public void setAutoStartTimeSeconds(int autoStartTimeSeconds)
    {
        this.mAutoStartTimeSeconds = autoStartTimeSeconds;
    }

    public String getUseProfileName()
    {
        return mUseProfileName;
    }

    public void setUseProfileName(String useProfileName)
    {
        this.mUseProfileName = useProfileName;
    }
    
    public String getDeviceProfileName()
    {
        return mDeviceProfileName;
    }

    public void setDeviceProfileName(String deviceProfileName)
    {
        this.mDeviceProfileName = deviceProfileName;
    }
}
