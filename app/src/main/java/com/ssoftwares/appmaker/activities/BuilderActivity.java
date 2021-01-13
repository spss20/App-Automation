package com.ssoftwares.appmaker.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.se.omapi.Session;
import android.text.InputFilter;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ssoftwares.appmaker.R;
import com.ssoftwares.appmaker.api.ApiClient;
import com.ssoftwares.appmaker.api.ApiService;
import com.ssoftwares.appmaker.fragments.CpanelBottomSheetFragment;
import com.ssoftwares.appmaker.fragments.ProductBottomSheetFragment;
import com.ssoftwares.appmaker.interfaces.CpanelSelectedListener;
import com.ssoftwares.appmaker.interfaces.ProductSelectedListener;
import com.ssoftwares.appmaker.modals.Cpanel;
import com.ssoftwares.appmaker.modals.DynamicEditText;
import com.ssoftwares.appmaker.modals.DynamicLinearLayout;
import com.ssoftwares.appmaker.modals.Product;
import com.ssoftwares.appmaker.modals.SelectItemDynamicLayout;
import com.ssoftwares.appmaker.modals.User;
import com.ssoftwares.appmaker.utils.AppUtils;
import com.ssoftwares.appmaker.utils.IdInputFilter;
import com.ssoftwares.appmaker.utils.SessionManager;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BuilderActivity extends AppCompatActivity {

    JSONObject rootJson;
    LinearLayout rootView;
    ApiService service;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_builder);

        service = ApiClient.create();
        sessionManager = new SessionManager(this);

