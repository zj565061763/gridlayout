package com.fanwe.library.gridlayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2017/7/17.
 */

public class SDGridLayout extends ViewGroup
{
    public SDGridLayout(Context context)
    {
        super(context);
    }

    public SDGridLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public SDGridLayout(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    private int mColumnCount = 1;

    public void setColumnCount(int columnCount)
    {
        if (columnCount < 1)
        {
            columnCount = 1;
        }
        if (mColumnCount != columnCount)
        {
            mColumnCount = columnCount;
        }
    }

    private int getColumnWidth()
    {
        int colWidth = (int) (((float) getMeasuredWidth() / mColumnCount) + 0.5f);
        return colWidth;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        measureChildren(MeasureSpec.makeMeasureSpec(getColumnWidth(), MeasureSpec.EXACTLY), heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b)
    {
        final int count = getChildCount();
        if (count > 0)
        {
            final int colWidth = (int) (((float) getWidth() / mColumnCount) + 0.5f);
            int col = 0;
            int row = 0;
            for (int i = 0; i < count; i++)
            {
                col = i % mColumnCount;
                row = i / mColumnCount;

                View child = getChildAt(i);

                final int left = col * colWidth;
                final int top = row * child.getMeasuredHeight();
                final int right = left + colWidth;
                final int bottom = top + child.getMeasuredHeight();
                child.layout(left, top, right, bottom);
            }
        }
    }
}
