package com.ssoftwares.appmaker.api;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.ssoftwares.appmaker.activities.MainActivity;
import com.ssoftwares.appmaker.modals.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommonApis {
    ApiService service;
    Context context;
    onSearchResult onSearchResult;

    public CommonApis(ApiService service, Context context, onSearchResult onSearchResult) {
        this.service = service;
        this.context = context;
        this.onSearchResult = onSearchResult;
    }

    public interface onSearchResult {
        void onSuccess(JsonObject result);

        void onFailed(String message);

    }

    public void search(String query, String filterType, String fieldName, String where) {
        String url = ApiClient.BASE_URL;
        String value = url + "/" + where + "?" + fieldName + "_" + filterType + "=" + query;
        service.filterProducts(value).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "" + response.body().size() +
                            " results founds", Toast.LENGTH_SHORT).show();
                    onSearchResult.onSuccess(response.body());
                } else
                    Toast.makeText(context, "Failed to fetch data", Toast.LENGTH_SHORT).show();
                onSearchResult.onFailed("Failed to fetch data");

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                onSearchResult.onFailed("Failed : Check you internet connection");
                Toast.makeText(context, "Failed : Check you internet connection", Toast.LENGTH_SHORT).show();

            }
        });
    }
}
