package com.example.prm392_fe.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;

import com.example.prm392_fe.R;
import com.example.prm392_fe.activity.OrderDetailActivity;
import com.example.prm392_fe.model.Order;

import java.util.ArrayList;

import lombok.Setter;

public class OrderAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Order> orders;
    @Setter private OnItemClickListener onDetailClickListener;
    @Setter private OnItemClickListener onDoneClickListener;

    public OrderAdapter(Context context, ArrayList<Order> orders) {
        this.context = context;
        this.orders = orders;
    }

    public interface OnItemClickListener {
        void onItemClick(int orderId);
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
            holder.btnDetail = convertView.findViewById(R.id.btnDetail);
            holder.btnDone = convertView.findViewById(R.id.btnDone);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Order order = orders.get(position);

        holder.tvOrderId.setText(String.valueOf(order.getOrderID()));
        holder.tvBookingDate.setText(order.getBookingTime());
        holder.tvBookingPrice.setText(String.valueOf(order.getBookingPrice()));
        holder.btnDetail.setOnClickListener(v -> {
            onDetailClickListener.onItemClick(order.getOrderID());
        });
        holder.btnDone.setOnClickListener(v -> {
            onDoneClickListener.onItemClick(order.getOrderID());
        });

        return convertView;
    }

    static class ViewHolder {
        TextView tvOrderId;
        TextView tvBookingDate;
        TextView tvBookingPrice;
        AppCompatButton btnDetail;
        AppCompatButton btnDone;
    }
}
