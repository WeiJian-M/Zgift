package com.example.zgift.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.zgift.R;
import com.example.zgift.domain.Courier;

import java.util.List;

public class CourierAdapter extends ArrayAdapter<Courier> {

    private int resourceId;

    public CourierAdapter(Context context, int textViewResourceId, List<Courier> objects) {
        super(context, textViewResourceId, objects);
        this.resourceId = textViewResourceId;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        Courier courier = getItem(position); // 获取实例
        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);

        TextView tv_item_courier_name = (TextView) view.findViewById(R.id.tv_item_courier_name);

        tv_item_courier_name.setText(courier.getCourierName());

        return view;
    }
}
