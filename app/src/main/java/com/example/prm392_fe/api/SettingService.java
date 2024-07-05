package com.example.prm392_fe.api;




import com.example.prm392_fe.model.UserInfoResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface SettingService {
    String USER_SETTING = "/api/Account";

    @GET(USER_SETTING + "/current")
    Call<UserInfoResponse> getCurrent();

}
