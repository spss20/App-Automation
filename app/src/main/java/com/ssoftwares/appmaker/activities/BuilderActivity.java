package com.ssoftwares.appmaker.activities;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ssoftwares.appmaker.R;
import com.ssoftwares.appmaker.api.ApiClient;
import com.ssoftwares.appmaker.api.ApiService;
import com.ssoftwares.appmaker.fragments.CpanelBottomSheetFragment;
import com.ssoftwares.appmaker.fragments.ProductBottomSheetFragment;
import com.ssoftwares.appmaker.modals.Cpanel;
import com.ssoftwares.appmaker.modals.DynamicEditText;
import com.ssoftwares.appmaker.modals.DynamicLinearLayout;
import com.ssoftwares.appmaker.modals.DynamicSpinner;
import com.ssoftwares.appmaker.modals.DynamicSwitch;
import com.ssoftwares.appmaker.modals.Product;
import com.ssoftwares.appmaker.modals.SelectItemDynamicLayout;
import com.ssoftwares.appmaker.utils.AppUtils;
import com.ssoftwares.appmaker.utils.Dimensions;
import com.ssoftwares.appmaker.utils.IdInputFilter;
import com.ssoftwares.appmaker.utils.SessionManager;

import org.apache.commons.io.IOUtils;
import org.apache.commons.text.StringEscapeUtils;
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

    private static final String TAG = BuilderActivity.class.getSimpleName();
    private JSONObject rootJson = null;
    private String configHash;
    private LinearLayout rootView;
    private ApiService service;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_builder);

        service = ApiClient.create();
        sessionManager = new SessionManager(this);
        if (sessionManager.getToken() == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            return;
        }
