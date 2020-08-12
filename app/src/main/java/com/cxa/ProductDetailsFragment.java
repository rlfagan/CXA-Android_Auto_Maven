package com.cxa;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.tl.uic.Tealeaf;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;


public class ProductDetailsFragment extends Fragment {

    private DatabaseAdapter dbHelper;
    private ArrayList<String> categoryList = new ArrayList<String>();

    private TextView productPriceText;
    private TextView productNameText;
    private TextView productDescriptionText;
    private ImageView productImage;
    private RatingBar productRating;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        dbHelper = new DatabaseAdapter(getContext());
        dbHelper.open();


        String tabletRow = getArguments().getString("message");
        String productType = getArguments().getString("productType");

        if(productType == null){
            productType = "Tablets";
        }

        final List<String[]> list = new ArrayList<String[]>();

        String next[] = {};
        try {
            InputStreamReader csvStreamReader = new InputStreamReader(getContext().getAssets().open(productType + ".csv"));

            CSVReader reader = new CSVReader(csvStreamReader);
            for (;;) {
                next = reader.readNext();
                if (next != null) {
                    list.add(next);
                } else {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < list.size(); i++) {
            categoryList.add(list.get(i)[1]);
        }

        ScrollView scroll = new ScrollView(getContext());

        View productView = inflater.inflate(R.layout.fragment_product_info,container, false);

        productNameText = (TextView) productView.findViewById(R.id.nameOfProduct);
        productNameText.setText(list.get(Integer.parseInt(tabletRow))[0]);

        System.out.println("this is product type " + productType + productNameText.getText().toString());

        final String productPriceCSV = list.get(Integer.parseInt(tabletRow))[1];

        productPriceText = (TextView) productView.findViewById(R.id.priceOfProduct);
        productPriceText.setText("Price: $" + productPriceCSV.toString());

        productRating = (RatingBar) productView.findViewById(R.id.ratingBar);
        int productRatingFloat = Integer.parseInt(list.get(Integer.parseInt(tabletRow))[3]);
        productRating.setRating(productRatingFloat);

        productDescriptionText = (TextView) productView.findViewById(R.id.descriptionOfProduct);
        productDescriptionText.setText(list.get(Integer.parseInt(tabletRow))[2]);

        productImage = (ImageView) productView.findViewById(R.id.productImage);

        if(productNameText.getText().equals("Reading Tablet")){
            System.out.println("THIS IS TABLET-0");
            productImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_reading_tablet));
        }
        else if(productNameText.getText().equals("Home Tablet")){
            System.out.println("THIS IS TABLET-1");
            productImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_home_tablet));
        }
        else if(productNameText.getText().equals("Multimedia Tablet")){
            System.out.println("THIS IS TABLET-2");
            productImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_multimedia_tablet));
        }
        else if(productNameText.getText().equals("Kitchen Tablet")){
            productImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_kitchen_tablet));
        }
        else if(productNameText.getText().equals("Portable Tablet")){
            productImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_portable_tablet));
        }
        else if(productNameText.getText().equals("Budget Tablet")){
            productImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_budget_tablet));
        }

        else if(productNameText.getText().equals("Style Home Ultra Cozy Large Sofa")){
            productImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_style_home));
        }
        else if(productNameText.getText().equals("Supreme Lounge Style Double Sofa")){
            productImage.setImageDrawable(getResources().getDrawable(R.drawable.supreme_lounge_style_soffa));
        }
        else if(productNameText.getText().equals("Supreme Giants Corner Sofa Set")){
            productImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_supreme_giants));
        }
        else if(productNameText.getText().equals("Student Laptop")){
            productImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_student_laptopt));
        }
        else if(productNameText.getText().equals("Office Laptop")){
            productImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_office_laptop));
        }
        else if(productNameText.getText().equals("Budget Laptop")){
            productImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_budget_laptop));
        }

        dbHelper.close();

        Button addToCart = (Button) productView.findViewById(R.id.buttonAddCart);
        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Tealeaf.logFormCompletion(true);
                dbHelper.open();
                if(dbHelper.checkProductPurchased(productNameText.getText().toString())){
                    alertView("Item is already in the cart");
                }
                else{
                    Double productPriceAsDouble = Double.parseDouble(productPriceCSV);
                    Double currentPriceAsDouble = Double.parseDouble(dbHelper.getCurrentSubtotal().substring(1)) + productPriceAsDouble;
                    String currentPriceAsString = String.format("%.2f", currentPriceAsDouble);
                    Double currentTaxAsDouble = 0.15*currentPriceAsDouble;
                    String currentTaxAsString = String.format("%.2f", currentTaxAsDouble);
                    String totalString = String.format("%.2f", currentPriceAsDouble + currentTaxAsDouble);
                    dbHelper.updateSubtotal(1, "$" + currentPriceAsString, "$0.00", currentTaxAsString, totalString);
                    dbHelper.addPurchase("$" + productPriceCSV,"$0.00","$0.00","$0.00",productNameText.getText().toString(),"1");
                    alertView("Item added in the cart");
                }

                dbHelper.close();


            }
        });

        return productView;
    }

    private void alertView( String message ) {
        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.setTitle("Item in the cart");
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Back",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dbHelper.open();
                        dbHelper.checkProductPurchased(productNameText.getText().toString());
                        dbHelper.close();
                        dialog.dismiss();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "View Cart",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getActivity(), CartActivity.class);
                        startActivity(intent);
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#5596e6"));
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#5596e6"));
    }

    
    /**
     * {@inheritDoc}
     */
    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onPause() {
        super.onPause();
    }
}
