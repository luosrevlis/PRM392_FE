package com.example.prm392_fe.fragment;

import static com.example.prm392_fe.api.APIClient.getClient;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prm392_fe.R;
import com.example.prm392_fe.adapter.DishBannerAdapter;
import com.example.prm392_fe.adapter.HorizontalMarginItemDecoration;
import com.example.prm392_fe.api.DishService;
import com.example.prm392_fe.api.UserService;
import com.example.prm392_fe.model.Cart;
import com.example.prm392_fe.model.CartItem;
import com.example.prm392_fe.model.Dish;
import com.example.prm392_fe.model.ListDishResponse;
import com.example.prm392_fe.model.PagedListDishResponse;
import com.example.prm392_fe.model.RandomDishResponse;
import com.example.prm392_fe.model.UserResponse;

import java.time.LocalTime;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    private static final String ARG_CART = "cartObject";
    private Cart cart;

    //private ArrayList<Dish> newDishes = new ArrayList<>();
    //private ArrayList<Dish> suggestionDishes = new ArrayList<>();
    private DishBannerAdapter newDishBannerAdapter;
    private DishBannerAdapter suggestionDishBannerAdapter;
    private TextView greetingText;
    private TextView suggestionText;
    private ViewPager2 suggestionBanner;

    private AppCompatButton btnSearch;
    public HomeFragment() {}

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param cart Parameter 1.
     * @return A new instance of fragment HomeFragment.
     */
    public static HomeFragment newInstance(Cart cart) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_CART, cart);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            cart = (Cart)getArguments().getSerializable(ARG_CART);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        greetingText = rootView.findViewById(R.id.tvGreetingMessage);
        suggestionText = rootView.findViewById(R.id.tvFoodSuggestion);
        btnSearch = rootView.findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(view -> {
            FragmentManager fragmentManager = getParentFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.clFragment, SearchFragment.newInstance(cart))
                    .addToBackStack(null)
                    .commit();
        });

        setGreetingMessage();
        suggestionBanner = rootView.findViewById(R.id.foodSuggestionBanner);
        suggestionDishBannerAdapter = new DishBannerAdapter(new ArrayList<>(),getContext(),this::onSuggestionAddToCartClick);
        suggestionBanner.setAdapter(suggestionDishBannerAdapter);
        suggestionBanner.setOffscreenPageLimit(1);


        ViewPager2 newFoodBanner = rootView.findViewById(R.id.foodNewBanner);
        newDishBannerAdapter = new DishBannerAdapter(new ArrayList<>(),getContext(),this::onNewDishesAddToCartClick);
        newFoodBanner.setAdapter(newDishBannerAdapter);
        newFoodBanner.setOffscreenPageLimit(1);

        int nextItemVisibleInPx = 26;
        int currentItemHorizontalMargin = 42;
        int tranX = (int) (nextItemVisibleInPx + currentItemHorizontalMargin);
        ViewPager2.PageTransformer pageTransformer = new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                page.setTranslationX(-tranX*position);
                page.setScaleY(1-(0.25f * Math.abs(position)));
            }
        };
        HorizontalMarginItemDecoration horizontalMarginItemDecoration = new
                HorizontalMarginItemDecoration(getContext(), currentItemHorizontalMargin);

        newFoodBanner.addItemDecoration(horizontalMarginItemDecoration);
        newFoodBanner.setPageTransformer(pageTransformer);
        suggestionBanner.addItemDecoration(horizontalMarginItemDecoration);
        suggestionBanner.setPageTransformer(pageTransformer);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int meal;
            int currentHour = LocalTime.now().getHour();
            if (currentHour < 9) {
                meal = 0;
                suggestionText.setText("Sáng nay ăn gì");
            } else if (currentHour < 14) {
                meal = 1;
                suggestionText.setText("Trưa nay ăn gì");
            } else {
                meal = 2;
                suggestionText.setText("Tối nay ăn gì");
            }
                setNewDish();
                setSuggestionDish(meal);
        }
        return rootView;
    }
    private void setNewDish(){
        DishService dishService = getClient(getContext()).create(DishService.class);
        Call<ListDishResponse> responseCall = dishService.getNewestDishes();
        responseCall.enqueue(new Callback<ListDishResponse>() {
            @Override
            public void onResponse(Call<ListDishResponse> call, Response<ListDishResponse> response) {
                ListDishResponse body = response.body();
                if (body == null) {
                Toast.makeText(getActivity(), "Lỗi server. Hãy thử lại sau.", Toast.LENGTH_SHORT).show();
                Log.e("RandomFragment", "Response body is null");
                return;
                }
                newDishBannerAdapter.updateItem(body.getResult());
            }
            @Override
            public void onFailure(Call<ListDishResponse> call, Throwable throwable) {
                Toast.makeText(getActivity(), "Lỗi kết nối. Hãy thử lại sau.", Toast.LENGTH_SHORT).show();
                Log.e("RandomFragment", "Random call failed", throwable);
            }
        });
    }
    private void setSuggestionDish(int meal){
        DishService dishService = getClient(getContext()).create(DishService.class);
        Call<PagedListDishResponse> responseCall = dishService.getDishesByFilter(1, null, null, null, meal, null, null);
        responseCall.enqueue(new Callback<PagedListDishResponse>() {
            @Override
            public void onResponse(Call<PagedListDishResponse> call, Response<PagedListDishResponse> response) {
                PagedListDishResponse body = response.body();
                if (body == null) {
                    Toast.makeText(getActivity(), "Lỗi server. Hãy thử lại sau.", Toast.LENGTH_SHORT).show();
                    Log.e("RandomFragment", "Response body is null");
                    return;
                }
                suggestionDishBannerAdapter.updateItem(body.getResult().getItems());
            }
            @Override
            public void onFailure(Call<PagedListDishResponse> call, Throwable throwable) {
                Toast.makeText(getActivity(), "Lỗi kết nối. Hãy thử lại sau.", Toast.LENGTH_SHORT).show();
                Log.e("RandomFragment", "Random call failed", throwable);
            }
        });
    }
    private void setGreetingMessage(){
        UserService userService = getClient(getContext()).create(UserService.class);
        Call<UserResponse> responseCall = userService.getCurrentUser();
        responseCall.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                UserResponse body = response.body();
                if (body == null) {
                    Toast.makeText(getActivity(), "Lỗi server. Hãy thử lại sau.", Toast.LENGTH_SHORT).show();
                    Log.e("RandomFragment", "Response body is null");
                    return;
                }
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    int currentHour = LocalTime.now().getHour();
                    String greetingMessage;
                    if (currentHour < 11) {
                        greetingMessage = "Chào buổi sáng, ";
                    } else if (currentHour < 16){
                        greetingMessage = "Chào buổi trưa, ";
                    } else {
                        greetingMessage = "Chào buổi tối, ";
                    }
                    greetingMessage += body.getResult().getFullName();
                    greetingText.setText(greetingMessage);
                }
            }
            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(getActivity(), "Lỗi kết nối. Hãy thử lại sau.", Toast.LENGTH_SHORT).show();
                Log.e("RandomFragment", "Random call failed", t);
            }
        });
    }
    private void onSuggestionAddToCartClick(int position){
        Dish selectedDish = suggestionDishBannerAdapter.getItemList().get(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Thêm vào giỏ hàng");
        builder.setMessage("Bạn có muốn thêm 1 "+selectedDish.getName()+ " vào giỏ hàng?");
        builder.setPositiveButton("Có", (dialog, which) -> {
            CartItem cartItem = new CartItem(selectedDish.getDishID(), 1, selectedDish);
            for (CartItem currentItem : cart.getItems()) {
                if (currentItem.getDishId() == (selectedDish.getDishID())){
                    currentItem.setQuantity(currentItem.getQuantity()+1);
                    Toast.makeText(getActivity(), "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            cart.getItems().add(cartItem);
            Toast.makeText(getActivity(), "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();});
        builder.setNegativeButton("Không", (dialog, which) -> dialog.dismiss());
        builder.show();
    }
    private void onNewDishesAddToCartClick(int position){
        Dish selectedDish = newDishBannerAdapter.getItemList().get(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Thêm vào giỏ hàng");
        builder.setMessage("Bạn có muốn thêm 1 "+selectedDish.getName()+ " vào giỏ hàng?");
        builder.setPositiveButton("Có", (dialog, which) -> {
            CartItem cartItem = new CartItem(selectedDish.getDishID(), 1, selectedDish);
            for (CartItem currentItem : cart.getItems()) {
                if (currentItem.getDishId() == selectedDish.getDishID()){
                    currentItem.setQuantity(currentItem.getQuantity()+1);
                    Toast.makeText(getActivity(), "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            cart.getItems().add(cartItem);
            Toast.makeText(getActivity(), "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();});
        builder.setNegativeButton("Không", (dialog, which) -> dialog.dismiss());
        builder.show();
    }
}