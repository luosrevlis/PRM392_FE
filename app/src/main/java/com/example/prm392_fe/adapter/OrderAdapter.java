package com.example.prm392_fe.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.prm392_fe.R;
import com.example.prm392_fe.model.Order;

import java.util.ArrayList;

public class OrderAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Order> orders;

    public OrderAdapter(Context context, ArrayList<Order> orders) {
        this.context = context;
        this.orders = orders;
    }

    @Override
    public int getCount() {
        return orders.size();
    }

    @Override
    public Object getItem(int position) {
        return orders.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.order_list_item, parent, false);
            holder = new ViewHolder();
            holder.tvOrderId = convertView.findViewById(R.id.tvOrderId);
            holder.tvBookingDate = convertView.findViewById(R.id.tvBookingDate);
            holder.tvBookingPrice = convertView.findViewById(R.id.tvBookingPrice);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Order order = orders.get(position);

        holder.tvOrderId.setText(String.valueOf(order.getOrderID()));
        holder.tvBookingDate.setText(order.getBookingTime());
        holder.tvBookingPrice.setText(String.valueOf(order.getBookingPrice()));

        return convertView;
    }

    static class ViewHolder {
        TextView tvOrderId;
        TextView tvBookingDate;
        TextView tvBookingPrice;
    }
}
