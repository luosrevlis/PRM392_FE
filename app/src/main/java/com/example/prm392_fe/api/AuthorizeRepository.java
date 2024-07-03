package com.example.prm392_fe.api;

import static com.example.prm392_fe.api.APIClient.getClient;

import android.content.Context;

public class AuthorizeRepository {
    public static AuthorizeService getAPIService(Context context) {
        return getClient(context).create(AuthorizeService.class);
    }
}
