/*
 * Copyright (C) 2008 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gskbyte.kora.customViews.gridLayout;

import org.gskbyte.kora.R;

import android.view.ViewGroup;
import android.view.View;
import android.view.animation.GridLayoutAnimationController;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

/**
 * A GridLayout positions its children in a static grid, defined by a fixed number of rows
 * and columns. The size of the rows and columns is dynamically computed depending on the
 * size of the GridLayout itself. As a result, GridLayout children's layout parameters
 * are ignored.
 *
 * The number of rows and columns are specified in XML using the attributes android:numRows
 * and android:numColumns.
 *
 * The GridLayout cannot be used when its size is unspecified.
 *
 * @attr ref com.google.android.photostream.R.styleable#GridLayout_numColumns
 * @attr ref com.google.android.photostream.R.styleable#GridLayout_numRows  
 */
public class GridLayout extends ViewGroup
{
    private int mNumColumns;
    private int mNumRows;

    private int mColumnWidth;
    private int mRowHeight;
    
    private int mMargin;

    public GridLayout(Context context)
    {
        this(context, null);
    }

    public GridLayout(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public GridLayout(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.GridLayout, defStyle, 0);

        mNumColumns = a.getInt(R.styleable.GridLayout_numColumns, 1);
        mNumRows = a.getInt(R.styleable.GridLayout_numRows, 1);

        mMargin = 10;
        
        a.recycle();
    }
    
    public int getNColumns()
    {
    	return mNumColumns;
    }
    
    public int getNRows()
    {
        return mNumRows;
    }
    
    public void setDimensions(int nrows, int ncolumns)
    {
        if(nrows > 0 && ncolumns>0){
            mNumRows = nrows;
            mNumColumns = ncolumns;
            requestLayout();
            invalidate();
        } // TODO TIRAR EXCEPCIÓN
    }
    
    public int getMargin()
    {
    	return mMargin;
    }
    
    public void setMargin(int margin)
    {
    	mMargin = margin;
        requestLayout();
        invalidate();
    }

    @Override
    protected void attachLayoutAnimationParameters(View child,
            ViewGroup.LayoutParams params, int index, int count)
    {

        GridLayoutAnimationController.AnimationParameters animationParams =
                (GridLayoutAnimationController.AnimationParameters)
                        params.layoutAnimationParameters;

        if (animationParams == null) {
            animationParams = new GridLayoutAnimationController.AnimationParameters();
            params.layoutAnimationParameters = animationParams;
        }

        animationParams.count = count;
        animationParams.index = index;
        animationParams.columnsCount = mNumColumns;
        animationParams.rowsCount = mNumRows;

        animationParams.column = index % mNumColumns;
        animationParams.row = index / mNumColumns;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        final int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec),
                  widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);

        final int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec),
                  heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthSpecMode == MeasureSpec.UNSPECIFIED || heightSpecMode == MeasureSpec.UNSPECIFIED) {
            throw new RuntimeException("GridLayout can't have UNSPECIFIED dimensions");
        }

        // Al tamaño total destinado a filas y columnas, hay que restarle el de los márgenes
        final int sumColumnsWidth = widthSpecSize - mMargin*(mNumColumns+1),
        		  sumRowsHeight = heightSpecSize - mMargin*(mNumRows+1);
        
        mColumnWidth = sumColumnsWidth / mNumColumns;
        mRowHeight = sumRowsHeight / mNumRows;

        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).measure(MeasureSpec.makeMeasureSpec(mColumnWidth, MeasureSpec.EXACTLY),
            			  		  MeasureSpec.makeMeasureSpec(mRowHeight, MeasureSpec.EXACTLY));
        }

        setMeasuredDimension(widthSpecSize, heightSpecSize);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b)
    {
        final int paddingLeft = getPaddingLeft();
        final int paddingTop = getPaddingTop();

        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                final int column = i % mNumColumns;
                final int row = i / mNumColumns;

                int childLeft = paddingLeft + column * mColumnWidth + (column+1)*mMargin;
                int childTop = paddingTop + row * mRowHeight + (row+1)*mMargin;

                child.layout(childLeft, childTop, 
		                     childLeft+child.getMeasuredWidth(),
		                     childTop+child.getMeasuredHeight());
            }
        }
        
    }
}


