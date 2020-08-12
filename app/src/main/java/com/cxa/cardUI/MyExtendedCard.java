package com.cxa.cardUI;

import android.content.Context;

import com.dexafree.materialList.cards.ExtendedCard;
import com.dexafree.materialList.cards.OnButtonPressListener;

/**
 * Created by dancrisan on 12/14/16.
 */

public abstract class MyExtendedCard extends ExtendedCard {

    protected OnButtonPressListener onCardPressedListener;

    public MyExtendedCard(final Context context) {
        super(context);
    }


    public OnButtonPressListener getOnCardPressedListener() {
        return onCardPressedListener;
    }

    public void setOnCardPressedListener(OnButtonPressListener onCardPressedListener) {
        this.onCardPressedListener = onCardPressedListener;
    }
}
