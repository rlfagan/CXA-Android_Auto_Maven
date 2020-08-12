/********************************************************************************************
 * Copyright (C) 2020 Acoustic, L.P. All rights reserved.
 *
 * NOTICE: This file contains material that is confidential and proprietary to
 * Acoustic, L.P. and/or other developers. No license is granted under any intellectual or
 * industrial property rights of Acoustic, L.P. except as may be provided in an agreement with
 * Acoustic, L.P. Any unauthorized copying or distribution of content from this file is
 * prohibited.
 ********************************************************************************************/
package com.cxa.cardUI;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import java.util.ArrayList;
import java.util.List;
import com.cxa.R;

import it.gmariotti.cardslib.library.view.CardViewNative;

public class CardArrayAdapter extends ArrayAdapter<Card> {

    private List<Card> cardList = new ArrayList<Card>();

    private ArrayList<Integer> listOfCards = new ArrayList<>();

    public CardArrayAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    @Override
    public void add(Card object) {
        cardList.add(object);
        super.add(object);
    }

    @Override
    public int getCount() {
        return this.cardList.size();
    }

    @Override
    public Card getItem(int index) {
        return this.cardList.get(index);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        final CardViewNative cardView;
        it.gmariotti.cardslib.library.internal.Card myCard;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.custom_card_holder, parent, false);
            Card card = getItem(position);
            myCard = new GplayCardCustomSource(getContext(), card.getLine2(), card.getLine3(), card.getLine4(), card.getLine5(), card.getLine6(), card.getCickListener());
            cardView = (CardViewNative) row.findViewById(R.id.carddemo_thumb_customsource);
            cardView.setBackgroundColor(Color.parseColor("#ffffff"));

            if (listOfCards.contains(position)) {
                cardView.setAlpha(0);


                myCard.setOnClickListener(new it.gmariotti.cardslib.library.internal.Card.OnCardClickListener() {
                    @Override
                    public void onClick(it.gmariotti.cardslib.library.internal.Card card, View view) {

                    }
                });

            }

            listOfCards.add(position);

            myCard.getCardHeader().setTitle(card.getLine1());

            cardView.setCard(myCard);

        } else {
//            viewHolder = (CardViewHolder)row.getTag();
        }

        return row;
    }

    public Bitmap decodeToBitmap(byte[] decodedByte) {
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }
}
