# GridLayout
由于在某些场合确实需要在ScrollView中嵌套网格或者列表布局，但是原生的ListView和GridView不能和ScrollView嵌套，如果重写ListView和GridView的onMeasure方法会导致在布局复杂的情况下极其的消耗性能卡顿明显。<br>
所以写了这个类，该类继承自ViewGroup实现的类似GridView的功能，方法名字和GridView保持一致，但是不包含布局重用机制。

## 效果图
* 在ScrollView中<br>
![](http://thumbsnap.com/s/zX46puTZ.png?0717)

* 在GridView的Item中<br>
![](http://thumbsnap.com/s/0TwOIbYZ.png?0717)

## 使用
1. xml布局
```xml
<?xml version="1.0" encoding="utf-8"?>
<ScrollView
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent">

        <com.fanwe.library.gridlayout.SDGridLayout
            android:id="@+id/view_grid"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:background="@color/green"/>

    </LinearLayout>

</ScrollView>
```
2. java代码
```java
ListViewAdapter adapter = new ListViewAdapter(DataModel.get(100), this);

view_grid.setNumColumns(3); //设置列数
view_grid.setVerticalSpacing(10); //设置竖直方向的Item间隔
view_grid.setHorizontalSpacing(10); //设置水平方向的Item间隔
view_grid.setAdapter(adapter); //设置适配器绑定数据
```
