package com.example.prm392_fe.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
public class DishSearchAdapter extends RecyclerView.Adapter<DishSearchAdapter.DishViewHolder> {
    private ArrayList<Dish> dishes;
    private Context context;
    private OnAddToCartClickListener listener;
    @NonNull
    @Override
    public DishViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DishViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search, parent, false));
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
    public void updateItemList(ArrayList<Dish> newDishes){
        this.dishes = newDishes;
        notifyDataSetChanged();
    }
    public void addNewDishes(ArrayList<Dish> newDishes){
        this.dishes.addAll(newDishes);
        notifyItemRangeInserted(dishes.size() - newDishes.size(), dishes.size()-1);
    }
    public ArrayList<Dish> getItemList() {
        return dishes;
    }
    @Getter
    public class DishViewHolder extends RecyclerView.ViewHolder{
        private final ImageView foodImage;
        private final TextView foodName, foodPrice;
        private final AppCompatImageButton btnSearchAddToCart;
        public DishViewHolder(@NonNull View itemView) {
            super(itemView);
            foodImage = itemView.findViewById(R.id.imageView);
            foodName = itemView.findViewById(R.id.tvFoodName);
            foodPrice = itemView.findViewById(R.id.tvFoodPrice);
            btnSearchAddToCart = itemView.findViewById(R.id.btnSearchAddToCart);
        }
        public void bind(Dish dish, OnAddToCartClickListener listener,int position){
            btnSearchAddToCart.setOnClickListener(v->listener.onAddToCartClick(position));
        }
    }
    public interface OnAddToCartClickListener{
        void onAddToCartClick(int positition);
    }
}
