package com.example.android.eventasy.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.android.eventasy.R;

public class EventListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.event_list_toolbar);
        if(toolbar!=null){
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeActionContentDescription(R.string.go_back_categories_screen);
        }

        if(savedInstanceState==null){
            Bundle args=new Bundle();
            args.putParcelable("CategoryObject",getIntent().getExtras().getParcelable("CategoryObject"));

            EventListFragment eventListFragment=new EventListFragment();
            eventListFragment.setArguments(args);
            if(!eventListFragment.isAdded()){
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.activity_event_list,eventListFragment)
                        .commit();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_event_list,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;

            case R.id.event_list_settings:
                Intent intent=new Intent(this,SettingsActivity.class);
                startActivity(intent);
                 break;
        }
        return true;
    }
}
