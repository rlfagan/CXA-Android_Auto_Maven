package com.cxa.cardUI;

/**
 * Created by dancrisan on 12/14/16.
 */

import android.content.Context;

public class MyBasicImageButtonsCard extends MyExtendedCard {
    public MyBasicImageButtonsCard(final Context context) {
        super(context);
    }

    @Override
    public int getLayout() {
        return com.dexafree.materialList.R.layout.material_basic_image_buttons_card_layout;
    }

}

