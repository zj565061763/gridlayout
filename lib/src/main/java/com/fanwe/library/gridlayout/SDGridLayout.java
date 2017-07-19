package com.fanwe.library.gridlayout;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.util.SparseArray;
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
     * 水平布局方向
     */
    public static final int HORIZONTAL = 0;
    /**
     * 竖直布局方向
     */
    public static final int VERTICAL = 1;
    /**
     * 保存每一行的最高高度，key：行，value：高度
     */
    private SparseArray<Integer> mArrRowHeight = new SparseArray<>();
    /**
     * 布局方向
     */
    private int mOrientation = VERTICAL;
    /**
     * 行或者列的网格数量
     */
    private int mSpanCount = 1;
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
     * 设置布局方向，默认竖直方向<br>
     * {@link #VERTICAL}<br>
     * {@link #HORIZONTAL}
     *
     * @param orientation
     */
    public void setOrientation(int orientation)
    {
        mOrientation = orientation;
    }

    /**
     * 设置行或者列的网格数量
     *
     * @param spanCount
     */
    public void setSpanCount(int spanCount)
    {
        if (mSpanCount != spanCount)
        {
            mSpanCount = spanCount;
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

    /**
     * 设置适配器
     *
     * @param adapter
     */
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
        removeAllViews();
        if (mAdapter == null || mAdapter.getCount() <= 0)
        {
            return;
        }

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

    /**
     * 返回VERTICAL方向下，列的宽度
     *
     * @param parentWidth
     * @return
     */
    private int getColumnWidth(int parentWidth)
    {
        int width = parentWidth - ((mSpanCount - 1) * mVerticalSpacing);
        width -= getPaddingLeft() + getPaddingRight();
        int colWidth = (int) (width / (float) mSpanCount + 0.5f);
        return colWidth;
    }

    /**
     * 返回child所在的列
     *
     * @param childIndex
     * @return
     */
    private int getColumnIndex(int childIndex)
    {
        if (mOrientation == VERTICAL)
        {
            return childIndex % mSpanCount;
        } else
        {
            return childIndex / mSpanCount;
        }
    }

    /**
     * 返回child所在的行
     *
     * @param childIndex
     * @return
     */
    private int getRowIndex(int childIndex)
    {
        if (mOrientation == VERTICAL)
        {
            return childIndex / mSpanCount;
        } else
        {
            return childIndex % mSpanCount;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

        int cWidthMeasureSpec = 0;
        if (mOrientation == VERTICAL)
        {
            cWidthMeasureSpec = MeasureSpec.makeMeasureSpec(getColumnWidth(sizeWidth), MeasureSpec.EXACTLY);
        } else
        {
            cWidthMeasureSpec = MeasureSpec.makeMeasureSpec(sizeWidth, MeasureSpec.UNSPECIFIED);
        }
        int cHeightMeasureSpec = MeasureSpec.makeMeasureSpec(sizeHeight, MeasureSpec.UNSPECIFIED);
        int count = getChildCount();
        int row = 0;
        for (int i = 0; i < count; i++)
        {
            row = getRowIndex(i);

            View child = getChildAt(i);
            LayoutParams params = child.getLayoutParams();
            cWidthMeasureSpec = getChildMeasureSpec(cWidthMeasureSpec, 0, params.width);
            cHeightMeasureSpec = getChildMeasureSpec(cHeightMeasureSpec, 0, params.height);
            child.measure(cWidthMeasureSpec, cHeightMeasureSpec);

            saveRowHeightIfNeed(row, child.getMeasuredHeight());
        }

        if (modeHeight != MeasureSpec.EXACTLY)
        {
            sizeHeight = getTotalHeight();
        }

        setMeasuredDimension(sizeWidth, sizeHeight);
    }

    private boolean saveRowHeightIfNeed(int row, int childHeight)
    {
        Integer oldHeight = mArrRowHeight.get(row);
        if (oldHeight == null)
        {
            mArrRowHeight.put(row, childHeight);
            return true;
        } else
        {
            if (childHeight > oldHeight)
            {
                mArrRowHeight.put(row, childHeight);
                return true;
            } else
            {
                return false;
            }
        }
    }

    /**
     * 返回一共有几行
     *
     * @return
     */
    private int getRowCount()
    {
        if (mOrientation == VERTICAL)
        {
            int div = getChildCount() / mSpanCount;
            if (getChildCount() % mSpanCount == 0)
            {
                return div;
            } else
            {
                return div + 1;
            }
        } else
        {
            return mSpanCount;
        }
    }

    /**
     * 返回总的高度
     *
     * @return
     */
    private int getTotalHeight()
    {
        int height = getPaddingTop() + getPaddingBottom();

        int rowCount = getRowCount();
        if (rowCount > 0)
        {
            for (int i = 0; i < rowCount; i++)
            {
                height += mArrRowHeight.get(i);
                if (i < rowCount - 1)
                {
                    height += mHorizontalSpacing;
                }
            }
        }
        return height;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b)
    {
        if (mOrientation == VERTICAL)
        {
            onLayoutVertical(changed, l, t, r, b);
        } else
        {
            onLayoutHorizontal(changed, l, t, r, b);
        }
    }

    private void onLayoutVertical(boolean changed, int l, int t, int r, int b)
    {
        int col = 0;
        int row = 0;
        int rowHeight = 0;
        int left = 0;
        int top = 0;
        int count = getChildCount();
        for (int i = 0; i < count; i++)
        {
            col = getColumnIndex(i);
            row = getRowIndex(i);
            rowHeight = mArrRowHeight.get(row);

            View child = getChildAt(i);
            if (col == 0)
            {
                left = getPaddingLeft();
            }
            if (row == 0)
            {
                top = getPaddingTop();
            }

            int right = left + child.getMeasuredWidth();
            int bottom = top + child.getMeasuredHeight();

            child.layout(left, top, right, bottom);

            //下一列的left
            left = right + mVerticalSpacing;
            if (col + 1 == mSpanCount)
            {
                //下一行的top
                top += rowHeight + mHorizontalSpacing;
            }
        }
    }

    private void onLayoutHorizontal(boolean changed, int l, int t, int r, int b)
    {
        int col = 0;
        int row = 0;
        int rowHeight = 0;
        int left = 0;
        int top = 0;
        int count = getChildCount();
        for (int i = 0; i < count; i++)
        {
            col = getColumnIndex(i);
            row = getRowIndex(i);
            rowHeight = mArrRowHeight.get(row);

            View child = getChildAt(i);
            if (col == 0)
            {
                left = getPaddingLeft();
            }
            if (row == 0)
            {
                top = getPaddingTop();
            }

            int right = left + child.getMeasuredWidth();
            int bottom = top + child.getMeasuredHeight();

            child.layout(left, top, right, bottom);

            //下一行的top
            top += rowHeight + mHorizontalSpacing;
            if (row + 1 == mSpanCount)
            {
                //下一列的left
                left = right + mVerticalSpacing;
            }
        }
    }
}
