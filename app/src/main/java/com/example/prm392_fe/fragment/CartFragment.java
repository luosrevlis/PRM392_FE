package com.example.prm392_fe.fragment;

import static com.example.prm392_fe.api.APIClient.getClient;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prm392_fe.R;
import com.example.prm392_fe.adapter.CartItemAdapter;
import com.example.prm392_fe.api.OrderService;
import com.example.prm392_fe.model.Cart;
import com.example.prm392_fe.model.CartItem;
import com.example.prm392_fe.model.CreateOrderDetail;
import com.example.prm392_fe.model.CreateOrderRequest;
import com.example.prm392_fe.model.CreateOrderResponse;
import com.example.prm392_fe.model.EmptyResponse;
import com.example.prm392_fe.model.UpdateTransactionRequest;
import com.example.prm392_fe.zalo.Api.CreateOrder;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

import lombok.Setter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.listeners.PayOrderListener;

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
    AppCompatButton btnCheckout;
    OrderService orderService;
    int orderId;
    @Setter OnTransactionCompletedListener onTransactionCompletedListener;

    public CartFragment() {
        // Required empty public constructor
    }

    public interface OnTransactionCompletedListener {
        void onTransactionCompleted();
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
        orderService = getClient(getActivity()).create(OrderService.class);

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
        btnCheckout = rootview.findViewById(R.id.btnCheckout);

        updateSubtotal();

        //ZALOPAY INIT
        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        ZaloPaySDK.init(2553, Environment.SANDBOX);

        btnCheckout.setOnClickListener(v -> {
            createOrder();
            startZaloPayTransaction();
        });

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

    private void createOrder() {
        ArrayList<CreateOrderDetail> orderDetails = new ArrayList<>();
        for (CartItem item : cart.getItems()) {
            orderDetails.add(new CreateOrderDetail(item.getDishId(), item.getQuantity()));
        }
        CreateOrderRequest request = new CreateOrderRequest();
        request.setDishes(orderDetails.toArray(new CreateOrderDetail[cart.getItems().size()]));
        Call<CreateOrderResponse> call = orderService.createOrder(request);
        call.enqueue(new Callback<CreateOrderResponse>() {
            @Override
            public void onResponse(Call<CreateOrderResponse> call, Response<CreateOrderResponse> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    Toast.makeText(getActivity(), "Tạo đơn hàng thất bại. Hãy thử lại sau.", Toast.LENGTH_SHORT).show();
                    Log.e("CartFragment", "Create order call failed. Server response unsuccessful");
                    return;
                }
                Toast.makeText(getActivity(), "Tạo đơn hàng thành công.", Toast.LENGTH_SHORT).show();
                orderId = response.body().getResult().getOrderID();
            }

            @Override
            public void onFailure(Call<CreateOrderResponse> call, Throwable throwable) {
                Toast.makeText(getActivity(), "Lỗi kết nối. Hãy thử lại sau.", Toast.LENGTH_SHORT).show();
                Log.e("CartFragment", "Create order call failed", throwable);
            }
        });
    }

    private void startZaloPayTransaction() {
        CreateOrder orderApi = new CreateOrder();
        try {
            JSONObject data = orderApi.createOrder(String.valueOf((int) cart.getSubtotal()));
            String code = data.getString("return_code");
            if (code.equals("1")) {
                String token = data.getString("zp_trans_token");
                ZaloPaySDK.getInstance().payOrder(requireActivity(), token, "demozdpk://app", new PayOrderListener() {
                    @Override
                    public void onPaymentSucceeded(String transactionId, String transToken, String appTransID) {
                        callUpdateTransaction();
                    }

                    @Override
                    public void onPaymentCanceled(String zpTransToken, String appTransID) {
                        Toast.makeText(getActivity(), "Yêu cầu thanh toán bị hủy.", Toast.LENGTH_SHORT).show();
                        Log.e("CartFragment", "Payment canceled");
                    }

                    @Override
                    public void onPaymentError(ZaloPayError zaloPayError, String zpTransToken, String appTransID) {
                        Toast.makeText(getActivity(), "Lỗi xảy ra khi thanh toán. Hãy thử lại sau.", Toast.LENGTH_SHORT).show();
                        Log.e("CartFragment", "Payment call failed");
                    }
                });
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void callUpdateTransaction() {
        UpdateTransactionRequest request = new UpdateTransactionRequest();
        request.setBankCode("zalopay");
        Call<EmptyResponse> call = orderService.updateTransaction(orderId, request);
        call.enqueue(new Callback<EmptyResponse>() {
            @Override
            public void onResponse(Call<EmptyResponse> call, Response<EmptyResponse> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    Toast.makeText(getActivity(), "Cập nhật tình trạng đơn hàng thất bại. Hãy thử lại sau.", Toast.LENGTH_SHORT).show();
                    Log.e("CartFragment", "Update transaction call failed. Server response unsuccessful");
                    return;
                }
                Toast.makeText(getActivity(), "Thanh toán đơn hàng thành công.", Toast.LENGTH_SHORT).show();
                onTransactionCompletedListener.onTransactionCompleted();
            }

            @Override
            public void onFailure(Call<EmptyResponse> call, Throwable throwable) {
                Toast.makeText(getActivity(), "Lỗi kết nối. Hãy thử lại sau.", Toast.LENGTH_SHORT).show();
                Log.e("CartFragment", "Update transaction call failed", throwable);
            }
        });
    }
}