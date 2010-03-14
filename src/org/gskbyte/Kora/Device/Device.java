package org.gskbyte.Kora.Device;

import android.graphics.drawable.Drawable;

public class Device
{
	public static final int ICON_NORMAL = 1,
			   				ICON_HIGH_CONTRAST = 2,
			   				ICON_ANIMATION = 3,
			   				ICON_PHOTO = 4;
	
	private int id;
	private String mTagName;
	private Drawable mNormalIcon, mHighContrastIcon, mAnimationIcon, mPhotoIcon;
	
	/// COMPLETAR ESTE CONSTRUCTOR (provisional)
	public Device(String name, Drawable icon)
	{
		mTagName = name;
		mNormalIcon = icon;
	}
	
	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}
	
	public String getName()
	{
		return mTagName;
	}
	
	public void setName(String name)
	{
		this.mTagName = name;
	}
	
	public Drawable getIcon(int which)
	{
		switch(which)
		{
		case ICON_NORMAL:
			return mNormalIcon;
		case ICON_HIGH_CONTRAST:
			return mHighContrastIcon;
		case ICON_ANIMATION:
			return mAnimationIcon;
		case ICON_PHOTO:
			return mPhotoIcon;
		default:
				return null;
		}
	}
	
	public void setIcon(int which, Drawable icon)
	{
		switch(which)
		{
		case ICON_NORMAL:
			mNormalIcon = icon;
		case ICON_HIGH_CONTRAST:
			mHighContrastIcon = icon;
		case ICON_ANIMATION:
			mAnimationIcon = icon;
		case ICON_PHOTO:
			mPhotoIcon = icon;
		}
	}
}
