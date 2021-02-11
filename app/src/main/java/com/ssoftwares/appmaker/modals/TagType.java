package com.ssoftwares.appmaker.modals;

public class TagType {
    String id;
    String name;
    String tag;

    public TagType() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public TagType(String id, String name, String tag) {
        this.id = id;
        this.name = name;
        this.tag = tag;
    }
}
