# About
由于在某些场合确实需要在ScrollView中嵌套网格或者列表布局，但是原生的ListView和GridView不能和ScrollView嵌套，如果重写ListView和GridView的onMeasure方法会导致在布局复杂的情况下极其的消耗性能卡顿明显。<br>
所以写了这个类，该类继承自ViewGroup，类似ReyclerView，支持水平方向和竖直方向布局，但是不包含布局重用机制。

## Gradle
[![](https://jitpack.io/v/zj565061763/gridlayout.svg)](https://jitpack.io/#zj565061763/gridlayout)

## 效果图
![](http://thumbsnap.com/i/xnAK2Zp1.gif?0719)

## 使用
1. xml布局
```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical">

    <Button
        android:id="@+id/btn_orientation"
        android:layout_width="match_parent"
        android:layout_height="50dp"
        android:text="切换布局方向"/>

    <com.fanwe.lib.gridlayout.FGridLayout
        android:id="@+id/view_grid"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:background="@color/green"
        android:padding="10dp"/>

</LinearLayout>
```
2. java代码
```java
view_grid.setSpanCount(3); // 设置行或者列的网格数量
view_grid.setOrientation(GridLayout.VERTICAL); // 设置布局方向（默认竖直方向）
view_grid.setHorizontalDivider(getResources().getDrawable(R.drawable.divider_horizontal)); // 设置横分割线
view_grid.setVerticalDivider(getResources().getDrawable(R.drawable.divider_vertical)); // 设置竖分割线
view_grid.setHorizontalSpacing(10); // 设置水平方向间距（如果有设置横分割线，此设置无效）
view_grid.setVerticalSpacing(10); //竖直方向间距（如果有设置竖分割线，此设置无效）
view_grid.setAdapter(adapter); // 设置适配器绑定数据

findViewById(R.id.btn_orientation).setOnClickListener(new View.OnClickListener()
{
    @Override
    public void onClick(View v)
    {
        if (view_grid.getOrientation() == FGridLayout.HORIZONTAL)
        {
            view_grid.setOrientation(FGridLayout.VERTICAL); //设置竖直方向布局
        } else
        {
            view_grid.setOrientation(FGridLayout.HORIZONTAL); //设置水平方向布局
        }
        view_grid.requestLayout(); //重新布局
    }
});
```