//        try {
//            InputStream jsonInputStream = getAssets().open("config.json");
//            rootJson = new JSONObject(IOUtils.toString(jsonInputStream, "UTF-8"));
//        } catch (IOException | JSONException e) {
//            e.printStackTrace();
//        }
        String data = getIntent().getStringExtra("config");
        if (data != null) {
            configHash = AppUtils.getMd5(data);
            try {
                //Check if a cache of same config file already exists
                if (sessionManager.getConfigHash() != null){
                    if (sessionManager.getConfigHash().equals(AppUtils.getMd5(data))){
                        data = sessionManager.getConfig();
                    }
                }
                rootJson = new JSONObject(data);
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
                                    configHash = AppUtils.getMd5(data);
                                    //Check if a cache of same config file already exists
                                    if (sessionManager.getConfigHash() != null){
                                        if (sessionManager.getConfigHash().equals(AppUtils.getMd5(data))){
                                            data = sessionManager.getConfig();
                                        }
                                    }
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

        LinearLayout.LayoutParams params15dp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params15dp.setMargins(0, Dimensions.get15dp(), 0, 0);
        LinearLayout.LayoutParams params20dpwrap = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params20dpwrap.setMargins(0, Dimensions.get20dp(), 0, 0);
        switch (type) {
            case "boolean" :
                DynamicSwitch dynamicSwitch = (DynamicSwitch) getLayoutInflater().inflate(R.layout.switch_item , null );
                dynamicSwitch.setApiKey(element.getString("id"));
                dynamicSwitch.setLayoutParams(params15dp);

                if (element.has("default")) dynamicSwitch.setChecked(element.getBoolean("default"));
                else dynamicSwitch.setChecked(false);

                if (element.has("text")) dynamicSwitch.setText(element.getString("text"));
                rootView.addView(dynamicSwitch);
                break;
            case "enum" :
                DynamicSpinner spinner = (DynamicSpinner) getLayoutInflater().inflate(R.layout.enum_item , null );
                spinner.setApiKey(element.getString("id"));
                spinner.setLayoutParams(params15dp);

                JSONArray jsonValues = element.getJSONArray("values");
                String[] values = new String[jsonValues.length() + 1];
                values[0] = element.getString("text");
                for (int i = 1 ; i < (jsonValues.length() + 1) ; i++){
                    values[i] = jsonValues.getString(i - 1);
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(BuilderActivity.this ,
                        android.R.layout.simple_spinner_item , values){
                    @Override
                    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                        View view = super.getDropDownView(position, convertView, parent);
                        TextView tv = (TextView) view;
                        tv.setGravity(Gravity.CENTER_HORIZONTAL);
                        if (position == 0) {
                            tv.setTextColor(Color.GRAY);
                        } else {
                            tv.setTextColor(Color.BLACK);
                        }
                        return view;
                    }
                };
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(arrayAdapter);
                rootView.addView(spinner);

                break;
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
                if (element.has("value"))
                    editext.getEditText().setText(element.getString("value"));

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
                                if (!isMatch) {
                                    editext.setError("Invalid " + editext.getHint());
                                    return;
                                }
                            }
                            if (editext.getMinLength() != -1) {
                                if (length < editext.getMinLength()) {
                                    editext.setError("Minimum length should be " + editext.getMinLength() + " characters");
                                    return;
                                }
                            }

                            if (editext.getMaxLength() != -1) {
                                if (length > editext.getMaxLength()) {
                                    editext.setError("Input exceeds the maximum length of " + editext.getMaxLength() + " characters");
                                    return;
                                }
                            }
                        }
                        editext.setError(null);
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
//                    if (element.has("value")) pickLayout.setUri(Uri.parse(element.getString("value")));

                    pickFileButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                            if (element.has("mime")) {
                                try {
                                    intent.setType(element.getString("mime"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            startActivityForResult(intent, pickLayout.getRequestCode());
                        }
                    });
                    rootView.addView(pickLayout);
//                    if (pickLayout.getUri() != null){
//                        onActivityResult(requestCode , RESULT_OK , new Intent().setData(pickLayout.getUri()));
//                    }
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
                if (endpoint.equals("cpanels")) {
                    SelectItemDynamicLayout selectCpanel = (SelectItemDynamicLayout) getLayoutInflater().inflate(R.layout.select_items_button, null);
                    selectCpanel.setLayoutParams(params15dp);
                    selectCpanel.setApiKey(element.getString("id"));
                    Button selectButton = (Button) selectCpanel.findViewById(R.id.select_item_bt);
                    selectButton.setCompoundDrawablesWithIntrinsicBounds(getDrawable(R.drawable.ic_cpanel), null, null, null);
                    selectButton.setText(element.getString("text"));
                    if (element.has("isRequired"))
                        selectCpanel.setRequired(element.getBoolean("isRequired"));
                    if (element.has("value")) {
                        String selectedId = element.getString("value");
                        selectCpanel.setSelectedId(selectedId);
                        getSingleCpanel(selectedId, selectCpanel);
                    }
                    selectButton.setOnClickListener(v -> {
                        CpanelBottomSheetFragment cpanelFragment = CpanelBottomSheetFragment.newInstance(cpanel -> {
                            onCpanelSelected(selectCpanel, cpanel);
                        });
                        cpanelFragment.show(getSupportFragmentManager(), "CpanelDialog");
                    });
                    rootView.addView(selectCpanel);
                } else if (endpoint.equals("products")) {
                    SelectItemDynamicLayout selectProduct = (SelectItemDynamicLayout) getLayoutInflater().inflate(R.layout.select_items_button, null);
                    selectProduct.setLayoutParams(params15dp);
                    selectProduct.setApiKey(element.getString("id"));
                    Button selectButton = (Button) selectProduct.findViewById(R.id.select_item_bt);
                    selectButton.setCompoundDrawablesWithIntrinsicBounds(getDrawable(R.drawable.ic_product_add), null, null, null);
                    selectButton.setText(element.getString("text"));
                    if (element.has("isRequired"))
                        selectProduct.setRequired(element.getBoolean("isRequired"));
                    if (element.has("value")) {
                        String selectedId = element.getString("value");
                        selectProduct.setSelectedId(selectedId);
                        getSingleProduct(selectedId, selectProduct);
                    }
                    selectButton.setOnClickListener(v -> {
                        ProductBottomSheetFragment productFragment = ProductBottomSheetFragment.newInstance(
                                product -> {
                                    onProductSelected(selectProduct, product);
                                }
                        );
                        productFragment.show(getSupportFragmentManager(), "ProductDialog");
                    });
                    rootView.addView(selectProduct);
                }
        }
    }

    private void getSingleProduct(String selectedId, SelectItemDynamicLayout selectProduct) {
        service.getSingleProduct(selectedId).enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if (response.body() != null)
                    onProductSelected(selectProduct, response.body());
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                AppUtils.handleNoInternetConnection(BuilderActivity.this);
            }
        });
    }

    public void getSingleCpanel(String selectedId, SelectItemDynamicLayout selectCpanel) {
        service.getSingleCpanel(sessionManager.getToken(), selectedId)
                .enqueue(new Callback<Cpanel>() {
                    @Override
                    public void onResponse(Call<Cpanel> call, Response<Cpanel> response) {
                        if (response.body() != null) {
                            onCpanelSelected(selectCpanel, response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<Cpanel> call, Throwable t) {
                        AppUtils.handleNoInternetConnection(BuilderActivity.this);
                    }
                });
    }

    public void onCpanelSelected(SelectItemDynamicLayout selectItemDynamicLayout, Cpanel cpanel) {
        View view;
        if (selectItemDynamicLayout.getChildCount() == 1) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Dimensions.get70dp());
            view = getLayoutInflater().inflate(R.layout.cpanel_item, null);
            view.findViewById(R.id.created_on).setVisibility(View.GONE);
            view.setLayoutParams(params);
            selectItemDynamicLayout.addView(view);
        } else {
            view = selectItemDynamicLayout.getChildAt(1);
        }
        ((TextView) view.findViewById(R.id.domain_name)).setText(cpanel.getDomain());
        ((TextView) view.findViewById(R.id.client_name)).setText(cpanel.getClient_name());
        selectItemDynamicLayout.setSelectedId(cpanel.getId());
        CpanelBottomSheetFragment fragment = (CpanelBottomSheetFragment) getSupportFragmentManager().findFragmentByTag("CpanelDialog");
        if (fragment != null) {
            fragment.dismiss();
        }
    }

    private void onProductSelected(SelectItemDynamicLayout selectProduct, Product product) {
        View view;
        if (selectProduct.getChildCount() == 1) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Dimensions.get70dp());
            view = getLayoutInflater().inflate(R.layout.product_item_2, null);
            view.findViewById(R.id.product_category).setVisibility(View.GONE);
            view.setLayoutParams(params);
            selectProduct.addView(view);
        } else {
            view = selectProduct.getChildAt(1);
        }
        ((TextView) view.findViewById(R.id.product_name)).setText(product.getName());
        ((TextView) view.findViewById(R.id.product_short_desc)).setText(product.getShort_description());
        selectProduct.setSelectedId(product.getId());
        ProductBottomSheetFragment fragment = (ProductBottomSheetFragment) getSupportFragmentManager().findFragmentByTag("ProductDialog");
        if (fragment != null) {
            fragment.dismiss();
        }
    }


    private void submitData() throws JSONException, IOException {

        String endpoint = rootJson.getString("endpoint");
        //prepare data
        JSONObject data = new JSONObject();
        //prepare files
        List<MultipartBody.Part> filesBodyList = new ArrayList<>();

        //Get All Dynamic Data with values entered by user and put into data object.
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
                if (childView.getMinLength() != -1) {
                    if (text.length() < childView.getMinLength()) {
                        childView.setError("Minimum length should be " + childView.getMinLength() + " characters");
                        return;
                    }
                }

                if (childView.getMaxLength() != -1) {
                    if (text.length() > childView.getMaxLength()) {
                        childView.setError("Input exceeds the maximum length of " + childView.getMaxLength() + " characters");
                        return;
                    }
                }
                data.put(childView.getApiKey(), text);
            }
            else if (view instanceof DynamicLinearLayout) {
                DynamicLinearLayout childView = (DynamicLinearLayout) view;
                if (childView.getUri() == null) {
                    if (childView.isRequired()) {
                        Toast.makeText(this, "" + childView.getApiKey().toUpperCase() + " is required", Toast.LENGTH_SHORT).show();
                        return;
                    } else continue;
                }
                MultipartBody.Part fileBody = ApiClient.prepareFilePart(BuilderActivity.this,
                        "files." + childView.getApiKey(), childView.getUri());
                filesBodyList.add(fileBody);
            }
            else if (view instanceof SelectItemDynamicLayout) {
                SelectItemDynamicLayout childView = (SelectItemDynamicLayout) view;
                if (childView.getSelectedId() == null) {
                    if (childView.isRequired()) {
                        Toast.makeText(this, "" + childView.getApiKey().toUpperCase() + " is required", Toast.LENGTH_SHORT).show();
                        return;
                    } else continue;
                }
                data.put(childView.getApiKey(), new JSONArray().put(childView.getSelectedId()));
            }
            else if (view instanceof DynamicSpinner){
                DynamicSpinner childView = (DynamicSpinner) view;
                if (childView.getSelectedItemPosition() == 0){
                    Toast.makeText(this, "Please select " +
                                    childView.getApiKey().replace("Select" , "")
                            , Toast.LENGTH_SHORT).show();
                    return;
                }
                data.put(childView.getApiKey() , childView.getSelectedItem().toString());
            }
            else if (view instanceof DynamicSwitch){
                DynamicSwitch childView = (DynamicSwitch) view;
                data.put(childView.getApiKey() , childView.isChecked());
            }
        }

        //Get Constant hardcoded values from api schema and put into data object.
        JSONArray schema = rootJson.getJSONArray("schema");
        for (int i = 0; i < schema.length(); i++) {
            JSONObject element = schema.getJSONObject(i);
            if (element.has("type")){
                if (element.getString("type").equals("constant")){
                    data.put(element.getString("id") , element.getString("value"));
                }
            }
        }

        Log.v("Data", data.toString());
        AppUtils.showLoadingBar(this);
        RequestBody dataBody = ApiClient.createPartFromString(data.toString());

        ApiService service = ApiClient.create();
        service.createEntry(sessionManager.getToken(), endpoint, dataBody, filesBodyList)
                .enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        AppUtils.dissmissLoadingBar();
                        if (response.body() == null) {
                            Toast.makeText(BuilderActivity.this, "Api Execution Failed", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (response.body().has("outputUrl")) {
                            AppUtils.showResultDialog(BuilderActivity.this, response.body().get("outputUrl").getAsString());
                        } else {
                            Toast.makeText(BuilderActivity.this, "ERR: No Output Url Found", Toast.LENGTH_SHORT).show();
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
            if (data == null || data.getData() == null)
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

    @Override
    protected void onPause() {
        super.onPause();
        Log.v(TAG, "OnPauseCalled");
        if (configHash == null)
            return;
        Log.v("Hash" , configHash);
        int configJsonLength = rootJson.toString().length();
        try {
            JSONArray schema = rootJson.getJSONArray("schema");
            for (int i = 0; i < rootView.getChildCount(); i++) {
                View view = rootView.getChildAt(i);
                if (view instanceof DynamicEditText) {
                    DynamicEditText childView = (DynamicEditText) view;
                    String text = childView.getEditText().getText().toString();
                    if (text.isEmpty())
                        continue;
                    if (childView.getRegex() != null && !text.matches(childView.getRegex())) {
                        continue;
                    }
                    for (int j = 0; j < schema.length(); j++) {
                        JSONObject childObject = schema.getJSONObject(j);
                        if (childObject.has("id") &&
                                childObject.getString("id").equals(childView.getApiKey())) {
                            childObject.put("value", text);
                        }
                    }
                } else if (view instanceof DynamicLinearLayout) {
                    DynamicLinearLayout childView = (DynamicLinearLayout) view;
                    Uri uri = childView.getUri();
                    if (uri != null) {
                        for (int j = 0; j < schema.length(); j++) {
                            JSONObject childObject = schema.getJSONObject(j);
                            if (childObject.has("id") &&
                                    childObject.getString("id").equals(childView.getApiKey())) {
                                childObject.put("value", uri.toString());
                            }
                        }
                    }
                } else if (view instanceof SelectItemDynamicLayout) {
                    SelectItemDynamicLayout childView = (SelectItemDynamicLayout) view;
                    String selected = childView.getSelectedId();
                    if (selected != null) {
                        for (int j = 0; j < schema.length(); j++) {
                            JSONObject childObject = schema.getJSONObject(j);
                            if (childObject.has("id") &&
                                    childObject.getString("id").equals(childView.getApiKey())) {
                                childObject.put("value", selected);
                            }
                        }
                    }
                }
            }
            if (rootJson.toString().length() != configJsonLength) {
                sessionManager.saveConfig(configHash , rootJson.toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}