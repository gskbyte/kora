package org.gskbyte.kora.customViews.detailedListView;

import org.gskbyte.kora.R;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DetailedListViewItem extends RelativeLayout
{
    
    public DetailedListViewItem(Context context, String mainText, String description, Drawable image)
    {
        super(context);
        
        LayoutInflater.from(context).inflate(R.layout.detailed_list_view, this, true);
        
        mMainText = (TextView) findViewById(R.id.mainText);
        mDescription = (TextView) findViewById(R.id.description);
        mImage = (ImageView) findViewById(R.id.image);
        
        mMainText.setText(mainText);
        if(description.length()>0)
            mDescription.setText(description);
        else
            mDescription.setVisibility(GONE);
        if(image!=null)
            mImage.setImageDrawable(image);
        else
            mImage.setVisibility(GONE);
    }
    
    public DetailedListViewItem(Context context, String mainText, String description, int drawable_id)
    {
        super(context);
        
        LayoutInflater.from(context).inflate(R.layout.detailed_list_view, this, true);
        
        mMainText = (TextView) findViewById(R.id.mainText);
        mDescription = (TextView) findViewById(R.id.description);
        mImage = (ImageView) findViewById(R.id.image);
        
        mMainText.setText(mainText);
        if(description.length()>0)
            mDescription.setText(description);
        else
            mDescription.setVisibility(GONE);
        
        if(drawable_id!=0)
            mImage = (ImageView) findViewById(drawable_id);
        else
            mImage.setVisibility(GONE);
    }
    
    public void setMainText(String mainText)
    {
        mMainText.setText(mainText);
    }

    public void setDescription(String description)
    {
        if(description.length()>0)
            mDescription.setText(description);
        else
            mDescription.setVisibility(GONE);
    }
    
    public void setImage(Drawable image)
    {
        if(image!=null)
            mImage.setImageDrawable(image);
        else
            mImage.setVisibility(GONE);
    }
    
    public void setImage(int drawable_id)
    {
        if(drawable_id!=0)
            mImage = (ImageView) findViewById(drawable_id);
        else
            mImage.setVisibility(GONE);
    }
    
    private TextView mMainText, mDescription;
    private ImageView mImage;
}
