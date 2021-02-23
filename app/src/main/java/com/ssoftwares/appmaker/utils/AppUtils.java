package com.ssoftwares.appmaker.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;


import com.ssoftwares.appmaker.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AppUtils {

    public static String getFileName(Context context , Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }
    private static Dialog resultDialog;
    private static Dialog dialog;
    private static boolean isProcessCompleted = false;

    public static void handleNoInternetConnection(Context context){
        Toast.makeText(context, "No Internet. Please check your internet connection", Toast.LENGTH_SHORT).show();

    }

    public static void showResultDialog(Activity activity, String scripturl) {
        if (resultDialog != null) {
            resultDialog.dismiss();
            resultDialog = null;
        }
        isProcessCompleted = false;
        resultDialog = new Dialog(activity);
        resultDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        resultDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        resultDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT ,
                WindowManager.LayoutParams.MATCH_PARENT);
        resultDialog.setContentView(R.layout.dialog_result);
        resultDialog.setCancelable(false);
        resultDialog.show();
        TextView outputTv = resultDialog.findViewById(R.id.output_text);
        outputTv.setMovementMethod(new ScrollingMovementMethod());
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        outputTv.setText("Fetching Data...");
                    }
                });
                try {
                    URL url = new URL(scripturl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.connect();
                    InputStream stream = url.openStream();
                    InputStreamReader inputStreamReader = new InputStreamReader(stream);
                    BufferedReader br = new BufferedReader(inputStreamReader);
                    String line;
                    while ((line = br.readLine()) != null){
                        final String finalLine = line;
                        if (line.contains("78599")) {
                            isProcessCompleted = true;
//                            break;
                        } else {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    outputTv.append("\n" + finalLine);
                                }
                            });
                        }
                    }
                    br.close();
                    inputStreamReader.close();
                    stream.close();
                    connection.disconnect();

                    if (!isProcessCompleted){
                        Thread.sleep(4000);
                        new Thread(this).start();
                    }

                } catch (IOException | InterruptedException e) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    e.printStackTrace();
                }

            }
        });
        thread.start();

        resultDialog.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissResultDialog();
            }
        });
    }

    public static void dismissResultDialog(){
        if (resultDialog != null)
            resultDialog.dismiss();
        resultDialog = null;
    }
    public static void showLoadingBar(Context context) {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_progress);
        dialog.setCancelable(false);
        dialog.show();
    }
    public static void dissmissLoadingBar() {
        if (dialog != null)
            dialog.dismiss();
        dialog = null;
    }

    public static String getMd5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
