/********************************************************************************************
 * Copyright (C) 2020 Acoustic, L.P. All rights reserved.
 *
 * NOTICE: This file contains material that is confidential and proprietary to
 * Acoustic, L.P. and/or other developers. No license is granted under any intellectual or
 * industrial property rights of Acoustic, L.P. except as may be provided in an agreement with
 * Acoustic, L.P. Any unauthorized copying or distribution of content from this file is
 * prohibited.
 ********************************************************************************************/
package com.cxa;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.cxa.util.UIUtil;
import com.tl.uic.Tealeaf;

public class CheckoutActivity extends AppCompatActivity {

    private DatabaseAdapter dbHelper;
    private Button purchaseComplete;
    private TextView creditCardText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        UIUtil.setupHeader(getSupportActionBar());

        enableCreditCardTextView();

        setCompletePurchaseButton();

        Button button = findViewById(R.id.prefill_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fillForm();
            }
        });
    }

    private void fillForm() {
        EditText nameOfCard = findViewById(R.id.nameOfCard);
        nameOfCard.setText("Leo");
        EditText cardNumber = findViewById(R.id.cardNumber);
        cardNumber.setText("5589765412346890");
        EditText cvvTextField = findViewById(R.id.cvvTextField);
        cvvTextField.setText("123");
        EditText expMonth = findViewById(R.id.expMonth);
        expMonth.setText("04");
        EditText expYear = findViewById(R.id.expYear);
        expYear.setText("2030");
        EditText address1 = findViewById(R.id.address1);
        address1.setText("505 Howard St");
        EditText address2 = findViewById(R.id.address2);
        address2.setText("Floor 6");

        EditText city = findViewById(R.id.city);
        city.setText("San Francisco");
        EditText state = findViewById(R.id.state);
        state.setText("CA");
        EditText zipCode = findViewById(R.id.zipCode);
        zipCode.setText("95105");

        // Log Screen Layout after fill form
        Tealeaf.logScreenLayout(this, "CheckOutFormScreen", 500);
    }

    public void setCompletePurchaseButton() {
        purchaseComplete = (Button) findViewById(R.id.buttonPurchase);
        purchaseComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!isValidateForm()) {
                    AlertDialog alertDialog = new AlertDialog.Builder(CheckoutActivity.this).create();
                    alertDialog.setTitle("Form Input");
                    alertDialog.setMessage("Please fill out form, or click PREFILL FORM button");
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Back",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });

                    alertDialog.show();
                    alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#5596e6"));
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#5596e6"));

                    return;
                }
                dbHelper = new DatabaseAdapter(getApplication());
                dbHelper.open();
                dbHelper.deleteAllPurchases();

                // Type 15
                Tealeaf.logFormCompletion(true, true);

                acknowledgePurchase();
            }
        });

    }

    public void enableCreditCardTextView() {
        creditCardText = (TextView) findViewById(R.id.cardNumber);
        Tealeaf.logFormCompletion(true);
    }

    private void acknowledgePurchase() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Test App");
        alertDialog.setMessage("This is a sample app for generating session. Please goto portal to see the generated session.");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(CheckoutActivity.this, MainActivity.class);
                        startActivity(intent);
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#5596e6"));
    }


    @Override
    public void onResume() {
        super.onResume();
        overridePendingTransition(0, 0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent e) {
        return super.dispatchTouchEvent(e);
    }

    private boolean isValidateForm() {
        EditText nameOfCard = findViewById(R.id.nameOfCard);
        EditText cardNumber = findViewById(R.id.cardNumber);
        EditText cvvTextField = findViewById(R.id.cvvTextField);
        EditText expMonth = findViewById(R.id.expMonth);
        EditText expYear = findViewById(R.id.expYear);
        EditText address1 = findViewById(R.id.address1);
//        EditText address2 = findViewById(R.id.address2);
        EditText city = findViewById(R.id.city);
        EditText state = findViewById(R.id.state);
        EditText zipCode = findViewById(R.id.zipCode);

        return !TextUtils.isEmpty(nameOfCard.getText()) && !TextUtils.isEmpty(cardNumber.getText()) && !TextUtils.isEmpty(cvvTextField.getText()) && !TextUtils.isEmpty(expMonth.getText()) &&
                !TextUtils.isEmpty(expYear.getText()) && !TextUtils.isEmpty(address1.getText()) && !TextUtils.isEmpty(city.getText()) &&
                !TextUtils.isEmpty(state.getText()) && !TextUtils.isEmpty(zipCode.getText());
    }
}
