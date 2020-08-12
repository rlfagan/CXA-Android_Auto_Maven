package com.cxa.rows;

/**
 * Created by dancrisan on 1/13/17.
 */

public class RowItem {
    private int imageId;
    private String desc;

    public RowItem(int imageId, String desc) {
        this.imageId = imageId;
        this.desc = desc;
    }
    public int getImageId() {
        return imageId;
    }
    public String getDesc() {
        return desc;
    }

}
