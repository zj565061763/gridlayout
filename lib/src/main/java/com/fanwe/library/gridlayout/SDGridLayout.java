package com.fanwe.library.gridlayout;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

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

    /**
     * 列数
     */
    private int mColumnCount = 1;
    /**
     * 水平方向间距
     */
    private int mHorizontalSpacing;
    /**
     * 竖直方向间距
     */
    private int mVerticalSpacing;

    private BaseAdapter mAdapter;

    /**
     * 设置列数
     *
     * @param columnCount
     */
    public void setColumnCount(int columnCount)
    {
        if (mColumnCount != columnCount)
        {
            mColumnCount = columnCount;
        }
    }

    /**
     * 设置水平方向间距
     *
     * @param horizontalSpacing
     */
    public void setHorizontalSpacing(int horizontalSpacing)
    {
        if (mHorizontalSpacing != horizontalSpacing)
        {
            mHorizontalSpacing = horizontalSpacing;
        }
    }

    /**
     * 设置竖直方向间距
     *
     * @param verticalSpacing
     */
    public void setVerticalSpacing(int verticalSpacing)
    {
        if (mVerticalSpacing != verticalSpacing)
        {
            mVerticalSpacing = verticalSpacing;
        }
    }

    public void setAdapter(BaseAdapter adapter)
    {
        if (mAdapter != null)
        {
            mAdapter.unregisterDataSetObserver(mDataSetObserver);
        }
        mAdapter = adapter;
        if (adapter != null)
        {
            adapter.registerDataSetObserver(mDataSetObserver);
        }
        bindData();
    }

    private DataSetObserver mDataSetObserver = new DataSetObserver()
    {
        @Override
        public void onChanged()
        {
            super.onChanged();
            bindData();
        }
    };

    private void bindData()
    {
        if (mAdapter == null || mAdapter.getCount() <= 0)
        {
            return;
        }

        removeAllViews();
        final int count = mAdapter.getCount();
        for (int i = 0; i < count; i++)
        {
            View child = mAdapter.getView(i, null, this);
            if (child != null)
            {
                addView(child);
            }
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
