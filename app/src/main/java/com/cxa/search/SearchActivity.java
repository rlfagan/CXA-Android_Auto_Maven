package com.cxa.search;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.cxa.CartActivity;
import com.cxa.MainActivity;
import com.cxa.MoreActivity;
import com.cxa.R;
import com.cxa.rows.CustomBaseAdapter;
import com.cxa.rows.RowItem;
import com.cxa.util.UIUtil;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;
import com.tl.uic.Tealeaf;
import java.util.ArrayList;

/**
 * Shows a list that can be filtered in-place with a SearchView in non-iconified mode.
 */
public class SearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private SearchView mSearchView;
    private ListView mListView;
    private ArrayAdapter<String> mAdapter;

    private final String[] mStrings = {
            "Tablets", "Furniture", "Laptops"
    };

    BottomBar bottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       UIUtil.setupHeader(getSupportActionBar());

        setContentView(R.layout.activity_search);

        mSearchView = (SearchView) findViewById(R.id.search_view);
        mListView = (ListView) findViewById(R.id.list_view);

        ArrayList<RowItem> items = new ArrayList<RowItem>();

        items.add(new RowItem(R.drawable.ic_multimedia_tablet, "Tablets"));
        items.add(new RowItem(R.drawable.ic_style_home, "Furniture"));
        items.add(new RowItem(R.drawable.ic_office_laptop, "Laptops"));


        CustomBaseAdapter adapter = new CustomBaseAdapter(SearchActivity.this, items, true);

        mListView.setAdapter(adapter);
        mListView.setTextFilterEnabled(true);

        setupSearchView();

        bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        loadBottomBar();
        
    }


    @Override
    public void onResume(){
        super.onResume();
        bottomBar.selectTabAtPosition(1);
        overridePendingTransition(0,0);

    }

    public static void addFocusAndRegister(TextView textView, Activity activity) {

        textView.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(v, InputMethodManager.SHOW_FORCED);

                    KeyboardView keyboardView = new KeyboardView(v.getContext().getApplicationContext(), null);
                    Tealeaf.logEvent(keyboardView , Tealeaf.TLF_UI_KEYBOARD_DID_SHOW_NOTIFICATION);
                    Tealeaf.logEvent(v, Tealeaf.TLF_ON_FOCUS_CHANGE_IN);
                } else {
                    // SearchView doesn't fire event

                }
            }
        });

        Tealeaf.registerFormField(textView, activity);
    }

    private void setupSearchView() {
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setQueryHint("Search CXA");

        int id = mSearchView.getContext()
                .getResources()
                .getIdentifier("android:id/search_src_text", null, null);
        EditText editText = (EditText) mSearchView.findViewById(id);

        addFocusAndRegister(editText, this);
    }

    @Override
    public boolean onQueryTextChange(String newText)
    {
        // Log the search result screen
        Tealeaf.logScreenLayout(this, "SearchDynamicUpdate", 5);
        if (TextUtils.isEmpty(newText)) {
            mListView.clearTextFilter();
        } else {
            mListView.setFilterText(newText);
        }
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query)
    {
        com.tl.uic.Tealeaf.logEvent(mSearchView, com.tl.uic.Tealeaf.TLF_ON_FOCUS_CHANGE_OUT);
        InputMethodManager imm = (InputMethodManager) mSearchView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mSearchView.getWindowToken(), 0);

        KeyboardView keyboardView = new KeyboardView(mSearchView.getContext().getApplicationContext(), null);
        Tealeaf.logEvent(keyboardView , Tealeaf.TLF_UI_KEYBOARD_DID_HIDE_NOTIFICATION);

        return false;
    }


    public void loadBottomBar(){
        bottomBar.selectTabAtPosition(1);

        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(int tabId) {
                if (tabId == R.id.tab_home) {
                    Intent intent = new Intent(SearchActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                else if (tabId == R.id.tab_shopping) {
                    Intent intent = new Intent(SearchActivity.this, CartActivity.class);
                    startActivity(intent);
                }
                else if (tabId == R.id.tab_more) {
                    Intent intent = new Intent(SearchActivity.this, MoreActivity.class);
                    startActivity(intent);
                }
            }
        });

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent e) {
        return super.dispatchTouchEvent(e);
    }
}


