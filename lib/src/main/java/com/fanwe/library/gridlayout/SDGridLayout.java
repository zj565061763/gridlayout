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
    private int mHorizontalSpacing;
    private int mVerticalSpacing;

    public void setColumnCount(int columnCount)
    {
        if (mColumnCount != columnCount)
        {
            mColumnCount = columnCount;
        }
    }

    public void setHorizontalSpacing(int horizontalSpacing)
    {
        if (mHorizontalSpacing != horizontalSpacing)
        {
            mHorizontalSpacing = horizontalSpacing;
        }
    }

    public void setVerticalSpacing(int verticalSpacing)
    {
        if (mVerticalSpacing != verticalSpacing)
        {
            mVerticalSpacing = verticalSpacing;
        }
    }

    private int getColumnWidth()
    {
        int colWidth = (int) (((getMeasuredWidth() - ((mColumnCount - 1) * mVerticalSpacing)) / (float) mColumnCount) + 0.5f);
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
            final int colWidth = getColumnWidth();
            int col = 0;
            int left = 0;
            int top = 0;
            for (int i = 0; i < count; i++)
            {
                col = i % mColumnCount;
                if (col == 0)
                {
                    left = 0;
                }

                View child = getChildAt(i);

                final int right = left + colWidth;
                final int bottom = top + child.getMeasuredHeight();
                child.layout(left, top, right, bottom);

                left = right + mVerticalSpacing;
                if (col + 1 == mColumnCount)
                {
                    top = bottom + mHorizontalSpacing;
                }
            }
        }
    }
}
