package com.example.prm392_fe.fragment;

import static com.example.prm392_fe.api.APIClient.getClient;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.prm392_fe.R;
import com.example.prm392_fe.activity.LoginActivity;
import com.example.prm392_fe.activity.MainActivity;
import com.example.prm392_fe.activity.UserProfileActivity;
import com.example.prm392_fe.api.DishService;
import com.example.prm392_fe.api.SettingService;
import com.example.prm392_fe.model.UserInfo;
import com.example.prm392_fe.model.UserInfoResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private SharedPreferences sharedPreferences;
    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    TextView userName;
    SettingService settingService;
    Button logout;
    RelativeLayout info;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    private void getCurrentUser(){
            Call<UserInfoResponse> call = settingService.getCurrent();

            call.enqueue(new Callback<UserInfoResponse>() {
                @Override
                public void onResponse(Call<UserInfoResponse> call, Response<UserInfoResponse> response) {
                    UserInfoResponse body = response.body();
                    if (body == null) {
                        Toast.makeText(getActivity(), "Lỗi server. Hãy thử lại sau.", Toast.LENGTH_SHORT).show();
                        Log.e("SettingsFragment", "Response body is null");
                        return;
                    }

                    UserInfo userInfo = body.getResult();
//                    Log.d("SettingsFragment", "User id: " + userInfo.getAccountID());
//                    Log.d("SettingsFragment", "User address: " + userInfo.getAddress());
//                    Log.d("SettingsFragment", "User name: " + userInfo.getFullName());
//                    Log.d("SettingsFragment", "User email: " + userInfo.getEmail());
                    userName.setText(userInfo.getFullName());


                }

                @Override
                public void onFailure(Call<UserInfoResponse> call, Throwable throwable) {
                    Toast.makeText(getActivity(), "Lỗi kết nối. Hãy thử lại sau.", Toast.LENGTH_SHORT).show();
                    Log.e("SettingsFragment", "User call failed", throwable);
                }
            });

    }
    private void performLogout() {
        deleteToken();
        navigateToLogin();
    }
    private void deleteToken() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("token"); // Remove the key "token"
        editor.apply();
    }
    private void navigateToLogin() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_settings, container, false);
        sharedPreferences = getActivity().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        settingService = getClient(getContext()).create(SettingService.class);
        userName = rootview.findViewById(R.id.username);
        logout = rootview.findViewById(R.id.logout_button);
        info = rootview.findViewById(R.id.information);
        getCurrentUser();
        logout.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(700).start();
        logout.setOnClickListener(v -> performLogout());
        info.setOnClickListener(v -> navigateToUserProfile());
        return rootview;
    }

    public void navigateToUserProfile() {
        Intent intent = new Intent(getActivity(), UserProfileActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

}