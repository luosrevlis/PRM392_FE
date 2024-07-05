package com.example.prm392_fe.adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.prm392_fe.R;
import com.example.prm392_fe.model.Dish;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import lombok.Getter;

public class DishBannerAdapter extends RecyclerView.Adapter<DishBannerAdapter.DishViewHolder> {
    private ArrayList<Dish> dishes;
    private Context context;
    public DishBannerAdapter(ArrayList<Dish> dishes, Context context) {
        this.dishes = dishes;
        this.context = context;
    }
    @NonNull
    @Override
    public DishViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DishViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_promotion, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DishViewHolder holder, int position) {
        Picasso
                .with(context)
                .load(dishes.get(position).getImageUrl())
                .placeholder(R.drawable.ic_loading)
                .error(R.drawable.ic_error_red)
                .into(holder.foodImage);
        holder.foodName.setText(dishes.get(position).getName());
        holder.foodPrice.setText(dishes.get(position).getPrice()+"vnd");
    }
    @Override
    public int getItemCount() {
        return 0;
    }

    public class DishViewHolder extends RecyclerView.ViewHolder {
        ImageView foodImage;
        TextView foodName, foodPrice;
        public DishViewHolder(@NonNull View itemView) {
            super(itemView);
            foodImage = itemView.findViewById(R.id.foodImage);
            foodName = itemView.findViewById(R.id.foodName);
            foodPrice = itemView.findViewById(R.id.foodPrice);
        }
    }
}