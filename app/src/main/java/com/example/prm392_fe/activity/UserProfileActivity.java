package com.example.prm392_fe.activity;

import static com.example.prm392_fe.api.APIClient.getClient;

import static java.security.AccessController.getContext;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.prm392_fe.R;
import com.example.prm392_fe.api.SettingService;
import com.example.prm392_fe.fragment.SettingsFragment;
import com.example.prm392_fe.model.EditAccountRequest;
import com.example.prm392_fe.model.UserInfo;
import com.example.prm392_fe.model.UserInfoResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserProfileActivity extends AppCompatActivity {
    TextView save, email;
    ImageView back;
    EditText name,address;
    private SettingService settingService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_profile);
        settingService = getClient(this).create(SettingService.class);
        save = findViewById(R.id.save);
        back = findViewById(R.id.button_back);
        name = findViewById(R.id.username);
        address = findViewById(R.id.address);
        email = findViewById(R.id.email);

        save.setOnClickListener(v -> save());
        back.setOnClickListener(v -> back());
        getCurrentUser();
    }
    private void getCurrentUser(){
        Call<UserInfoResponse> call = settingService.getCurrent();

        call.enqueue(new Callback<UserInfoResponse>() {
            @Override
            public void onResponse(Call<UserInfoResponse> call, Response<UserInfoResponse> response) {
                UserInfoResponse body = response.body();
                if (body == null) {
                    Toast.makeText(UserProfileActivity.this, "Lỗi server. Hãy thử lại sau.", Toast.LENGTH_SHORT).show();
                    Log.e("UserProfileActivity", "Response body is null");
                    return;
                }

                UserInfo userInfo = body.getResult();
                name.setText(userInfo.getFullName());
                email.setText(userInfo.getEmail());
                address.setText(userInfo.getAddress());


            }

            @Override
            public void onFailure(Call<UserInfoResponse> call, Throwable throwable) {
                Toast.makeText(UserProfileActivity.this, "Lỗi kết nối. Hãy thử lại sau.", Toast.LENGTH_SHORT).show();
                Log.e("UserProfileActivity", "User call failed", throwable);
            }
        });

    }
    private void save() {
        String user = this.name.getText().toString();
        String add = this.address.getText().toString();

        EditAccountRequest editRequest = new EditAccountRequest(user, add);

        Call<UserInfoResponse> call = settingService.updateAccount(editRequest);
        call.enqueue(new Callback<UserInfoResponse>() {
            @Override
            public void onResponse(Call<UserInfoResponse> call, Response<UserInfoResponse> response) {
                if (response.isSuccessful()) {
                    UserInfoResponse body = response.body();
                    if (body != null && body.getStatusCode() == 200) {
                        UserInfo userInfo = body.getResult();
                        name.setText(userInfo.getFullName());
                        address.setText(userInfo.getAddress());
                        Toast.makeText(UserProfileActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                        back();
                    }
                    else{
                        Toast.makeText(UserProfileActivity.this, "Lỗi server. Hãy thử lại sau.", Toast.LENGTH_SHORT).show();
                        Log.e("UserProfileActivity", "Response body is null");
                        return;
                    }
                }
                else{
                    Toast.makeText(UserProfileActivity.this, "Lỗi server. Hãy thử lại sau.", Toast.LENGTH_SHORT).show();
                    Log.e("UserProfileActivity", "Response is null");
                    return;
                }

            }
            @Override
            public void onFailure(Call<UserInfoResponse> call, Throwable throwable) {
                Toast.makeText(UserProfileActivity.this, "Lỗi server. Hãy thử lại sau.", Toast.LENGTH_SHORT).show();
                Log.e("UserProfileActivity", "Response is null");
            }
        });
    }
    private void back() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("NAVIGATE_TO_SETTINGS", true);
        startActivity(intent);
        this.finish();
    }
}