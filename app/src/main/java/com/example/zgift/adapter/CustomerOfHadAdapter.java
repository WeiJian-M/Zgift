package com.example.zgift.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import com.example.zgift.R;
import com.example.zgift.domain.CustomerOfHad;

import java.util.List;
public class CustomerOfHadAdapter extends ArrayAdapter<CustomerOfHad> {

    private int resourceId;

    public CustomerOfHadAdapter(Context context, int textViewResource, List<CustomerOfHad> objects) {
        super(context, textViewResource, objects);
        resourceId = textViewResource;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        CustomerOfHad customerOfHad = getItem(position); // 获取实例

        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);

        TextView tv_item_customerHad_name = (TextView) view.findViewById(R.id.tv_item_customerHad_name);
        TextView tv_item_courierHad_name = (TextView) view.findViewById(R.id.tv_item_courierHad_name);

        tv_item_customerHad_name.setText(customerOfHad.getCustomerName());
        tv_item_courierHad_name.setText(customerOfHad.getCourierName());

        return view;
    }
}
