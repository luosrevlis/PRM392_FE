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
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_fe.R;
import com.example.prm392_fe.model.CartItem;
import com.example.prm392_fe.model.Dish;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.CartItemViewHolder> {
    Context context;
    ArrayList<CartItem> items;
    OnItemClickListener incListener;
    OnItemClickListener decListener;
    OnItemClickListener quantityListener;

    public CartItemAdapter(Context context, ArrayList<CartItem> items) {
        this.context = context;
        this.items = items;
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
        holder.tvUnitPrice.setText(formatAmount(dish.getPrice() / 1000));
        holder.tvTotalPrice.setText(formatAmount(dish.getPrice() * item.getQuantity() / 1000));
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
                if (quantity < 0) {
                    quantity = 0;
                    holder.etQuantity.setText("0");
                }
                if (quantity > 99) {
                    quantity = 99;
                    holder.etQuantity.setText("99");
                }
                item.setQuantity(quantity);
                holder.tvTotalPrice.setText(formatAmount(dish.getPrice() * item.getQuantity() / 1000));
                quantityListener.onItemClick(holder.getAdapterPosition());
            }
        });

        holder.btnInc.setOnClickListener(v -> {
            item.setQuantity(item.getQuantity() + 1);
            holder.etQuantity.setText("" + item.getQuantity());
            holder.tvTotalPrice.setText(formatAmount(dish.getPrice() * item.getQuantity() / 1000));
            incListener.onItemClick(holder.getAdapterPosition());
        });

        holder.btnDec.setOnClickListener(v -> {
            if (item.getQuantity() <= 0) {
                return;
            }
            item.setQuantity(item.getQuantity() - 1);
            holder.etQuantity.setText("" + item.getQuantity());
            holder.tvTotalPrice.setText(formatAmount(dish.getPrice() * item.getQuantity() / 1000));
            decListener.onItemClick(holder.getAdapterPosition());
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private String formatAmount(double amount) {
        return String.format(Locale.ENGLISH, "%.1fk", amount);
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public class CartItemViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        TextView tvName;
        TextView tvUnitPrice;
        TextView tvTotalPrice;
        EditText etQuantity;
        Button btnInc;
        Button btnDec;

        public CartItemViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvName = itemView.findViewById(R.id.tvName);
            tvUnitPrice = itemView.findViewById(R.id.tvUnitPrice);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
            etQuantity = itemView.findViewById(R.id.etQuantity);
            btnInc = itemView.findViewById(R.id.btnInc);
            btnDec = itemView.findViewById(R.id.btnDec);
        }
    }
}
