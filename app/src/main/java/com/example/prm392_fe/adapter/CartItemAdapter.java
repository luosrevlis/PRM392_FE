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
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.CartItemViewHolder> {
    Context context;
    ArrayList<CartItem> items;
    DecimalFormat df;
    OnItemClickListener incListener;
    OnItemClickListener decListener;
    OnItemClickListener quantityListener;
    OnItemClickListener closeListener;

    public CartItemAdapter(Context context, ArrayList<CartItem> items) {
        this.context = context;
        this.items = items;
        df = new DecimalFormat("##,###.#k");
    }

    @NonNull
    @Override
    public CartItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_item, parent, false);
        return new CartItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemViewHolder holder, int position) {
        CartItem item = items.get(position);
        Dish dish = item.getDish();
        Picasso
                .with(context)
                .load(dish.getImageUrl())
                .placeholder(R.drawable.ic_loading)
                .error(R.drawable.ic_error_red)
                .into(holder.ivImage);
        holder.tvName.setText(dish.getName());
        holder.tvUnitPrice.setText("Đơn giá: " + df.format(dish.getPrice() / 1000));
        holder.tvTotalPrice.setText(df.format(dish.getPrice() * item.getQuantity() / 1000));

        holder.etQuantity.setText("" + item.getQuantity());
        holder.etQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().isEmpty()) {
                    holder.etQuantity.setText("" + item.getQuantity());
                    return;
                }
                int quantity = Integer.parseInt(s.toString());
                if (quantity < 1) {
                    quantity = 1;
                    holder.etQuantity.setText("1");
                }
                if (quantity > 50) {
                    quantity = 50;
                    holder.etQuantity.setText("50");
                }
                item.setQuantity(quantity);
                holder.tvTotalPrice.setText(df.format(dish.getPrice() * item.getQuantity() / 1000));
                quantityListener.onItemClick(holder.getAdapterPosition());
            }
        });

        holder.btnInc.setOnClickListener(v -> {
            if (item.getQuantity() >= 50) {
                return;
            }
            item.setQuantity(item.getQuantity() + 1);
            holder.etQuantity.setText("" + item.getQuantity());
            holder.tvTotalPrice.setText(df.format(dish.getPrice() * item.getQuantity() / 1000));
            incListener.onItemClick(holder.getAdapterPosition());
        });

        holder.btnDec.setOnClickListener(v -> {
            if (item.getQuantity() <= 1) {
                return;
            }
            item.setQuantity(item.getQuantity() - 1);
            holder.etQuantity.setText("" + item.getQuantity());
            holder.tvTotalPrice.setText(df.format(dish.getPrice() * item.getQuantity() / 1000));
            decListener.onItemClick(holder.getAdapterPosition());
        });

        holder.ivClose.setOnClickListener(v -> {
            items.remove(holder.getAdapterPosition());
            notifyItemRemoved(holder.getAdapterPosition());
            closeListener.onItemClick(holder.getAdapterPosition());
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public class CartItemViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        ImageView ivClose;
        TextView tvName;
        TextView tvUnitPrice;
        TextView tvTotalPrice;
        EditText etQuantity;
        AppCompatButton btnInc;
        AppCompatButton btnDec;

        public CartItemViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivImage);
            ivClose = itemView.findViewById(R.id.ivClose);
            tvName = itemView.findViewById(R.id.tvName);
            tvUnitPrice = itemView.findViewById(R.id.tvUnitPrice);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
            etQuantity = itemView.findViewById(R.id.etQuantity);
            btnInc = itemView.findViewById(R.id.btnInc);
            btnDec = itemView.findViewById(R.id.btnDec);
        }
    }
}
