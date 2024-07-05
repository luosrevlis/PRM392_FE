package com.example.prm392_fe.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_fe.R;
import com.example.prm392_fe.model.CartItem;
import com.example.prm392_fe.model.Dish;
import com.example.prm392_fe.model.OrderDetail;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.OrderDetailViewHolder> {
    Context context;
    ArrayList<OrderDetail> items;
    DecimalFormat df;

    public OrderDetailAdapter(Context context, ArrayList<OrderDetail> items) {
        this.context = context;
        this.items = items;
        df = new DecimalFormat("##,###.#k");
    }

    @NonNull
    @Override
    public OrderDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_detail_item, parent, false);
        return new OrderDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailViewHolder holder, int position) {
        OrderDetail item = items.get(position);
        Picasso
                .with(context)
                .load(item.getImageUrl())
                .placeholder(R.drawable.ic_loading)
                .error(R.drawable.ic_error_red)
                .into(holder.ivImage);
        holder.tvName.setText(item.getDishName());
        holder.tvUnitPrice.setText("Đơn giá: " + df.format(item.getPrice() / 1000));
        holder.tvQuantity.setText("Số lượng: " + item.getQuantity());
        holder.tvTotalPrice.setText("Thành tiền: " + df.format(item.getPrice() * item.getQuantity() / 1000));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class OrderDetailViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        TextView tvName;
        TextView tvUnitPrice;
        TextView tvQuantity;
        TextView tvTotalPrice;

        public OrderDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvName = itemView.findViewById(R.id.tvName);
            tvUnitPrice = itemView.findViewById(R.id.tvUnitPrice);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
        }
    }
}
