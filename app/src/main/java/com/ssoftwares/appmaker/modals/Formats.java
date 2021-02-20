package com.ssoftwares.appmaker.modals;

import java.io.Serializable;

public class Formats implements Serializable {
    Attachment thumbnail;
    Attachment large;
    Attachment medium;
    Attachment small;

    public Attachment getThumbnail() {
        return thumbnail;
    }

    public Attachment getLarge() {
        return large;
    }

    public Attachment getMedium() {
        return medium;
    }

    public Attachment getSmall() {
        return small;
    }
}
