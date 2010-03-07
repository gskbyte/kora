package org.gskbyte.Kora.Settings;

import android.graphics.drawable.Drawable;

public class Device
{
	static final int ICON_NORMAL = 1,
			   ICON_HIGH_CONTRAST = 2,
			   ICON_ANIMATION = 3,
			   ICON_PHOTO = 4;
	
	private int id;
	private String name, translatableName;
	private Drawable normalIcon, highContrastIcon, animationIcon, photoIcon;
	
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
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getTranslatableName()
	{
		return translatableName;
	}

	public void setTranslatableName(String translatableName)
	{
		this.translatableName = translatableName;
	}
	
	private Drawable getIcon(int which)
	{
		switch(which)
		{
		case ICON_NORMAL:
			return normalIcon;
		case ICON_HIGH_CONTRAST:
			return highContrastIcon;
		case ICON_ANIMATION:
			return animationIcon;
		case ICON_PHOTO:
			return photoIcon;
		default:
				return null;
		}
	}
	
	private void setIcon(int which, Drawable icon)
	{
		switch(which)
		{
		case ICON_NORMAL:
			normalIcon = icon;
		case ICON_HIGH_CONTRAST:
			highContrastIcon = icon;
		case ICON_ANIMATION:
			animationIcon = icon;
		case ICON_PHOTO:
			photoIcon = icon;
		}
	}
}
