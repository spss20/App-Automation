package com.ssoftwares.appmaker.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ssoftwares.appmaker.modals.Banner;
import com.ssoftwares.appmaker.modals.Category;
import com.ssoftwares.appmaker.modals.Cpanel;
import com.ssoftwares.appmaker.modals.Demo;
import com.ssoftwares.appmaker.modals.Product;
import com.ssoftwares.appmaker.modals.SubProduct;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface ApiService {

    @FormUrlEncoded
    @POST("auth/local")
    Call<JsonObject> login(
            @Field("identifier") String email,
            @Field("password") String password
    );

    @Headers({
            "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MiwiaWF0IjoxNjA5MzA3ODM4LCJleHAiOjE2MTE4OTk4Mzh9.3-jvv-nKTFbJvqxoSqIyNFJrECGAFHmhAhUc2WqR1Ho"
    })
    @Multipart
    @POST("{endpoint}")
    Call<JsonObject> createEntry(
            @Path("endpoint") String endpoint,
            @Part("data") RequestBody data,
            @Part List<MultipartBody.Part> files
            );

    @GET("banners")
    Call<List<Banner>> getBanners();

    @GET("categories")
    Call<List<Category>> getCategories();

    @GET("products")
    Call<List<Product>> getProducts(
            @Query("tag") String tag,
            @Query("categories") String id
    );

    @GET("products/{id}")
    Call<Product> getSingleProduct(
            @Path("id") String id
    );

    @GET("subproducts")
    Call<List<SubProduct>> getSubProducts(
            @Query("product") String product_id
    );

    @GET("demos")
    Call<List<Demo>> getDemos(
            @Query("product") String product_id
    );

    @GET("configs")
    Call<JsonArray> getConfig(
            @Query("name") String config_name
    );

    @GET("cpanels")
    Call<List<Cpanel>> getCpanels(
            @Header("Authorization") String token,
            @Query("user_id") String user_id
    );

}
