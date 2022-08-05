package com.example.zgift.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.zgift.R;
import com.example.zgift.domain.Customer;

import java.util.List;

public class CustomerAdapter extends ArrayAdapter<Customer> {

    private int resourceId;

    public CustomerAdapter(Context context, int textViewResource, List<Customer> objects) {
        super(context, textViewResource, objects);
        this.resourceId = textViewResource;
    }

    public View getView(int position, View convertView, ViewGroup parent){

        Customer customer = getItem(position); // 获取当前项的实例

        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);

        TextView tv_item_customer_name = (TextView) view.findViewById(R.id.tv_item_customer_name);
        tv_item_customer_name.setText(customer.getCustomerName());

        return view;
    }
}
