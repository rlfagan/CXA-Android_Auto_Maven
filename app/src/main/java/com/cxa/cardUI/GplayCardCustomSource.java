/*
 * ******************************************************************************
 *   Copyright (c) 2013-2014 Gabriele Mariotti.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *  *****************************************************************************
 */

package com.cxa.cardUI;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.cxa.R;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;

/**
 * This class provides a simple example as Google Play card.
 * The Google maps icon this time is loaded from package manager.
 *
 * @author Gabriele Mariotti (gabri.mariotti@gmail.com), Ronald Ammann (ramdroid.fn@gmail.com)
 */
public class GplayCardCustomSource extends Card {

    private String globalProductPrice;
    private float globalProductRating;
    private String globalRatingNumbers;
    private String globalBrandName;
    private int globalDrawableRes;

    public GplayCardCustomSource(Context context, String productPrice, float productRating, String ratingNumbers, String brandName, int drawableRes, OnCardClickListener clickable) {
        super(context, R.layout.custom_card);
        init(productPrice, productRating, ratingNumbers, brandName, drawableRes, clickable);
    }

    public GplayCardCustomSource(Context context, int innerLayout, String productPrice, float productRating, String ratingNumbers, String brandName, int drawableRes, OnCardClickListener clickable) {
        super(context, innerLayout);
        init(productPrice, productRating, ratingNumbers, brandName, drawableRes, clickable);
    }

    private void init(String productPrice, float productRating, String ratingNumbers, String brandName, int drawableRes, OnCardClickListener clickable) {
        CardHeader header = new CardHeader(getContext());
        header.setButtonOverflowVisible(false);
        header.setTitle("Reading tablet");
        globalProductPrice = productPrice;
        globalProductRating = productRating;
        globalRatingNumbers = ratingNumbers;
        globalBrandName = brandName;
        globalDrawableRes = drawableRes;

        setOnClickListener(clickable);

        addCardHeader(header);

        CustomThumbCard thumbnail = new CustomThumbCard((getContext()));

        thumbnail.setDrawableResource(globalDrawableRes);

        addCardThumbnail(thumbnail);
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {

        TextView title = (TextView) view.findViewById(R.id.carddemo_gplay_main_inner_title);
        title.setText(globalRatingNumbers);

        TextView subtitle = (TextView) view.findViewById(R.id.carddemo_gplay_main_inner_subtitle);
        subtitle.setText(globalProductPrice);

        TextView subtitle2 = (TextView) view.findViewById(R.id.carddemo_gplay_main_inner_subtitle2);
        subtitle2.setText(globalBrandName);

        RatingBar mRatingBar = (RatingBar) parent.findViewById(R.id.carddemo_gplay_main_inner_ratingBar);

        mRatingBar.setBackgroundColor(Color.parseColor("#ffffff"));

        mRatingBar.setNumStars(5);
        mRatingBar.setMax(5);
        mRatingBar.setStepSize(0.5f);
        mRatingBar.setRating(globalProductRating);

        if(globalProductRating < 0){
            mRatingBar.setVisibility(View.GONE);
        }

    }

}
