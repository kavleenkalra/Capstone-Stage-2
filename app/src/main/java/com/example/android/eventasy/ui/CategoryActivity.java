package com.example.android.eventasy.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.android.eventasy.R;

public class CategoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        Toolbar toolbar=(Toolbar)findViewById(R.id.category_toolbar);
        if(toolbar!=null){
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeActionContentDescription(R.string.go_back_main_screen);
        }

        if(savedInstanceState==null){
            CategoryFragment categoryFragment=new CategoryFragment();
            if(!categoryFragment.isAdded()){
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.activity_category,categoryFragment)
                        .commit();
            }
        }
    }
}
