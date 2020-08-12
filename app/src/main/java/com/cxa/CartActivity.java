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
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.cxa.cardUI.MyBasicImageButtonsCard;
import com.cxa.search.SearchActivity;
import com.cxa.util.UIUtil;
import com.dexafree.materialList.cards.OnButtonPressListener;
import com.dexafree.materialList.model.Card;
import com.dexafree.materialList.view.MaterialListView;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

public class CartActivity extends AppCompatActivity {

    private DatabaseAdapter dbHelper;
    private SimpleCursorAdapter dataAdapter;
    private int quanity = 1;
    private int i = 0;
    private Button checkoutButton;
    BottomBar bottomBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        UIUtil.setupHeader(getSupportActionBar());

        displaySummaryView();

        setCheckoutButton();

        generateCardsFromDatabase();

        bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        displayBottomBar();

    }

    private void displaySummaryView() {

        dbHelper = new DatabaseAdapter(this);
        dbHelper.open();


        Cursor cursor = dbHelper.fetchAllPurchases();

        String[] columns = new String[] {
                DatabaseAdapter.KEY_SUBTOTAL,
                DatabaseAdapter.KEY_SHIPPING,
                DatabaseAdapter.KEY_TAX,
                DatabaseAdapter.KEY_TOTAL
        };

        // the XML defined views which the data will be bound to
        int[] to = new int[] {
                R.id.subtotal,
                R.id.shipping,
                R.id.tax,
                R.id.total,
        };

        // create the adapter using the cursor pointing to the desired data
        //as well as the layout information
        dataAdapter = new SimpleCursorAdapter(
                this, R.layout.custom_cart_bill_info,
                cursor,
                columns,
                to,
                0);

        ListView listView = (ListView) findViewById(R.id.acquiredProductsList);
        listView.setEnabled(false);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);


        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view,
                                    int position, long id) {
                // Get the cursor, positioned to the corresponding row in the result set
                Cursor cursor = (Cursor) listView.getItemAtPosition(position);

                // Get the state's capital from this row in the database.
                String countryCode =
                        cursor.getString(cursor.getColumnIndexOrThrow("code"));
                Toast.makeText(getApplicationContext(),
                        countryCode, Toast.LENGTH_SHORT).show();

            }
        });

        dbHelper.close();

    }

    public void generateCardsFromDatabase(){
        dbHelper.open();

        if(dbHelper.getElementsInDB() > 1){
            for(i = 1; i < dbHelper.getElementsInDB(); i++){
                MyBasicImageButtonsCard card1 = new MyBasicImageButtonsCard(getApplicationContext());
                final String itemTitle = dbHelper.getObjectsPurchased().get(i);
                final String priceOfItem = dbHelper.getListOfPrices().get(i);
                final String itemQuantity = dbHelper.getListOfQuantities().get(i);
                quanity = Integer.parseInt(itemQuantity);
                card1.setTitle(itemTitle);
                card1.setOnRightButtonPressedListener(new OnButtonPressListener() {
                    @Override
                    public void onButtonPressedListener(View view, Card card) {
                        System.out.println(itemTitle);

                        dbHelper.updateQuantity(itemTitle, Integer.toString(Integer.parseInt(itemQuantity) + 1));

                        Double productPriceAsDouble = Double.parseDouble(priceOfItem.substring(1));
                        Double currentPriceAsDouble = Double.parseDouble(dbHelper.getCurrentSubtotal().substring(1)) + productPriceAsDouble;
                        String currentPriceAsString = String.format("%.2f", currentPriceAsDouble);
                        Double currentTaxAsDouble = 0.15*currentPriceAsDouble;
                        String currentTaxAsString = String.format("%.2f", currentTaxAsDouble);
                        String totalString = String.format("%.2f", currentPriceAsDouble + currentTaxAsDouble);
                        dbHelper.updateSubtotal(1, "$" + currentPriceAsString, "$0.00", currentTaxAsString, totalString);

                        finish();
                        startActivity(getIntent());

                    }
                });
                card1.setRightButtonTextColor(Color.parseColor("#b2096f"));
                card1.setOnLeftButtonPressedListener(new OnButtonPressListener() {
                    @Override
                    public void onButtonPressedListener(View view, Card card) {

                        dbHelper.updateQuantity(itemTitle, Integer.toString(Integer.parseInt(itemQuantity) - 1));

                        Double productPriceAsDouble = Double.parseDouble(priceOfItem.substring(1));
                        Double currentPriceAsDouble = Double.parseDouble(dbHelper.getCurrentSubtotal().substring(1)) - productPriceAsDouble;
                        String currentPriceAsString = String.format("%.2f", currentPriceAsDouble);
                        Double currentTaxAsDouble = 0.15*currentPriceAsDouble;
                        String currentTaxAsString = String.format("%.2f", currentTaxAsDouble);
                        String totalString = String.format("%.2f", currentPriceAsDouble + currentTaxAsDouble);
                        dbHelper.updateSubtotal(1, "$" + currentPriceAsString, "$0.00", currentTaxAsString, totalString);

                        finish();
                        startActivity(getIntent());

                    }
                });
                //hey
                card1.setDescription(dbHelper.getListOfQuantities().get(i) + " x " + priceOfItem);

                if(itemTitle.equals("Reading Tablet")){
                    card1.setDrawable(R.drawable.ic_reading_tablet);
                }
                else if(itemTitle.equals("Home Tablet")){
                    card1.setDrawable(R.drawable.ic_home_tablet);
                }
                else if(itemTitle.equals("Multimedia Tablet")){
                    card1.setDrawable(R.drawable.ic_multimedia_tablet);
                }
                else if(itemTitle.equals("Kitchen Tablet")){
                    card1.setDrawable(R.drawable.ic_kitchen_tablet);
                }
                else if(itemTitle.equals("Portable Tablet")){
                    card1.setDrawable(R.drawable.ic_portable_tablet);
                }
                else if(itemTitle.equals("Budget Tablet")){
                    card1.setDrawable(R.drawable.ic_budget_tablet);
                }
                else if(itemTitle.equals("Style Home Ultra Cozy Large Sofa")){
                    card1.setDrawable(R.drawable.ic_style_home);
                }
                else if(itemTitle.equals("Supreme Lounge Style Double Sofa")){
                    card1.setDrawable(R.drawable.supreme_lounge_style_soffa);
                }
                else if(itemTitle.equals("Supreme Giants Corner Sofa Set")){
                    card1.setDrawable(R.drawable.ic_supreme_giants);
                }
                else if(itemTitle.equals("Student Laptop")){
                    card1.setDrawable(R.drawable.ic_student_laptopt);
                }
                else if(itemTitle.equals("Office Laptop")){
                    card1.setDrawable(R.drawable.ic_office_laptop);
                }
                else if(itemTitle.equals("Budget Laptop")){
                    card1.setDrawable(R.drawable.ic_budget_laptop);
                }


                if(quanity == 0){
                    dbHelper.deleteProduct(itemTitle);
                    finish();
                    startActivity(getIntent());
                }

                card1.setLeftButtonText("Remove");
                card1.setRightButtonText("Add");



                MaterialListView mListView = (MaterialListView) findViewById(R.id.material_listview);

                mListView.add(card1);

            }

        }

        if(dbHelper.getElementsInDB() > 2){
            MyBasicImageButtonsCard card1 = new MyBasicImageButtonsCard(getApplicationContext());
            card1.setTitle("");
            card1.setDescription("");
            card1.setLeftButtonText("");
            card1.setRightButtonText("");
            card1.setBackgroundColor(0);
            MaterialListView mListView = (MaterialListView) findViewById(R.id.material_listview);

            mListView.add(card1);
        }
    }

    public void setCheckoutButton(){
        checkoutButton = (Button) findViewById(R.id.checkoutButton);
        checkoutButton.getBackground().setColorFilter(0xFF7CC7FF, PorterDuff.Mode.MULTIPLY);
        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbHelper.open();
                if(dbHelper.getElementsInDB() == 1){
                    alertCartEmpty();
                }
                else{
                    Intent intent = new Intent(CartActivity.this, CheckoutActivity.class);
                    startActivity(intent);
                }

            }
        });
    }

    public void displayBottomBar(){

        bottomBar.selectTabAtPosition(2);


        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(int tabId) {
                if (tabId == R.id.tab_home) {
                    Intent intent = new Intent(CartActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                else if (tabId == R.id.tab_search) {
                    Intent intent = new Intent(CartActivity.this, SearchActivity.class);
                    startActivity(intent);
                }
                else if (tabId == R.id.tab_more) {
                    Intent intent = new Intent(CartActivity.this, MoreActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void alertCartEmpty() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Empty Cart");
        alertDialog.setMessage("Please add items to cart to proceed");
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Back",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Start shopping",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(CartActivity.this, MainActivity.class);
                        startActivity(intent);
                        dialog.dismiss();
                    }
                });

        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#5596e6"));
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#5596e6"));
    }

    @Override
    public void onResume(){
        super.onResume();
        bottomBar.selectTabAtPosition(2);
        overridePendingTransition(0,0);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent e) {
        return super.dispatchTouchEvent(e);
    }
}
