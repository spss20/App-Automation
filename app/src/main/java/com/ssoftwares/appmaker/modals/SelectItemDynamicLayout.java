package com.ssoftwares.appmaker.modals;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

public class SelectItemDynamicLayout extends LinearLayout {

    private String apiKey;
    private String selectedId;
    private boolean isRequired;

    public SelectItemDynamicLayout(Context context) {
        super(context);
    }

    public SelectItemDynamicLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SelectItemDynamicLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SelectItemDynamicLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getSelectedId() {
        return selectedId;
    }

    public void setSelectedId(String selectedId) {
        this.selectedId = selectedId;
    }

    public boolean isRequired() {
        return isRequired;
    }

    public void setRequired(boolean required) {
        isRequired = required;
    }
}
