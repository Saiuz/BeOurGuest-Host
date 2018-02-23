package com.lalit.hostbarcode;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class order_lv_adapter extends BaseAdapter {

    Activity activity;
    String[] table_no;
    String[] time;
    String[] name;
    String[] date;
    public order_lv_adapter(Activity activity,  String[] table_no, String[] date, String[] time,String[] name) {
        this.activity = activity;
        this.table_no = table_no;
        this.time = time;
        this.name = name;
        this.date = date;
    }

    @Override
    public int getCount() {
        return time.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater l = activity.getLayoutInflater();
        View view = l.inflate(R.layout.lv_order, null);
        TextView name_tv = (TextView) view.findViewById(R.id.customer_name);
        TextView table = (TextView) view.findViewById(R.id.table_no);
        TextView date_tv = (TextView) view.findViewById(R.id.date);
        TextView time_tv = (TextView) view.findViewById(R.id.time);
        table.setText("Order from table no. "+table_no[position]);
        time_tv.setText(time[position]);
        name_tv.setText(name[position]);
        date_tv.setText(date[position]);
        view.setTag(time[position]);
        return view;
    }
}
