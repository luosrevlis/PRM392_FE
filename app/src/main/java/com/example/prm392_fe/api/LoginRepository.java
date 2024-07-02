package com.example.prm392_fe.api;

import static com.example.prm392_fe.api.APIClient.getClient;

import android.content.Context;

public class LoginRepository {
    public static LoginService getAPIService(Context context) {
        return getClient(context).create(LoginService.class);
    }
}
