package com.example.prm392_fe.adapter;
import android.content.Context;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.RecyclerView;
import com.example.prm392_fe.R;
import com.example.prm392_fe.model.Dish;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

import lombok.AllArgsConstructor;
import lombok.Getter;
@AllArgsConstructor
public class DishBannerAdapter extends RecyclerView.Adapter<DishBannerAdapter.DishViewHolder> {
    private ArrayList<Dish> dishes;
    private Context context;
    private OnAddToCartClickListener listener;
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
                .into(holder.getFoodImage());
        holder.getFoodName().setText(dishes.get(position).getName());
        String foodPrice = String.format(Locale.US, "%,.0f vnđ", dishes.get(position).getPrice());
        holder.getFoodPrice().setText(foodPrice);
        holder.bind(dishes.get(position), listener, position);
    }
    @Override
    public int getItemCount() {
        return dishes.size();
    }
    public void updateItem(ArrayList<Dish> itemList){
        this.dishes = itemList;
        notifyDataSetChanged();
    }
    public ArrayList<Dish> getItemList() {
        return dishes;
    }

    @Getter
    public class DishViewHolder extends RecyclerView.ViewHolder {
        ImageView foodImage;
        TextView foodName, foodPrice;
        AppCompatImageButton btnAddToCart;
        public DishViewHolder(@NonNull View itemView) {
            super(itemView);
            foodImage = itemView.findViewById(R.id.foodImage);
            foodName = itemView.findViewById(R.id.foodName);
            foodPrice = itemView.findViewById(R.id.foodPrice);
            btnAddToCart = itemView.findViewById(R.id.btnPromotionAddToCart);
        }
        public void bind(Dish dish, OnAddToCartClickListener listener,int position){
            btnAddToCart.setOnClickListener(v->listener.onAddToCartClick(position));
        }
    }
    public interface OnAddToCartClickListener{
        void onAddToCartClick(int positition);
    }
}
