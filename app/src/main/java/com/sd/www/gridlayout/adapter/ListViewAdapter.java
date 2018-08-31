package com.sd.www.gridlayout.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sd.lib.adapter.FSimpleAdapter;
import com.sd.www.gridlayout.DataModel;

/**
 * Created by Administrator on 2017/7/17.
 */

public class ListViewAdapter extends FSimpleAdapter<DataModel>
{
    @Override
    public int getLayoutId(int position, View convertView, ViewGroup parent)
    {
        return com.sd.www.gridlayout.R.layout.item_listview;
    }

    @Override
    public void onBindData(int position, View convertView, ViewGroup parent, DataModel model)
    {
        TextView tv_position = get(com.sd.www.gridlayout.R.id.tv_position, convertView);
        tv_position.setText(String.valueOf(position));

//        if (position % 2 == 0)
//        {
//            convertView.setLayoutParams(new ViewGroup.LayoutParams(-1, 100));
//        } else
//        {
//            convertView.setLayoutParams(new ViewGroup.LayoutParams(-1, 200));
//        }

//        if (position % 2 == 0)
//        {
//            convertView.setLayoutParams(new ViewGroup.LayoutParams(100, -1));
//        } else
//        {
//            convertView.setLayoutParams(new ViewGroup.LayoutParams(200, -1));
//        }
    }
}
