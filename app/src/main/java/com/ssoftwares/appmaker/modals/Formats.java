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
        if (large == null)
            return getMedium();
        return large;
    }

    public Attachment getMedium() {
        if (medium == null)
            return getSmall();
        return medium;
    }

    public Attachment getSmall() {
        if (small == null)
            return getThumbnail();
        return small;
    }
}
