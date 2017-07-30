package com.fanwe.www.gridlayout;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.fanwe.library.SDLibrary;
import com.fanwe.library.activity.SDBaseActivity;
import com.fanwe.library.drawable.SDDrawable;
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

        ListViewAdapter adapter = new ListViewAdapter(DataModel.get(13), this);

        view_grid.setSpanCount(3); //设置行或者列的网格数量
        view_grid.setHorizontalDivider(new SDDrawable().color(Color.BLUE).size(5));
        view_grid.setVerticalDivider(new SDDrawable().color(Color.BLACK).size(5));
        view_grid.setAdapter(adapter); //设置适配器绑定数据

        findViewById(R.id.btn_orientation).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (view_grid.getOrientation() == SDGridLayout.HORIZONTAL)
                {
                    view_grid.setOrientation(SDGridLayout.VERTICAL); //设置竖直方向布局（默认竖直方向）
                } else
                {
                    view_grid.setOrientation(SDGridLayout.HORIZONTAL); //设置水平方向布局
                }
                view_grid.requestLayout(); //重新布局
            }
        });
    }
}
