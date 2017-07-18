package com.fanwe.www.gridlayout;

import android.os.Bundle;

import com.fanwe.library.SDLibrary;
import com.fanwe.library.activity.SDBaseActivity;
import com.fanwe.library.gridlayout.SDGridLayout;
import com.fanwe.www.gridlayout.adapter.ListViewAdapter;

public class MainActivity extends SDBaseActivity
{

    private SDGridLayout view_grid;

    @Override
    protected void init(Bundle savedInstanceState)
    {
        SDLibrary.getInstance().init(getApplication());
        setContentView(R.layout.activity_main);
        view_grid = (SDGridLayout) findViewById(R.id.view_grid);

        ListViewAdapter adapter = new ListViewAdapter(DataModel.get(10), this);

        view_grid.setNumColumns(3); //设置列数
        view_grid.setVerticalSpacing(10); //设置竖直方向的Item间隔
        view_grid.setHorizontalSpacing(10); //设置水平方向的Item间隔
        view_grid.setAdapter(adapter); //设置适配器绑定数据
    }
}
