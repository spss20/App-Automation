package com.ssoftwares.appmaker.modals;

import com.ssoftwares.appmaker.api.ApiClient;

public class File {
    String id;
    String name;
    String url;
    String ext;
    String mime;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return ApiClient.BASE_URL +  url;
    }

    public String getExt() {
        return ext;
    }

    public String getMime() {
        return mime;
    }
}
