package com.cxa.util;

import androidx.appcompat.app.ActionBar;

import com.cxa.R;

/**
 * Created by dancrisan on 1/30/17.
 */

public class UIUtil {

    public static void setupHeader(ActionBar bar){
        bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        bar.setCustomView(R.layout.custom_header_layout);
    }
}
