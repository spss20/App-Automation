package com.ssoftwares.appmaker.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ssoftwares.appmaker.modals.User;

import org.json.JSONException;
import org.json.JSONObject;

public class SessionManager {

    private Context mContext;
    private SharedPreferences preferences;

    public SessionManager(Context context){
        this.mContext = context;
        preferences = mContext.getSharedPreferences("AppAutomation" , Context.MODE_PRIVATE);
    }

    public void saveUser(JsonObject response){
        String token = response.get("jwt").getAsString();
        String user = response.get("user").getAsJsonObject().toString();

        SharedPreferences.Editor editor  = preferences.edit();
        editor.putString("token" , token);
        editor.putString("user" , user);
        editor.apply();
    }

    public User getUser(){
       String user = preferences.getString("user" , null);
       if (user != null){
           JsonParser parser = new JsonParser();
           JsonElement mJson =  parser.parse(user);
           Gson gson = new Gson();
           return gson.fromJson(mJson, User.class);
       } else
           return null;
    }

    public String getUserId(){
        String user = preferences.getString("user" , null);
        if (user != null){
            try {
                JSONObject userObject = new JSONObject(user);
                return userObject.getString("id");
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } else
            return null;
    }

    public String getToken(){
        return "Bearer " + preferences.getString("token" , null);
    }

    public void logout() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("token");
        editor.remove("user");
        editor.apply();
    }
}
