package com.ssoftwares.appmaker.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.widget.Toast;

import com.ssoftwares.appmaker.R;
import com.ssoftwares.appmaker.adapters.DemosAdapter;
import com.ssoftwares.appmaker.api.ApiClient;
import com.ssoftwares.appmaker.api.ApiService;
import com.ssoftwares.appmaker.interfaces.OnClickInterface;
import com.ssoftwares.appmaker.modals.Demo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class ProductDemoActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 50 ;
    private ApiService service;
    private DemosAdapter adapter;
    private DownloadManager downloadManager;
    private String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE , Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_demo);

        service = ApiClient.create();
        downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        registerReceiver(downloadReceiver, filter);

        String productId = getIntent().getStringExtra("product_id");
        String productName = getIntent().getStringExtra("product_name");

        if (productId == null){
            finish();
            return;
        }

        ActionBar toolbar = getSupportActionBar();
        toolbar.setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle(productName);

        RecyclerView demoRecycler = findViewById(R.id.product_demos_recycler);
        demoRecycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DemosAdapter(this , new ArrayList<>() , onClick);
        demoRecycler.setAdapter(adapter);

        getDemos(productId);

    }

    private OnClickInterface onClick = new OnClickInterface() {
        @Override
        public void onClick(Object object) {
            Demo demo = (Demo) object;
            if (demo.getUrl() != null && !demo.getUrl().isEmpty()){
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(demo.getUrl()));
                startActivity(i);
            } else {
                if (demo.getFile() != null){
                    if (!checkForPermissions()){
                        return;
                    }
                    DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(demo.getFile().getUrl()));
                    request.setDescription("downloading demo files...");
                    request.setTitle(demo.getFile().getName());
//                        request.allowScanningByMediaScanner();
//                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOCUMENTS , demo.getFile().getName() );

                    downloadManager.enqueue(request);
                    if (DownloadManager.STATUS_SUCCESSFUL == 8) {
                        Toast.makeText(ProductDemoActivity.this, "Download Successfull", Toast.LENGTH_SHORT).show();
                    } else Toast.makeText(ProductDemoActivity.this, "Download Failed", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };
    private BroadcastReceiver downloadReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

            if (downloadId == -1)
                return;

            // query download status
            Cursor cursor = downloadManager.query(new DownloadManager.Query().setFilterById(downloadId));
            if (cursor.moveToFirst()) {
                int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                if(status == DownloadManager.STATUS_SUCCESSFUL){

                    // download is successful
                    String uri = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                    File file = new File(Uri.parse(uri).getPath());
                }
                else {
                    // download is assumed cancelled
                }
            }
            else {
                // download is assumed cancelled
            }
        }
    };

    private boolean checkForPermissions() {
        boolean isGranted = true;
        for (String p : permissions){
            if (ContextCompat.checkSelfPermission(this , p) != PERMISSION_GRANTED)
                isGranted = false;
        }
        if (isGranted)
            return true;

        ActivityCompat.requestPermissions(this , permissions , PERMISSION_REQUEST_CODE);
        return false;
    }

    private void getDemos(String product_id) {
        service.getDemos(product_id).enqueue(new Callback<List<Demo>>() {
            @Override
            public void onResponse(Call<List<Demo>> call, Response<List<Demo>> response) {
                if (response.body() != null){
                    adapter.updateData(response.body());
                } else {
                    Toast.makeText(ProductDemoActivity.this, "Failed to fetch demo's ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Demo>> call, Throwable t) {
                Toast.makeText(ProductDemoActivity.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (downloadReceiver != null) {
            unregisterReceiver(downloadReceiver);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}