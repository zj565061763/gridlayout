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
     * 保存每一行的最大高度，key：行，value：高度
     */
    private SparseArray<Integer> mArrRowHeight = new SparseArray<>();
    /**
     * 保存每一列的最大宽度，key：列，value：宽度
     */
    private SparseArray<Integer> mArrColumnWidth = new SparseArray<>();
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
        if (orientation == VERTICAL || orientation == HORIZONTAL)
        {
            if (mOrientation != orientation)
            {
                mOrientation = orientation;
                requestLayout();
            }
        } else
        {
            throw new IllegalArgumentException("orientation must be SDGridLayout.VERTICAL or SDGridLayout.HORIZONTAL");
        }
    }

    /**
     * 返回布局方向<br>
     * {@link #VERTICAL}<br>
     * {@link #HORIZONTAL}
     *
     * @return
     */
    public int getOrientation()
    {
        return mOrientation;
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
     * 返回行或者列的网格数量
     *
     * @return
     */
    public int getSpanCount()
    {
        return mSpanCount;
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

    //----------help method start----------

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
     * 返回一共有几列
     *
     * @return
     */
    private int getColumnCount()
    {
        if (mOrientation == VERTICAL)
        {
            return mSpanCount;
        } else
        {
            int div = getChildCount() / mSpanCount;
            if (getChildCount() % mSpanCount == 0)
            {
                return div;
            } else
            {
                return div + 1;
            }
        }
    }

    /**
     * 返回总的宽度
     *
     * @return
     */
    private int getTotalWidth()
    {
        int value = getPaddingLeft() + getPaddingRight();

        int count = getColumnCount();
        if (count > 0)
        {
            for (int i = 0; i < count; i++)
            {
                value += mArrColumnWidth.get(i);
                if (i < count - 1)
                {
                    value += mVerticalSpacing;
                }
            }
        }
        return value;
    }

    /**
     * 返回总的高度
     *
     * @return
     */
    private int getTotalHeight()
    {
        int value = getPaddingTop() + getPaddingBottom();

        int count = getRowCount();
        if (count > 0)
        {
            for (int i = 0; i < count; i++)
            {
                value += mArrRowHeight.get(i);
                if (i < count - 1)
                {
                    value += mHorizontalSpacing;
                }
            }
        }
        return value;
    }

    private boolean saveRowHeightIfNeed(int row, int childHeight)
    {
        Integer oldValue = mArrRowHeight.get(row);
        if (oldValue == null)
        {
            mArrRowHeight.put(row, childHeight);
            return true;
        } else
        {
            if (childHeight > oldValue)
            {
                mArrRowHeight.put(row, childHeight);
                return true;
            } else
            {
                return false;
            }
        }
    }

    private boolean saveColumnWidthIfNeed(int column, int childWidth)
    {
        Integer oldValue = mArrColumnWidth.get(column);
        if (oldValue == null)
        {
            mArrColumnWidth.put(column, childWidth);
            return true;
        } else
        {
            if (childWidth > oldValue)
            {
                mArrColumnWidth.put(column, childWidth);
                return true;
            } else
            {
                return false;
            }
        }
    }

    //----------help method end----------

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        mArrRowHeight.clear();
        mArrColumnWidth.clear();

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int cWidthMeasureSpec = 0;
        if (mOrientation == VERTICAL)
        {
            if (widthMode == MeasureSpec.UNSPECIFIED)
            {
                cWidthMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.UNSPECIFIED);
            } else
            {
                cWidthMeasureSpec = MeasureSpec.makeMeasureSpec(getColumnWidth(widthSize), MeasureSpec.EXACTLY);
            }
        } else
        {
            cWidthMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.UNSPECIFIED);
        }
        int cHeightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.UNSPECIFIED);

        int row = 0;
        int col = 0;
        int count = getChildCount();
        for (int i = 0; i < count; i++)
        {
            row = getRowIndex(i);
            col = getColumnIndex(i);

            View child = getChildAt(i);
            LayoutParams params = child.getLayoutParams();
            cWidthMeasureSpec = getChildMeasureSpec(cWidthMeasureSpec, 0, params.width);
            cHeightMeasureSpec = getChildMeasureSpec(cHeightMeasureSpec, 0, params.height);
            child.measure(cWidthMeasureSpec, cHeightMeasureSpec);

            saveRowHeightIfNeed(row, child.getMeasuredHeight());
            saveColumnWidthIfNeed(col, child.getMeasuredWidth());
        }

        switch (widthMode)
        {
            case MeasureSpec.AT_MOST:
                widthSize = Math.max(widthSize, getTotalWidth());
                break;
            case MeasureSpec.EXACTLY:
                break;
            case MeasureSpec.UNSPECIFIED:
                widthSize = getTotalWidth();
                break;
        }

        if (heightMode != MeasureSpec.EXACTLY)
        {
            heightSize = getTotalHeight();
        }

        setMeasuredDimension(widthSize, heightSize);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b)
    {
        int row = 0;
        int col = 0;
        int left = 0;
        int top = 0;

        int count = getChildCount();
        for (int i = 0; i < count; i++)
        {
            row = getRowIndex(i);
            col = getColumnIndex(i);

            View child = getChildAt(i);
            if (row == 0)
            {
                top = getPaddingTop();
            }
            if (col == 0)
            {
                left = getPaddingLeft();
            }

            int right = left + child.getMeasuredWidth();
            int bottom = top + child.getMeasuredHeight();

            child.layout(left, top, right, bottom);

            if (mOrientation == VERTICAL)
            {
                //下一列的left
                left = right + mVerticalSpacing;
                if (col + 1 == mSpanCount)
                {
                    //下一行的top
                    top += mArrRowHeight.get(row) + mHorizontalSpacing;
                }
            } else
            {
                //下一行的top
                top += mArrRowHeight.get(row) + mHorizontalSpacing;
                if (row + 1 == mSpanCount)
                {
                    //下一列的left
                    left = right + mVerticalSpacing;
                }
            }
        }
    }
}
