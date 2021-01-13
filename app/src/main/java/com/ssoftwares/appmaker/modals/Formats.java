package com.ssoftwares.appmaker.modals;

import java.io.Serializable;

public class Formats implements Serializable {
    Image thumbnail;
    Image large;
    Image medium;
    Image small;

    public Image getThumbnail() {
        return thumbnail;
    }

    public Image getLarge() {
        return large;
    }

    public Image getMedium() {
        return medium;
    }

    public Image getSmall() {
        return small;
    }
}
