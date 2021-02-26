package com.ssoftwares.appmaker.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ssoftwares.appmaker.modals.Banner;
import com.ssoftwares.appmaker.modals.Category;
import com.ssoftwares.appmaker.modals.Cpanel;
import com.ssoftwares.appmaker.modals.Demo;
import com.ssoftwares.appmaker.modals.Order;
import com.ssoftwares.appmaker.modals.Product;
import com.ssoftwares.appmaker.modals.SubProduct;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
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
import retrofit2.http.QueryName;
import retrofit2.http.Url;


public interface ApiService {

    @FormUrlEncoded
    @POST("auth/local")
    Call<JsonObject> login(
            @Field("identifier") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("auth/local/register")
    Call<JsonObject> register(
            @Field("username") String username,
            @Field("email") String email,
            @Field("password") String password,
            @Field("confirmed") boolean confirmed,
            @Field("blocked") boolean blocked,
            @Field("phone") String phone,
            @Field("company_name") String company_name

    );

    @Multipart
    @POST("{endpoint}")
    Call<JsonObject> createEntry(
            @Header("Authorization") String token,
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

    @GET
    Call<ArrayList<Product>> filterProducts(
            @Url String url

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

    @GET("static-pages")
    Call<JsonObject> getStatic_Pages(

    );

    @GET("settings")
    Call<JsonObject> getSettings(
    );

    @GET("cpanels")
    Call<List<Cpanel>> getCpanels(
            @Header("Authorization") String token,
            @Query("user_id") String user_id,
            @Query("_sort") String sort
    );

    @GET("cpanels/{id}")
    Call<Cpanel> getSingleCpanel(
            @Header("Authorization") String token,
            @Path("id") String id
    );

    @Multipart
    @POST("orders")
    Call<ResponseBody> createOrder(
            @Header("Authorization") String token,
            @Part("data") RequestBody data,
            @Part MultipartBody.Part config,
            @Part MultipartBody.Part orderImage
    );

    @GET("orders")
    Call<List<Order>> getOrders(
            @Header("Authorization") String token,
            @Query("_sort") String sort
    );

    @GET
    Call<JsonObject> fetchJson(
            @Url String url
    );
}
