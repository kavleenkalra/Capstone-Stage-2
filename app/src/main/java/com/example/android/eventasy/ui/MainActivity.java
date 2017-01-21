package com.example.android.eventasy.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.android.eventasy.R;
import com.gordonwong.materialsheetfab.MaterialSheetFab;
import com.gordonwong.materialsheetfab.MaterialSheetFabEventListener;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton fab;
    MaterialSheetFab materialSheetFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpFab();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                materialSheetFab.showSheet();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(materialSheetFab.isSheetVisible())
            materialSheetFab.hideSheet();
        else
            super.onBackPressed();
    }

    private void setUpFab() {

        fab = (FloatingActionButton) findViewById(R.id.fab);
        View sheetView=findViewById(R.id.fab_sheet);
        View overlay=findViewById(R.id.overlay);
        int sheetColor=Color.WHITE;
        int fabColor=R.color.colorAccent;

        //Create material sheet FAB.
        materialSheetFab=new MaterialSheetFab(fab,sheetView,overlay,sheetColor,fabColor);

        //Set material sheet event listener.
        materialSheetFab.setEventListener(new MaterialSheetFabEventListener() {

            @Override
            public void onShowSheet() {
            }

            @Override
            public void onHideSheet() {
            }
        });

        //Set material sheet item click listeners.

        View categoryView=findViewById(R.id.category_view);
        if(categoryView!=null){
            categoryView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    materialSheetFab.hideSheet();
                    Intent intent=new Intent(getApplicationContext(),CategoryActivity.class);
                    startActivity(intent);
                }
            });
        }

        View favoriteView=findViewById(R.id.favorite_view);
        if(favoriteView!=null){
            favoriteView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    materialSheetFab.hideSheet();
                    Intent intent=new Intent(getApplicationContext(),FavouriteEventListActivity.class);
                    startActivity(intent);
                }
            });
        }

    }
}