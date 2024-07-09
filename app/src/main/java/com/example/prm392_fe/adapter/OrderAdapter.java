package com.example.prm392_fe.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_fe.R;
import com.example.prm392_fe.model.Order;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    Context context;
    ArrayList<Order> items;
    boolean isAdmin;
    DecimalFormat df;
    OnItemClickListener onDetailClickListener;
    OnItemClickListener onDoneClickListener;

    public OrderAdapter(Context context, ArrayList<Order> items, boolean isAdmin) {
        this.context = context;
        this.items = items;
        this.isAdmin = isAdmin;
        df = new DecimalFormat("##,###.#k");
    }

    public interface OnItemClickListener {
        void onItemClick(int orderId);
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_list_item, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = items.get(position);

        holder.tvOrderId.setText(String.valueOf(order.getOrderID()));

        String originalFormat = "yyyy-MM-dd'T'HH:mm:ss"; // Adjust this format according to the actual format of your bookingTime
        String targetFormat = "dd/MM/yyyy";

        SimpleDateFormat originalDateFormat = new SimpleDateFormat(originalFormat);
        SimpleDateFormat targetDateFormat = new SimpleDateFormat(targetFormat);

        try {
            Date date = originalDateFormat.parse(order.getBookingTime());
            // Add 7 hours to the date
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.HOUR_OF_DAY, 7);
            Date newDate = calendar.getTime();

            String formattedDate = targetDateFormat.format(newDate);
            holder.tvBookingDate.setText(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
            // Handle the error
        }

        DecimalFormat df = new DecimalFormat("##,###.#k");
        holder.tvBookingPrice.setText(df.format(order.getBookingPrice() / 1000));
        holder.btnDetail.setOnClickListener(v -> onDetailClickListener.onItemClick(order.getOrderID()));

        if (isAdmin) {
            holder.btnDone.setOnClickListener(v -> onDoneClickListener.onItemClick(order.getOrderID()));
        } else {
            holder.btnDone.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(ArrayList<Order> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderId;
        TextView tvBookingDate;
        TextView tvBookingPrice;
        AppCompatButton btnDetail;
        AppCompatButton btnDone;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvBookingDate = itemView.findViewById(R.id.tvBookingDate);
            tvBookingPrice = itemView.findViewById(R.id.tvBookingPrice);
            btnDetail = itemView.findViewById(R.id.btnDetail);
            btnDone = itemView.findViewById(R.id.btnDone);
        }
    }
}
