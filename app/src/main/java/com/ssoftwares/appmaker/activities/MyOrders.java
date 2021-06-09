package com.ssoftwares.appmaker.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.ssoftwares.appmaker.R;
import com.ssoftwares.appmaker.api.ApiClient;
import com.ssoftwares.appmaker.api.ApiService;
import com.ssoftwares.appmaker.utils.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyOrders extends AppCompatActivity {


    private static final String TAG = "MyOrders" ;
    SessionManager sessionManager;
    ApiService service;
    private List<String> ignore = Arrays.asList("cpanels", "adminpanels");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);

        getSupportActionBar().setTitle("My Orders");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sessionManager = new SessionManager(this);
        service = ApiClient.create();

        getOrders();
    }

    private void getOrders() {
        service.getUserOrders(sessionManager.getToken() , sessionManager.getUserId())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.code() != 200){
                            Toast.makeText(MyOrders.this , response.message() , Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Log.v(TAG , response.body().toString());
                        try {
                            JSONObject res = new JSONObject(response.body().string());
                            while (res.keys().hasNext()){
                                String key = res.keys().next();
                                Object json = res.get(key);
                                if (json instanceof JSONArray){
                                    JSONArray productArray = (JSONArray) json;
                                    if (!ignore.contains(key) && productArray.length() != 0){
                                        Log.v("TAG" , key + " is an array list with multiple orders");

                                    }
                                }
                            }
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
    }
}