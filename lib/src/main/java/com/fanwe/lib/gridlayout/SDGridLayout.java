/*
 * Copyright (C) 2017 zhengjun, fanwe (http://www.fanwe.com)
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
package com.fanwe.lib.gridlayout;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


public class SDGridLayout extends ViewGroup
{
    public SDGridLayout(Context context)
    {
        super(context);
        init();
    }

    public SDGridLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public SDGridLayout(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init();
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

    private Drawable mHorizontalDivider;
    private Drawable mVerticalDivider;

    private boolean mPreferHorizontalDivider = true;

    private BaseAdapter mAdapter;

    private void init()
    {
        if (getBackground() == null)
        {
            setBackgroundColor(Color.TRANSPARENT);
        }
    }

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
            mOrientation = orientation;
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
     * 设置行或者列的
     * <p>
     * 网格数量
     *
     * @param spanCount
     */
    public void setSpanCount(int spanCount)
    {
        if (spanCount < 1)
        {
            spanCount = 1;
        }
        mSpanCount = spanCount;
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
        mHorizontalSpacing = horizontalSpacing;
    }

    /**
     * 设置竖直方向间距
     *
     * @param verticalSpacing
     */
    public void setVerticalSpacing(int verticalSpacing)
    {
        mVerticalSpacing = verticalSpacing;
    }

    /**
     * 设置水平分割线Drawable
     *
     * @param horizontalDivider
     */
    public void setHorizontalDivider(Drawable horizontalDivider)
    {
        mHorizontalDivider = horizontalDivider;
    }

    /**
     * 设置竖直分割线Drawable
     *
     * @param verticalDivider
     */
    public void setVerticalDivider(Drawable verticalDivider)
    {
        mVerticalDivider = verticalDivider;
    }

    /**
     * 设置横竖分割线交叉的地方优先用横分割线还是竖分割线
     *
     * @param preferHorizontalDivider true-优先横分割线，false-优先竖分割线
     */
    public void setPreferHorizontalDivider(boolean preferHorizontalDivider)
    {
        mPreferHorizontalDivider = preferHorizontalDivider;
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
        View child = null;
        for (int i = 0; i < count; i++)
        {
            child = mAdapter.getView(i, null, this);
            addView(child);
        }
    }

    //----------help method start----------

    public int getVerticalSpacing()
    {
        if (mVerticalDivider != null)
        {
            return mVerticalDivider.getIntrinsicWidth();
        }
        return mVerticalSpacing;
    }

    public int getHorizontalSpacing()
    {
        if (mHorizontalDivider != null)
        {
            return mHorizontalDivider.getIntrinsicHeight();
        }
        return mHorizontalSpacing;
    }

    /**
     * 返回VERTICAL方向下，列的宽度
     *
     * @param parentWidth
     * @return
     */
    private int getColumnWidth(int parentWidth)
    {
        int width = parentWidth - ((mSpanCount - 1) * getVerticalSpacing());
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

        if (mArrColumnWidth.size() > 0)
        {
            final int count = getColumnCount();
            for (int i = 0; i < count; i++)
            {
                value += mArrColumnWidth.get(i);
                if (i < count - 1)
                {
                    value += getVerticalSpacing();
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

        if (mArrRowHeight.size() > 0)
        {
            final int count = getRowCount();
            for (int i = 0; i < count; i++)
            {
                value += mArrRowHeight.get(i);
                if (i < count - 1)
                {
                    value += getHorizontalSpacing();
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
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int cWidthMeasureSpec = 0;
        int cHeightMeasureSpec = 0;

        if (mOrientation == VERTICAL && widthMode == MeasureSpec.EXACTLY)
        {
            cWidthMeasureSpec = MeasureSpec.makeMeasureSpec(getColumnWidth(widthSize), MeasureSpec.EXACTLY);
        } else
        {
            cWidthMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.UNSPECIFIED);
        }
        cHeightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.UNSPECIFIED);

        int row = 0;
        int col = 0;
        View child = null;
        final int count = getChildCount();
        for (int i = 0; i < count; i++)
        {
            row = getRowIndex(i);
            col = getColumnIndex(i);

            child = getChildAt(i);
            final LayoutParams params = child.getLayoutParams();

            child.measure(getChildMeasureSpec(cWidthMeasureSpec, 0, params.width),
                    getChildMeasureSpec(cHeightMeasureSpec, 0, params.height));

            saveRowHeightIfNeed(row, child.getMeasuredHeight());
            saveColumnWidthIfNeed(col, child.getMeasuredWidth());
        }

        for (int i = 0; i < count; i++)
        {
            row = getRowIndex(i);
            col = getColumnIndex(i);

            child = getChildAt(i);

            cWidthMeasureSpec = MeasureSpec.makeMeasureSpec(mArrColumnWidth.get(col), MeasureSpec.EXACTLY);
            cHeightMeasureSpec = MeasureSpec.makeMeasureSpec(mArrRowHeight.get(row), MeasureSpec.EXACTLY);

            child.measure(cWidthMeasureSpec, cHeightMeasureSpec);
        }

        if (widthMode != MeasureSpec.EXACTLY)
        {
            widthSize = getTotalWidth();
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

        final int count = getChildCount();
        for (int i = 0; i < count; i++)
        {
            row = getRowIndex(i);
            col = getColumnIndex(i);

            final View child = getChildAt(i);
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
                left = right + getVerticalSpacing();
                if (col + 1 == mSpanCount)
                {
                    //下一行的top
                    top += mArrRowHeight.get(row) + getHorizontalSpacing();
                }
            } else
            {
                //下一行的top
                top += mArrRowHeight.get(row) + getHorizontalSpacing();
                if (row + 1 == mSpanCount)
                {
                    //下一列的left
                    left = right + getVerticalSpacing();
                }
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        canvas.save();

        final boolean drawHor = mHorizontalDivider != null && mArrRowHeight.size() > 1;
        final boolean drawVer = mVerticalDivider != null && mArrColumnWidth.size() > 1;

        if (drawHor || drawVer)
        {
            final int lastRow = mArrRowHeight.size() - 1;
            final int lastCol = mArrColumnWidth.size() - 1;

            int row = 0;
            int col = 0;
            View child = null;

            int left = 0;
            int top = 0;
            int right = 0;
            int bottom = 0;

            int count = getChildCount();
            for (int i = 0; i < count; i++)
            {
                row = getRowIndex(i);
                col = getColumnIndex(i);
                child = getChildAt(i);

                if (drawHor && row < lastRow)
                {
                    left = child.getLeft();
                    top = child.getBottom();
                    right = child.getRight();
                    bottom = top + mHorizontalDivider.getIntrinsicHeight();

                    if (mPreferHorizontalDivider && col < lastCol)
                    {
                        right += getVerticalSpacing();
                    }

                    mHorizontalDivider.setBounds(left, top, right, bottom);
                    mHorizontalDivider.draw(canvas);
                }

                if (drawVer && col < lastCol)
                {
                    left = child.getRight();
                    top = child.getTop();
                    right = left + mVerticalDivider.getIntrinsicWidth();
                    bottom = child.getBottom();

                    if (!mPreferHorizontalDivider && row < lastRow)
                    {
                        bottom += getHorizontalSpacing();
                    }

                    mVerticalDivider.setBounds(left, top, right, bottom);
                    mVerticalDivider.draw(canvas);
                }
            }
        }

        canvas.restore();
    }
}