//        try {
//            InputStream jsonInputStream = getAssets().open("config_cpanel.json");
//            rootJson = new JSONObject(IOUtils.toString(jsonInputStream, "UTF-8"));
//        } catch (IOException | JSONException e) {
//            e.printStackTrace();
//        }
        String textJson = getIntent().getStringExtra("config");
        if (textJson != null) {
            try {
                rootJson = new JSONObject(textJson);
                if (rootJson == null) {
                    Toast.makeText(this, "Cannot read json file . Failed!", Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        rootView = findViewById(R.id.root_view);
        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        ImageView backbtn = findViewById(R.id.back_bt);

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (rootJson != null)
            try {
                if (rootJson.has("title"))
                    toolbarTitle.setText(rootJson.getString("title"));
                else
                    toolbarTitle.setText(getIntent().getStringExtra("subproduct_name"));
                JSONArray schema = rootJson.getJSONArray("schema");
                for (int i = 0; i < schema.length(); i++) {
                    JSONObject element = schema.getJSONObject(i);
                    inflateView(rootView, element.getString("type"), element);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        else {
            String name = getIntent().getStringExtra("config_name");
            if (name == null)
                return;
            service.getConfig(name)
                    .enqueue(new Callback<JsonArray>() {
                        @Override
                        public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                            if (response.body() != null) {
                                try {
                                    String data = response.body().getAsJsonArray().get(0).getAsJsonObject().get("data").getAsJsonObject().toString();
                                    rootJson = new JSONObject(data);
                                    if (rootJson.has("title"))
                                        toolbarTitle.setText(rootJson.getString("title"));
                                    else
                                        toolbarTitle.setText(getIntent().getStringExtra("subproduct_name"));
                                    JSONArray schema = rootJson.getJSONArray("schema");
                                    for (int i = 0; i < schema.length(); i++) {
                                        JSONObject element = schema.getJSONObject(i);
                                        inflateView(rootView, element.getString("type"), element);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<JsonArray> call, Throwable t) {
                            AppUtils.handleNoInternetConnection(BuilderActivity.this);
                        }
                    });
        }
    }

    private void inflateView(LinearLayout rootView, String type, JSONObject element) throws JSONException {
        Resources r = getResources();
        int px15 = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                15,
                r.getDisplayMetrics()
        );
        int px70 = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                70,
                getResources().getDisplayMetrics()
        );
        int px20 = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                20,
                r.getDisplayMetrics()
        );
        LinearLayout.LayoutParams params15dp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params15dp.setMargins(0, px15, 0, 0);
        LinearLayout.LayoutParams params20dpwrap = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params20dpwrap.setMargins(0, px20, 0, 0);
        switch (type) {
            case "edittext":
                DynamicEditText editext = (DynamicEditText) getLayoutInflater().inflate(R.layout.edittext_item, null);
                editext.setApiKey(element.getString("id"));
                editext.setLayoutParams(params15dp);
                if (element.has("minLength")) editext.setMinLength(element.getInt("minLength"));
                if (element.has("maxLength")) editext.setMaxLength(element.getInt("maxLength"));
                if (element.has("isRequired"))
                    editext.setRequired(element.getBoolean("isRequired"));
                if (element.has("default_value"))
                    editext.setDefaultValue(element.getString("default_value"));
                if (element.has("hint")) editext.setHint(element.getString("hint"));
                if (element.has("stringregex")) editext.setRegex(element.getString("stringregex"));
                if (element.has("regex")) {
                    String regex = element.getString("regex");
                    editext.getEditText().setFilters(new InputFilter[]{new IdInputFilter(BuilderActivity.this, regex)});
                }
                editext.getEditText().setFocusable(true);
                editext.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (!hasFocus) {
                            int length = editext.getEditText().getText().toString().length();
                            if (length == 0) {
                                return;
                            }
                            if (editext.getRegex() != null) {
                                boolean isMatch = editext.getEditText().getText().toString().matches(editext.getRegex());
                                if (!isMatch)
                                    editext.setError("Invalid " + editext.getHint());
                                else
                                    editext.setError(null);
                            }
                            if (editext.getMinLength() != -1) {
                                if (length < editext.getMinLength()) {
                                    editext.setError("Minimum length should be " + editext.getMinLength() + " characters");
                                } else {
                                    editext.setError(null);
                                }
                            }

                            if (editext.getMaxLength() != -1) {
                                if (length > editext.getMaxLength()) {
                                    editext.setError("Input exceeds the maximum length of " + editext.getMinLength() + " characters");
                                } else {
                                    editext.setError(null);
                                }
                            }
                        } else editext.setError(null);
                    }
                });
                rootView.addView(editext);
                break;
            case "textview":
                TextView textView = (TextView) getLayoutInflater().inflate(R.layout.textview_item, null);
                textView.setLayoutParams(params15dp);
                textView.setText(element.getString("text"));
                if (element.has("size")) textView.setTextSize(element.getInt("size"));
                if (element.has("gravity")) {
                    switch (element.getString("gravity")) {
                        case "start":
                            textView.setGravity(Gravity.START);
                            break;
                        case "end":
                            textView.setGravity(Gravity.END);
                            break;
                        case "center":
                            textView.setGravity(Gravity.CENTER_HORIZONTAL);
                            break;
                        default:
                            textView.setGravity(Gravity.START);
                    }
                }
                rootView.addView(textView);
                break;
            case "button":
                if (element.getString("action").equalsIgnoreCase("pick")) {
                    DynamicLinearLayout pickLayout = (DynamicLinearLayout) getLayoutInflater().inflate(R.layout.button_pick_item, null);
                    pickLayout.setApiKey(element.getString("id"));
                    pickLayout.setLayoutParams(params15dp);

                    Button pickFileButton = pickLayout.findViewById(R.id.pick_bt);

                    if (element.has("text")) pickFileButton.setText(element.getString("text"));
                    if (element.has("extension"))
                        pickLayout.setExtension(element.getString("extension"));
                    int requestCode = new Random().nextInt(10000);
                    pickLayout.setRequestCode(requestCode);
                    pickFileButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                            if (element.has("mimetype")) {
                                try {
                                    intent.setType(element.getString("mimetype"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            startActivityForResult(intent, pickLayout.getRequestCode());
                        }
                    });
                    rootView.addView(pickLayout);
                } else if (element.getString("action").equalsIgnoreCase("submit")) {
                    Button submit = (Button) getLayoutInflater().inflate(R.layout.button_item, null);
                    params20dpwrap.gravity = Gravity.CENTER_HORIZONTAL;
                    submit.setLayoutParams(params20dpwrap);
                    submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                submitData();
                            } catch (JSONException | IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    rootView.addView(submit);
                }
                break;
            case "relation":
                String endpoint = element.getString("endpoint");
                if (endpoint.equals("cpanels")){
                    SelectItemDynamicLayout selectCpanel = (SelectItemDynamicLayout) getLayoutInflater().inflate(R.layout.select_items_button , null);
                    selectCpanel.setLayoutParams(params15dp);
                    selectCpanel.setApiKey(element.getString("id"));
                    Button selectButton = (Button) selectCpanel.findViewById(R.id.select_item_bt);
                    selectButton.setCompoundDrawablesWithIntrinsicBounds(getDrawable(R.drawable.ic_cpanel) , null , null , null);
                    selectButton.setText(element.getString("text"));
                    selectButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            CpanelBottomSheetFragment cpanelFragment = CpanelBottomSheetFragment.newInstance(new CpanelSelectedListener() {
                                @Override
                                public void onSelected(Cpanel cpanel) {
                                    View view;
                                    if (selectCpanel.getChildCount() == 1) {
                                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, px70);
                                        view = getLayoutInflater().inflate(R.layout.cpanel_item, null);
                                        view.findViewById(R.id.created_on).setVisibility(View.GONE);
                                        view.setLayoutParams(params);
                                        selectCpanel.addView(view);
                                    } else {
                                        view = selectCpanel.getChildAt(1);
                                    }
                                    ((TextView)view.findViewById(R.id.domain_name)).setText(cpanel.getDomain());
                                    ((TextView) view.findViewById(R.id.client_name)).setText(cpanel.getClient_name());
                                    selectCpanel.setSelectedId(cpanel.getId());
                                    CpanelBottomSheetFragment fragment = (CpanelBottomSheetFragment) getSupportFragmentManager().findFragmentByTag("CpanelDialog");
                                    if (fragment != null) {
                                        fragment.dismiss();
                                    }
                                }
                            });
                            cpanelFragment.show(getSupportFragmentManager() , "CpanelDialog");
                        }
                    });
                    rootView.addView(selectCpanel);
                } else if (endpoint.equals("products")){
                    SelectItemDynamicLayout selectProduct = (SelectItemDynamicLayout) getLayoutInflater().inflate(R.layout.select_items_button , null);
                    selectProduct.setLayoutParams(params15dp);
                    selectProduct.setApiKey(element.getString("id"));
                    Button selectButton = (Button) selectProduct.findViewById(R.id.select_item_bt);
                    selectButton.setCompoundDrawablesWithIntrinsicBounds(getDrawable(R.drawable.ic_product_add) , null , null , null);
                    selectButton.setText(element.getString("text"));
                    selectButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ProductBottomSheetFragment productFragment = ProductBottomSheetFragment.newInstance(
                                    new ProductSelectedListener() {
                                        @Override
                                        public void onSelected(Product product) {
                                            View view;
                                            if (selectProduct.getChildCount() == 1) {
                                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, px70);
                                                view = getLayoutInflater().inflate(R.layout.product_item_2, null);
                                                view.findViewById(R.id.product_category).setVisibility(View.GONE);
                                                view.setLayoutParams(params);
                                                selectProduct.addView(view);
                                            } else {
                                                view = selectProduct.getChildAt(1);
                                            }
                                            ((TextView)view.findViewById(R.id.product_name)).setText(product.getName());
                                            ((TextView) view.findViewById(R.id.product_short_desc)).setText(product.getShort_description());
                                            selectProduct.setSelectedId(product.getId());
                                            ProductBottomSheetFragment fragment = (ProductBottomSheetFragment) getSupportFragmentManager().findFragmentByTag("ProductDialog");
                                            if (fragment != null) {
                                                fragment.dismiss();
                                            }
                                        }
                                    }
                            );
                            productFragment.show(getSupportFragmentManager() , "ProductDialog");
                        }
                    });
                    rootView.addView(selectProduct);
                }
        }
    }

    private void submitData() throws JSONException, IOException {

        String endpoint = rootJson.getString("endpoint");
        //prepare data
        JSONObject data = new JSONObject();
        //prepare files
        List<MultipartBody.Part> filesBodyList = new ArrayList<>();

        for (int i = 0; i < rootView.getChildCount(); i++) {
            View view = rootView.getChildAt(i);
            if (view instanceof DynamicEditText) {
                DynamicEditText childView = (DynamicEditText) view;
                String text = childView.getEditText().getText().toString();
                if (text.isEmpty())
                    if (childView.isRequired()) {
                        childView.setError("This field can't be empty");
                        return;
                    } else continue;
                if (childView.getRegex() != null && !text.matches(childView.getRegex())) {
                    childView.setError("Invalid " + childView.getHint());
                    return;
                }
                data.put(childView.getApiKey(), text);
            } else if (view instanceof DynamicLinearLayout) {
                DynamicLinearLayout childView = (DynamicLinearLayout) view;
                if (childView.getUri() == null) {
                    if (childView.isRequired()) {
                        Toast.makeText(this, "" + childView.getApiKey() + " is required", Toast.LENGTH_SHORT).show();
                        return;
                    } else continue;
                }
                MultipartBody.Part fileBody = ApiClient.prepareFilePart(BuilderActivity.this,
                        "files." + childView.getApiKey(), childView.getUri());
                filesBodyList.add(fileBody);
            } else if (view instanceof SelectItemDynamicLayout){
                SelectItemDynamicLayout childView = (SelectItemDynamicLayout) view;
                if (childView.getSelectedId() == null){
                    if (childView.isRequired()){
                        Toast.makeText(this, "" +  childView.getApiKey() + "is required", Toast.LENGTH_SHORT).show();
                        return;
                    } else continue;
                }
                data.put(childView.getApiKey() , new JSONArray().put(childView.getSelectedId()));
            }
        }

//        data.put("user_id", new JSONArray().put("2"));
        Log.v("Data", data.toString());
        RequestBody dataBody = ApiClient.createPartFromString(data.toString());

        ApiService service = ApiClient.create();
        service.createEntry(endpoint, dataBody, filesBodyList)
                .enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if (response.body() == null) {
                            Toast.makeText(BuilderActivity.this, "Api Execution Failed", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Log.v("Response", response.body().toString());
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Toast.makeText(BuilderActivity.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (data.getData() == null)
                return;
            for (int i = 0; i < rootView.getChildCount(); i++) {
                View view = rootView.getChildAt(i);
                if (view instanceof DynamicLinearLayout) {
                    if (requestCode == ((DynamicLinearLayout) view).getRequestCode()) {
                        DynamicLinearLayout dynamicLinearLayout = (DynamicLinearLayout) view;
                        String fileName = AppUtils.getFileName(this, data.getData());

                        if (dynamicLinearLayout.getExtension() != null) {
                            String[] fileNameSplitted = fileName.split("\\.");
                            if (fileNameSplitted.length > 1) {
                                if (fileNameSplitted[fileNameSplitted.length - 1].equalsIgnoreCase(dynamicLinearLayout.getExtension())) {
                                    dynamicLinearLayout.setUri(data.getData());
                                    ((TextView) dynamicLinearLayout.findViewById(R.id.file_name_tv)).setText(fileName);
                                } else {
                                    Toast.makeText(this, "Invalid File. Please select a file with valid extension", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(this, "Invalid File. Please select a file with valid extension", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            dynamicLinearLayout.setUri(data.getData());
                            ((TextView) dynamicLinearLayout.findViewById(R.id.file_name_tv)).setText(fileName);
                        }
                    }
                }
            }
        }
    }


}