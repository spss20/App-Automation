package com.ssoftwares.appmaker.api;

import android.content.Context;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;
import android.webkit.MimeTypeMap;

import androidx.annotation.NonNull;

import com.ssoftwares.appmaker.modals.DynamicLinearLayout;
import com.ssoftwares.appmaker.utils.AppUtils;

import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ApiClient {

    private static Retrofit retrofit;
    public static final String BASE_URL = "http://ssoft.xyz:1337";
//    public static final String BASE_URL = "http://10.0.2.2:1337";
//    public static final String BASE_URL = "http://192.168.1.7:1337";

    public static ApiService create() {
        if (retrofit == null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .build();
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build();
        }
        return retrofit.create(ApiService.class);
    }

    @NonNull
    public static RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(descriptionString, MultipartBody.FORM);
    }

    @NonNull
    public static MultipartBody.Part prepareFilePart(Context context, String keyName, Uri fileUri) throws IOException {
        // create RequestBody instance from file
        InputStream is = context.getContentResolver().openInputStream(fileUri);
        RequestBody requestFile =
                RequestBody.create(
                        IOUtils.toByteArray(is),
                        MediaType.parse(context.getContentResolver().getType(fileUri))
                );

        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(keyName,
                AppUtils.getFileName(context, fileUri), requestFile);
    }

    @NonNull
    public static MultipartBody.Part prepareFilePart(String keyName, DynamicLinearLayout dl) {
        Log.v("Mime Type" , dl.getFileName());
        byte[] fileBytes = Base64.decode(dl.getFileBase64(), Base64.DEFAULT);
        // create RequestBody instance from file
        String mime;
        mime = URLConnection.guessContentTypeFromName(dl.getFileName());
        if (mime == null)
            mime = "*/*";
//        if (mime == null) {
//            String[] spittedName = dl.getFileName().split("\\.");
//            if (spittedName.length > 0) {
////                mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension("." + spittedName[spittedName.length-1]);
//               mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension("json");
//            }
//            if (mime == null)
//                return null;
//        }
        Log.v("MemeType", mime);
        RequestBody requestFile =
                RequestBody.create(
                        fileBytes,
                        MediaType.parse(mime)
                );

        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(keyName,
                dl.getFileName(), requestFile);
    }
}
