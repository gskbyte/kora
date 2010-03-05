package org.gskbyte.Kora.customViews.DetailedListView;

import android.graphics.drawable.Drawable;

public class DetailedViewModel
{

public DetailedViewModel(String tag)
{
    mTag = tag;
}

public DetailedViewModel(String tag, String mainText, String description, 
        Drawable image)
{
    mTag = tag;
    mMainText = mainText;
    mDescription = description;
    mImage = image;
}

public String tag()
{
    return mTag;
}

public String mainText()
{
    return mMainText;
}

public void setMainText(String mMainText)
{
    this.mMainText = mMainText;
}

public String description()
{
    return mDescription;
}

public void setDescription(String mDescription)
{
    this.mDescription = mDescription;
}

public Drawable image()
{
    return mImage;
}

public void setImage(Drawable image)
{
    mImage = image;
}

protected String mTag = "";
protected String mMainText = "";
protected String mDescription = "";
protected Drawable mImage = null;

}