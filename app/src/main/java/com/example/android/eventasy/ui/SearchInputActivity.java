package com.example.android.eventasy.ui;

import android.app.SearchManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.android.eventasy.R;

public class SearchInputActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_input);
        onSearchRequested();

        Toolbar toolbar = (Toolbar) findViewById(R.id.search_input_toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeActionContentDescription(R.string.go_back_main_screen);
            toolbar.findViewById(R.id.search_toolbar_layout).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onSearchRequested();
                }
            });
        }

    }
}
