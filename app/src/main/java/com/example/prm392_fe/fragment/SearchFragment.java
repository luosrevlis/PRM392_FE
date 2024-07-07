package com.example.prm392_fe.fragment;

import static com.example.prm392_fe.api.APIClient.getClient;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.prm392_fe.R;
import com.example.prm392_fe.adapter.DishSearchAdapter;
import com.example.prm392_fe.api.DishService;
import com.example.prm392_fe.model.Cart;
import com.example.prm392_fe.model.CartItem;
import com.example.prm392_fe.model.Dish;
import com.example.prm392_fe.model.PagedListDish;
import com.example.prm392_fe.model.PagedListDishResponse;

import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {
    private AppCompatImageButton btnReturn, btnFilter, btnSearch;
    private EditText edFoodName;
    private RecyclerView searchList;
    private DishSearchAdapter dishSearchAdapter = new DishSearchAdapter(new ArrayList<>(),getContext(),this::onAddToCartClick);

    @Nullable
    private Integer meal;
    @Nullable
    private Double minPrice;
    @Nullable
    private Double maxPrice;
    @Nullable
    private String name;
    private int currentPage;
    private static final int pageSize = 5;
    private int totalPage;

    private static final String ARG_CART = "cartObject";
    private Cart cart;

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param cart Parameter 1.
     * @return A new instance of fragment SearchFragment.
     */
    public static SearchFragment newInstance(Cart cart) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_CART,cart);
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
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        btnReturn = rootView.findViewById(R.id.btnBack);
        btnFilter = rootView.findViewById(R.id.btnFilter);
        btnSearch = rootView.findViewById(R.id.btnSearch);
        edFoodName = rootView.findViewById(R.id.edFoodName);
        searchList = rootView.findViewById(R.id.searchList);

        searchList.setLayoutManager(new LinearLayoutManager(getContext()));
        searchList.setAdapter(dishSearchAdapter);

        searchList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager llm = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (llm != null && llm.findLastCompletelyVisibleItemPosition() >= dishSearchAdapter.getItemCount() - 2 && currentPage < totalPage) {
                    currentPage++;
                    setSearchList(currentPage);
                }
            }
        });

        btnReturn.setOnClickListener(view -> {
            FragmentManager fragmentManager = getParentFragmentManager();
            fragmentManager.popBackStackImmediate();
        });
        btnFilter.setOnClickListener(view -> {
            showFilterDialog();
        });
        btnSearch.setOnClickListener(view -> {
            name = edFoodName.getText().toString();
            if (name.isEmpty()) {
                name = null;
            }
            setSearchList(1);
        });
        return rootView;
    }
    private void setSearchList(int pageNumber){
        DishService dishService = getClient(getContext()).create(DishService.class);
        Call<PagedListDishResponse> responseCall = dishService.getDishesByFilter(1,name, maxPrice, minPrice, meal, pageNumber, pageSize);
        responseCall.enqueue(new Callback<PagedListDishResponse>() {
            @Override
            public void onResponse(Call<PagedListDishResponse> call, Response<PagedListDishResponse> response) {
                PagedListDishResponse body = response.body();
                if (body == null) {
                    Toast.makeText(getActivity(), "Lỗi server. Hãy thử lại sau.", Toast.LENGTH_SHORT).show();
                    Log.e("RandomFragment", "Response body is null");
                    return;
                }
                PagedListDish pagedListDish = body.getResult();
                currentPage = pagedListDish.getPageNumber();
                totalPage = pagedListDish.getTotalPage();
                if (pagedListDish.getPageNumber()==1) {
                    dishSearchAdapter.updateItemList(pagedListDish.getItems());
                } else {
                    dishSearchAdapter.addNewDishes(pagedListDish.getItems());
                }
            }

            @Override
            public void onFailure(Call<PagedListDishResponse> call, Throwable throwable) {
                Toast.makeText(getActivity(), "Lỗi kết nối. Hãy thử lại sau.", Toast.LENGTH_SHORT).show();
                Log.e("RandomFragment", "Random call failed", throwable);
            }
        });
    }
    private void showFilterDialog(){
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_search_filter, null);
        EditText edMinPrice = dialogView.findViewById(R.id.edMinPrice);
        EditText edMaxPrice = dialogView.findViewById(R.id.edMaxPrice);
        Spinner mealPicker = dialogView.findViewById(R.id.spMealPicker);
        edMinPrice.setText(minPrice == null ? "" : String.format(Locale.US, "%.0f", minPrice));
        edMaxPrice.setText(maxPrice == null ? "" : String.format(Locale.US, "%.0f", maxPrice));
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(),R.layout.item_spinner_text, new String[] {"Bữa sáng", "Bữa trưa", "Bữa tối","Tất cả"});
        mealPicker.setAdapter(arrayAdapter);
        mealPicker.setSelection( meal == null ? 3 : meal);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
            .setTitle("Bộ lọc").setView(dialogView)
            .setPositiveButton("Lọc",null)// overrided method below will be called instead
            .setNegativeButton("Hủy",(dialog, which) -> {
                dialog.dismiss();
            });
        AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(dialog -> {
            Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
            button.setOnClickListener(v -> {
                try {
                    if (edMinPrice.getText().toString().isEmpty()){
                        minPrice = null;
                    } else {
                        minPrice = Double.parseDouble(edMinPrice.getText().toString());
                    }
                    if (edMaxPrice.getText().toString().isEmpty()){
                        maxPrice = null;
                    } else {
                        maxPrice = Double.parseDouble(edMaxPrice.getText().toString());
                    }
                    meal = mealPicker.getSelectedItemPosition();
                    if (meal >= 3 || meal < 0) {
                        meal = null;
                    }
                    setSearchList(1);
                    dialog.dismiss();
                } catch (NumberFormatException e) {
                    minPrice = null;
                    maxPrice = null;
                    Toast.makeText(getActivity(), "Giá trị không hợp lệ, vui lòng nhập lại", Toast.LENGTH_SHORT).show();
                }
            });
        });
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }
    private void onAddToCartClick(int position){
        Dish selectedDish = dishSearchAdapter.getItemList().get(position);
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