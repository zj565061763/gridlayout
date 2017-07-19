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

    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;

    private SparseArray<Integer> mArrRowHeight = new SparseArray<>();

    private int mOrientation = HORIZONTAL;
    /**
     * 列数
     */
    private int mNumColumns = 1;
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
     * @param numColumns
     */
    public void setNumColumns(int numColumns)
    {
        if (mNumColumns != numColumns)
        {
            mNumColumns = numColumns;
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

    private int getColumnWidth(int parentWidth)
    {
        int width = parentWidth - ((mNumColumns - 1) * mVerticalSpacing);
        width -= getPaddingLeft() + getPaddingRight();
        int colWidth = (int) (width / (float) mNumColumns + 0.5f);
        return colWidth;
    }

    private int getColumnIndex(int childIndex)
    {
        if (mOrientation == VERTICAL)
        {
            return childIndex % mNumColumns;
        } else
        {
            return childIndex / mNumColumns;
        }
    }

    private int getRowIndex(int childIndex)
    {
        if (mOrientation == VERTICAL)
        {
            return childIndex / mNumColumns;
        } else
        {
            return childIndex % mNumColumns;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        if (mOrientation == VERTICAL)
        {
            onMeasureVertical(widthMeasureSpec, heightMeasureSpec);
        } else
        {
            onMeasureHorizontal(widthMeasureSpec, heightMeasureSpec);
        }
    }

    private void onMeasureVertical(int widthMeasureSpec, int heightMeasureSpec)
    {
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

        int cWidthMeasureSpec = MeasureSpec.makeMeasureSpec(getColumnWidth(sizeWidth), MeasureSpec.EXACTLY);
        int cHeightMeasureSpec = MeasureSpec.makeMeasureSpec(sizeHeight, MeasureSpec.UNSPECIFIED);
        int count = getChildCount();
        int row = 0;
        int tempRow = 0;
        int rowHeight = 0;
        int totalHeight = 0;
        for (int i = 0; i < count; i++)
        {
            row = getRowIndex(i);

            if (row != tempRow)
            {
                //新一行
                mArrRowHeight.put(tempRow, rowHeight);
                totalHeight += rowHeight + mHorizontalSpacing;

                rowHeight = 0;
            }

            View child = getChildAt(i);
            LayoutParams params = child.getLayoutParams();
            cWidthMeasureSpec = getChildMeasureSpec(cWidthMeasureSpec, 0, params.width);
            cHeightMeasureSpec = getChildMeasureSpec(cHeightMeasureSpec, 0, params.height);
            child.measure(cWidthMeasureSpec, cHeightMeasureSpec);

            if (child.getMeasuredHeight() > rowHeight)
            {
                rowHeight = child.getMeasuredHeight();
            }

            tempRow = row;
        }

        if (rowHeight > 0)
        {
            totalHeight += rowHeight;
            mArrRowHeight.put(row, rowHeight);
        }

        if (modeHeight != MeasureSpec.EXACTLY)
        {
            sizeHeight = totalHeight + getPaddingTop() + getPaddingBottom();
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
            int div = getChildCount() / mNumColumns;
            if (getChildCount() % mNumColumns == 0)
            {
                return div;
            } else
            {
                return div + 1;
            }
        } else
        {
            return mNumColumns;
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

    private void onMeasureHorizontal(int widthMeasureSpec, int heightMeasureSpec)
    {
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

        int cWidthMeasureSpec = MeasureSpec.makeMeasureSpec(sizeWidth, MeasureSpec.UNSPECIFIED);
        int cHeightMeasureSpec = MeasureSpec.makeMeasureSpec(sizeHeight, MeasureSpec.UNSPECIFIED);
        int row = 0;
        int count = getChildCount();
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
            if (col + 1 == mNumColumns)
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
            if (row + 1 == mNumColumns)
            {
                //下一列的left
                left = right + mVerticalSpacing;
            }
        }
    }
}
