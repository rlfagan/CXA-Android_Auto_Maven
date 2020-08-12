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

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;

import com.cxa.rows.CustomBaseAdapter;
import com.cxa.rows.RowItem;
import com.cxa.search.SearchActivity;
import com.cxa.util.UIUtil;
import com.ibm.eo.EOCore;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;
import com.tl.uic.Tealeaf;
import com.tl.uic.TealeafEOLifecycleObject;

import java.util.ArrayList;

/**
 * Shows a list that can be filtered in-place with a SearchView in non-iconified mode.
 */
public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private ListView mListView;
    private DatabaseAdapter dbHelper;
    BottomBar bottomBar;
    CustomBaseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Best practice on when to Enable Tealeaf library
        if (Build.VERSION.SDK_INT >= 16 && Build.VERSION.SDK_INT <= 30) {
            Tealeaf tealeaf = new Tealeaf(this.getApplication());
            Tealeaf.enable();
        }

        UIUtil.setupHeader(getSupportActionBar());

        setContentView(R.layout.activity_main);

        String ibm_id = EOCore.getConfigItemString("ibmId", TealeafEOLifecycleObject.getInstance());
        Tealeaf.logCustomEvent(ibm_id);

        initializeTotal();

        setupList();

        bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        loadBottomBar();
    }

    @Override
    public void onResume() {
        super.onResume();
        overridePendingTransition(0, 0);
        bottomBar.selectTabAtPosition(0);
    }

    public void setupList() {
        ArrayList<RowItem> items = new ArrayList<>();

        RowItem tabletsRow = new RowItem(R.drawable.ic_multimedia_tablet, "Tablets");
        RowItem furnitureRow = new RowItem(R.drawable.ic_style_home, "Furniture");
        RowItem laptopsRow = new RowItem(R.drawable.ic_office_laptop, "Laptops");

        items.add(tabletsRow);
        items.add(furnitureRow);
        items.add(laptopsRow);

        mListView = (ListView) findViewById(R.id.list_view);
        adapter = new CustomBaseAdapter(this, items, false);
        mListView.setAdapter(adapter);

        mListView.setTextFilterEnabled(true);
    }


    public boolean onQueryTextChange(String newText) {
        if (TextUtils.isEmpty(newText)) {
            mListView.clearTextFilter();
        } else {
            mListView.setFilterText(newText.toString());
        }
        return true;
    }

    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    public void loadBottomBar() {
        bottomBar.selectTabAtPosition(0);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(int tabId) {
                if (tabId == R.id.tab_search) {
                    Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                    startActivity(intent);
                }
                if (tabId == R.id.tab_shopping) {
                    Intent intent = new Intent(MainActivity.this, CartActivity.class);
                    startActivity(intent);
                } else if (tabId == R.id.tab_more) {
                    Intent intent = new Intent(MainActivity.this, MoreActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    public void initializeTotal() {
        dbHelper = new DatabaseAdapter(this);
        dbHelper.open();

        if (dbHelper.getElementsInDB() == 0) {
            dbHelper.addPurchase("$0.00", "$0.00", "$0.00", "$0.00", "total", "1");
        }

        dbHelper.close();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent e) {
        return super.dispatchTouchEvent(e);
    }
}


