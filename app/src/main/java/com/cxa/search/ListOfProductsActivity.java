package com.cxa.search;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.cxa.CartActivity;
import com.cxa.MainActivity;
import com.cxa.MoreActivity;
import com.cxa.ProductDetailsFragment;
import com.cxa.R;
import com.cxa.cardUI.Card;
import com.cxa.cardUI.CardArrayAdapter;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;

public class ListOfProductsActivity extends AppCompatActivity {

    private String productType = "";
    private CardArrayAdapter cardArrayAdapter;
    private ListView listView;
    private ArrayList<String> productNames = new ArrayList<String>();
    private ArrayList<String> productPrice = new ArrayList<String>();
    private ArrayList<Float> productRating = new ArrayList<>();
    private ArrayList<String> numberRatings = new ArrayList<String>();
    private ArrayList<String> brandNames = new ArrayList<String>();
    private ArrayList<Integer> imageIds = new ArrayList<>();

    TextView title_text;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        Bundle b = getIntent().getExtras();
        String value = b.getString("key");

        productType = value;

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_header_layout);
        title_text = (TextView) findViewById(R.id.my_title_text);
        title_text.setText(productType);
        title_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadProductInformation(1);
            }
        });

        getWindow().getDecorView().setBackgroundColor(Color.WHITE);

        ScrollView scrollView = new ScrollView(this);

        setListOfProducts();

        loadBottomBar();
    }

    public void setListOfProducts(){
        final List<String[]> list = new ArrayList<String[]>();

        String next[] = {};
        try {
            InputStreamReader csvStreamReader = new InputStreamReader(getApplication().getAssets().open(productType + ".csv"));

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
            productNames.add(list.get(i)[0]);
            productPrice.add(list.get(i)[1]);
            productRating.add(Float.valueOf(list.get(i)[3]));
            numberRatings.add(list.get(i)[4]);
            brandNames.add(list.get(i)[5]);
            imageIds.add(getResources().getIdentifier(list.get(i)[6], "drawable", getApplicationContext().getPackageName()));
        }

        listView = (ListView) findViewById(R.id.card_listView);

        listView.addHeaderView(new View(this));
        listView.addFooterView(new View(this));

        cardArrayAdapter = new CardArrayAdapter(getApplicationContext(), R.layout.custom_card_holder);

        Card card1 = new Card(productNames.get(0), productPrice.get(0), productRating.get(0), numberRatings.get(0), brandNames.get(0), imageIds.get(0), new it.gmariotti.cardslib.library.internal.Card.OnCardClickListener(){
            @Override
            public void onClick(it.gmariotti.cardslib.library.internal.Card card, View view) {
                listView.setVisibility(View.GONE);
                loadProductInformation(0);
            }
        });

        Card card2 = new Card(productNames.get(1), productPrice.get(1), productRating.get(1), numberRatings.get(1), brandNames.get(1), imageIds.get(1), new it.gmariotti.cardslib.library.internal.Card.OnCardClickListener(){
            @Override
            public void onClick(it.gmariotti.cardslib.library.internal.Card card, View view) {
                listView.setVisibility(View.GONE);
                loadProductInformation(1);
            }
        });

        Card card3 = new Card(productNames.get(2), productPrice.get(2), productRating.get(2), numberRatings.get(2), brandNames.get(2), imageIds.get(2), new it.gmariotti.cardslib.library.internal.Card.OnCardClickListener(){
            @Override
            public void onClick(it.gmariotti.cardslib.library.internal.Card card, View view) {
                listView.setVisibility(View.GONE);
                loadProductInformation(2);
            }
        });

        cardArrayAdapter.add(card1);
        cardArrayAdapter.add(card2);
        cardArrayAdapter.add(card3);

        listView.setAdapter(cardArrayAdapter);
    }

    public boolean loadProductInformation(int productRow){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, productFromRow(productRow, productType));
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        return true;
    }

    public void loadBottomBar(){
        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.selectTabAtPosition(1);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(int tabId) {
                if (tabId == R.id.tab_home) {
                    Intent intent = new Intent(ListOfProductsActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                else if (tabId == R.id.tab_shopping) {
                    Intent intent = new Intent(ListOfProductsActivity.this, CartActivity.class);
                    startActivity(intent);
                }
                else if (tabId == R.id.tab_more) {
                    Intent intent = new Intent(ListOfProductsActivity.this, MoreActivity.class);
                    startActivity(intent);
                }
            }
        });

    }


    public ProductDetailsFragment productFromRow(int productRow, String productType){
        Bundle bundle=new Bundle();
        bundle.putString("message", Integer.toString(productRow));
        bundle.putString("productType", productType);
        ProductDetailsFragment fragobj = new ProductDetailsFragment();
        fragobj.setArguments(bundle);
        return fragobj;
    }

    @Override
    public void onResume(){
        super.onResume();
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
