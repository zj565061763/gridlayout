package com.sd.lib.gridlayout;

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


public class FGridLayout extends ViewGroup
{
    public FGridLayout(Context context)
    {
        super(context);
        init();
    }

    public FGridLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public FGridLayout(Context context, AttributeSet attrs, int defStyleAttr)
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
    private final SparseArray<Integer> mRowHeightHolder = new SparseArray<>();
    /**
     * 保存每一列的最大宽度，key：列，value：宽度
     */
    private final SparseArray<Integer> mColumnWidthHolder = new SparseArray<>();
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
            setBackgroundColor(Color.TRANSPARENT);
    }

    /**
     * 设置布局方向，默认竖直方向
     * <br>
     * {@link #VERTICAL}
     * <br>
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
            throw new IllegalArgumentException("orientation must be FGridLayout.VERTICAL or FGridLayout.HORIZONTAL");
        }
    }

    /**
     * 返回布局方向
     * <br>
     * {@link #VERTICAL}
     * <br>
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
        if (spanCount < 1)
            spanCount = 1;

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
     * @param drawable
     */
    public void setHorizontalDivider(Drawable drawable)
    {
        mHorizontalDivider = drawable;
    }

    /**
     * 设置竖直分割线Drawable
     *
     * @param drawable
     */
    public void setVerticalDivider(Drawable drawable)
    {
        mVerticalDivider = drawable;
    }

    /**
     * 设置横竖分割线交叉的地方优先用横分割线还是竖分割线，默认优先横分割线
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
            mAdapter.unregisterDataSetObserver(mDataSetObserver);

        mAdapter = adapter;

        if (adapter != null)
            adapter.registerDataSetObserver(mDataSetObserver);

        bindData();
    }

    private final DataSetObserver mDataSetObserver = new DataSetObserver()
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
            return;

        final int count = mAdapter.getCount();
        for (int i = 0; i < count; i++)
        {
            final View child = mAdapter.getView(i, null, this);
            addView(child);
        }
    }

    //----------help method start----------

    public int getVerticalSpacing()
    {
        return mVerticalDivider == null ? mVerticalSpacing : mVerticalDivider.getIntrinsicWidth();
    }

    public int getHorizontalSpacing()
    {
        return mHorizontalDivider == null ? mHorizontalSpacing : mHorizontalDivider.getIntrinsicHeight();
    }

    private int getColumnWidthInVerticalMode(int total)
    {
        final int value = total - ((mSpanCount - 1) * getVerticalSpacing())
                - (getPaddingLeft() + getPaddingRight());
        return (int) (value / (float) mSpanCount + 0.5f);
    }

    private int getRowHeightInHorizontalMode(int total)
    {
        final int value = total - ((mSpanCount - 1) * getHorizontalSpacing())
                - (getPaddingTop() + getPaddingBottom());
        return (int) (value / (float) mSpanCount + 0.5f);
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
            final int count = getChildCount() / mSpanCount;
            final int left = getChildCount() % mSpanCount;
            return left == 0 ? count : count + 1;
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
            final int count = getChildCount() / mSpanCount;
            final int left = getChildCount() % mSpanCount;
            return left == 0 ? count : count + 1;
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

        if (mColumnWidthHolder.size() > 0)
        {
            final int count = getColumnCount();
            for (int i = 0; i < count; i++)
            {
                value += mColumnWidthHolder.get(i);
                if (i < count - 1)
                    value += getVerticalSpacing();
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

        if (mRowHeightHolder.size() > 0)
        {
            final int count = getRowCount();
            for (int i = 0; i < count; i++)
            {
                value += mRowHeightHolder.get(i);
                if (i < count - 1)
                    value += getHorizontalSpacing();
            }
        }
        return value;
    }

    private boolean saveRowHeightIfNeed(int row, int childHeight)
    {
        final Integer oldValue = mRowHeightHolder.get(row);
        if (oldValue == null)
        {
            mRowHeightHolder.put(row, childHeight);
            return true;
        } else
        {
            if (childHeight > oldValue)
            {
                mRowHeightHolder.put(row, childHeight);
                return true;
            } else
            {
                return false;
            }
        }
    }

    private boolean saveColumnWidthIfNeed(int column, int childWidth)
    {
        final Integer oldValue = mColumnWidthHolder.get(column);
        if (oldValue == null)
        {
            mColumnWidthHolder.put(column, childWidth);
            return true;
        } else
        {
            if (childWidth > oldValue)
            {
                mColumnWidthHolder.put(column, childWidth);
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
        mRowHeightHolder.clear();
        mColumnWidthHolder.clear();

        final int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        final int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int cWidthMeasureSpec = 0;
        int cHeightMeasureSpec = 0;

        if (mOrientation == VERTICAL)
        {
            if (widthMode == MeasureSpec.UNSPECIFIED)
                cWidthMeasureSpec = widthMeasureSpec;
            else
                cWidthMeasureSpec = MeasureSpec.makeMeasureSpec(getColumnWidthInVerticalMode(widthSize), widthMode);

            cHeightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.UNSPECIFIED);
        } else
        {
            if (heightMode == MeasureSpec.UNSPECIFIED)
                cHeightMeasureSpec = heightMeasureSpec;
            else
                cHeightMeasureSpec = MeasureSpec.makeMeasureSpec(getRowHeightInHorizontalMode(heightSize), heightMode);

            cWidthMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.UNSPECIFIED);
        }

        final int count = getChildCount();
        for (int i = 0; i < count; i++)
        {
            final int row = getRowIndex(i);
            final int col = getColumnIndex(i);
            final View child = getChildAt(i);
            final ViewGroup.LayoutParams params = child.getLayoutParams();

            child.measure(getChildMeasureSpec(cWidthMeasureSpec, 0, params.width),
                    getChildMeasureSpec(cHeightMeasureSpec, 0, params.height));

            saveRowHeightIfNeed(row, child.getMeasuredHeight());
            saveColumnWidthIfNeed(col, child.getMeasuredWidth());
        }

        for (int i = 0; i < count; i++)
        {
            final int row = getRowIndex(i);
            final int col = getColumnIndex(i);
            final View child = getChildAt(i);

            cWidthMeasureSpec = MeasureSpec.makeMeasureSpec(mColumnWidthHolder.get(col), MeasureSpec.EXACTLY);
            cHeightMeasureSpec = MeasureSpec.makeMeasureSpec(mRowHeightHolder.get(row), MeasureSpec.EXACTLY);

            child.measure(cWidthMeasureSpec, cHeightMeasureSpec);
        }

        final int widthFinal = widthMode == MeasureSpec.EXACTLY ? widthSize : getTotalWidth();
        final int heightFinal = heightMode == MeasureSpec.EXACTLY ? heightSize : getTotalHeight();

        setMeasuredDimension(widthFinal, heightFinal);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b)
    {
        int left = 0;
        int top = 0;
        final int count = getChildCount();
        for (int i = 0; i < count; i++)
        {
            final int row = getRowIndex(i);
            final int col = getColumnIndex(i);
            final View child = getChildAt(i);

            if (row == 0)
                top = getPaddingTop();

            if (col == 0)
                left = getPaddingLeft();

            final int right = left + child.getMeasuredWidth();
            final int bottom = top + child.getMeasuredHeight();

            child.layout(left, top, right, bottom);

            if (mOrientation == VERTICAL)
            {
                //下一列的left
                left = right + getVerticalSpacing();
                if (col + 1 == mSpanCount)
                {
                    //下一行的top
                    top += mRowHeightHolder.get(row) + getHorizontalSpacing();
                }
            } else
            {
                //下一行的top
                top += mRowHeightHolder.get(row) + getHorizontalSpacing();
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

        final boolean drawHor = mHorizontalDivider != null && mRowHeightHolder.size() > 1;
        final boolean drawVer = mVerticalDivider != null && mColumnWidthHolder.size() > 1;

        if (drawHor || drawVer)
        {
            final int lastRow = mRowHeightHolder.size() - 1;
            final int lastCol = mColumnWidthHolder.size() - 1;

            int left = 0;
            int top = 0;
            int right = 0;
            int bottom = 0;

            int count = getChildCount();
            for (int i = 0; i < count; i++)
            {
                final int row = getRowIndex(i);
                final int col = getColumnIndex(i);
                final View child = getChildAt(i);

                if (drawHor && row < lastRow)
                {
                    left = child.getLeft();
                    top = child.getBottom();
                    right = child.getRight();
                    bottom = top + mHorizontalDivider.getIntrinsicHeight();

                    if (mPreferHorizontalDivider && col < lastCol)
                        right += getVerticalSpacing();

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
                        bottom += getHorizontalSpacing();

                    mVerticalDivider.setBounds(left, top, right, bottom);
                    mVerticalDivider.draw(canvas);
                }
            }
        }

        canvas.restore();
    }
}
