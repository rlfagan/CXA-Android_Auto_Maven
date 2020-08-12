package com.cxa;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.ibm.eo.EOCore;
import com.cxa.search.SearchActivity;
import com.cxa.util.UIUtil;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;
import com.tl.uic.Tealeaf;
import com.tl.uic.TealeafEOLifecycleObject;

public class MoreActivity extends AppCompatActivity {
    EditText ibm_id_text;
    EditText post_url_text;
    EditText killswitch_url_text;
    EditText appkey_text;
    BottomBar bottomBar;

    Button saveSettings_button;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);

        UIUtil.setupHeader(getSupportActionBar());

        getWindow().getDecorView().setBackgroundColor(Color.WHITE);

        ibm_id_text = (EditText) findViewById(R.id.ibm_id_text);
        ibm_id_text.setText(EOCore.getConfigItemString("ibmId", TealeafEOLifecycleObject.getInstance()));

        post_url_text = (EditText) findViewById(R.id.editText_post_url);
        post_url_text.setText(EOCore.getConfigItemString(Tealeaf.TLF_POST_MESSAGE_URL, TealeafEOLifecycleObject.getInstance()));

        killswitch_url_text = (EditText) findViewById(R.id.editText_killswitch_url);
        killswitch_url_text.setText(EOCore.getConfigItemString(Tealeaf.TLF_KILL_SWITCH_URL, TealeafEOLifecycleObject.getInstance()));

        appkey_text = (EditText) findViewById(R.id.editText_appkey);
        appkey_text.setText(EOCore.getConfigItemString(Tealeaf.TLF_APP_KEY, TealeafEOLifecycleObject.getInstance()));

        saveSettings_button = (Button) findViewById(R.id.button_save_settings);
        saveSettings_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean configModified = false;
                if (!post_url_text.getText().equals((EOCore.getConfigItemString(Tealeaf.TLF_POST_MESSAGE_URL, TealeafEOLifecycleObject.getInstance())))) {
                    EOCore.updateConfig(Tealeaf.TLF_POST_MESSAGE_URL, post_url_text.getText().toString(), TealeafEOLifecycleObject.getInstance());
                    configModified = true;
                }
                if (!killswitch_url_text.getText().equals((EOCore.getConfigItemString(Tealeaf.TLF_KILL_SWITCH_URL, TealeafEOLifecycleObject.getInstance())))) {
                    EOCore.updateConfig(Tealeaf.TLF_KILL_SWITCH_URL, killswitch_url_text.getText().toString(), TealeafEOLifecycleObject.getInstance());
                    configModified = true;
                }
                if (!appkey_text.getText().equals((EOCore.getConfigItemString(Tealeaf.TLF_APP_KEY, TealeafEOLifecycleObject.getInstance())))) {
                    EOCore.updateConfig(Tealeaf.TLF_APP_KEY, appkey_text.getText().toString(), TealeafEOLifecycleObject.getInstance());
                    configModified = true;
                }

                if (configModified) {
                    Tealeaf.disable();
                    Tealeaf.enable();
                }

                Tealeaf.logFormCompletion(true);

                Intent intent = new Intent(MoreActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


        bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        loadBottomBar();

    }

    @Override
    public void onResume() {
        super.onResume();
        bottomBar.selectTabAtPosition(3);
        overridePendingTransition(0, 0);

    }


    public void loadBottomBar() {
        bottomBar.selectTabAtPosition(3);

        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(int tabId) {
                if (tabId == R.id.tab_search) {
                    Intent intent = new Intent(MoreActivity.this, SearchActivity.class);
                    startActivity(intent);
                }
                if (tabId == R.id.tab_shopping) {
                    Intent intent = new Intent(MoreActivity.this, CartActivity.class);
                    startActivity(intent);
                } else if (tabId == R.id.tab_home) {
                    Intent intent = new Intent(MoreActivity.this, MainActivity.class);
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
