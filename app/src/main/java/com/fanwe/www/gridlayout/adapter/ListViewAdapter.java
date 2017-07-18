package com.fanwe.www.gridlayout.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.fanwe.library.adapter.SDSimpleAdapter;
import com.fanwe.www.gridlayout.DataModel;
import com.fanwe.www.gridlayout.R;

import java.util.List;

/**
 * Created by Administrator on 2017/7/17.
 */

public class ListViewAdapter extends SDSimpleAdapter<DataModel>
{
    public ListViewAdapter(List<DataModel> listModel, Activity activity)
    {
        super(listModel, activity);
    }

    @Override
    public int getLayoutId(int position, View convertView, ViewGroup parent)
    {
        return R.layout.item_listview;
    }

    @Override
    public void bindData(int position, View convertView, ViewGroup parent, DataModel model)
    {
//        convertView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));

//        if (position % 2 == 0)
//        {
//            SDViewUtil.setHeight(convertView, 100);
//        } else
//        {
//            SDViewUtil.setHeight(convertView, 200);
//        }
    }
}
