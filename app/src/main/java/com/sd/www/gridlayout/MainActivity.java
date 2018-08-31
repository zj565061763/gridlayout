package com.sd.www.gridlayout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.GridLayout;

import com.sd.lib.gridlayout.FGridLayout;
import com.sd.www.gridlayout.adapter.ListViewAdapter;

public class MainActivity extends AppCompatActivity
{
    private FGridLayout view_grid;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        view_grid = findViewById(R.id.view_grid);

        ListViewAdapter adapter = new ListViewAdapter();
        adapter.getDataHolder().setData(DataModel.get(20));

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
    }
}
