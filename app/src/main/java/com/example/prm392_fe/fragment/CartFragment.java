package com.example.prm392_fe.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.prm392_fe.R;
import com.example.prm392_fe.adapter.CartItemAdapter;
import com.example.prm392_fe.model.Cart;
import com.example.prm392_fe.model.CartItem;

import java.text.DecimalFormat;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CartFragment extends Fragment {
    private static final String ARG_CART = "cart";
    private Cart cart;
    CartItemAdapter cartItemAdapter;
    RecyclerView rvDishes;
    DecimalFormat df;
    TextView tvSubtotalValue;

    public CartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param cart Cart Object.
     * @return A new instance of fragment CartFragment.
     */
    public static CartFragment newInstance(Cart cart) {
        CartFragment fragment = new CartFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_CART, cart);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            cart = (Cart) getArguments().getSerializable(ARG_CART);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_cart, container, false);

        cartItemAdapter = new CartItemAdapter(getActivity(), cart.getItems());
        cartItemAdapter.setIncListener(position -> updateSubtotal());
        cartItemAdapter.setDecListener(position -> updateSubtotal());
        cartItemAdapter.setQuantityListener(position -> updateSubtotal());
        cartItemAdapter.setCloseListener(position -> updateSubtotal());

        rvDishes = rootview.findViewById(R.id.rvDishes);
        rvDishes.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvDishes.setAdapter(cartItemAdapter);

        df = new DecimalFormat("##,###.#k");
        tvSubtotalValue = rootview.findViewById(R.id.tvSubtotalValue);
        updateSubtotal();

        return rootview;
    }

    private void updateSubtotal() {
        double subtotal = 0;
        for (CartItem item: cart.getItems()) {
            subtotal += item.getDish().getPrice() * item.getQuantity();
        }
        cart.setSubtotal(subtotal);
        tvSubtotalValue.setText(df.format(subtotal / 1000));
    }
}