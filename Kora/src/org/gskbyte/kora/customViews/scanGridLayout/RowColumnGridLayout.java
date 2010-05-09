package org.gskbyte.kora.customViews.scanGridLayout;

import android.content.Context;
import android.view.MotionEvent;

public class RowColumnGridLayout extends ScanGridLayout
{

    public RowColumnGridLayout(Context context)
    {
        super(context);
    }

    @Override
    public void focusNext()
    {
        
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        return false;
    }

}
