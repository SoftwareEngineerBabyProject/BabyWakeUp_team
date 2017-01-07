package com.example.wenyu.baby;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends BaseActivity {
    private String selectedLocation;
    private String locationActivitySend;
    private TextView location_text;
    private TextView textDateshow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        location_text = (TextView) findViewById(R.id.location_text);
        Bundle bundle = this.getIntent().getExtras();
        if(bundle!=null) {
            selectedLocation = bundle.getString("selectedLocation");
            setLocationActivitySend(selectedLocation);
            location_text.setText(locationActivitySend);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "鬧鐘設定", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, AlarmActivity.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Date
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String dts=sdf.format(date);
        textDateshow = (TextView)findViewById(R.id.Date);
        textDateshow.setText(dts);
        //抓取日期結束
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);
        menu.findItem(R.id.menu_item_new).setVisible(false);
        menu.findItem(R.id.menu_item_save).setVisible(false);
        menu.findItem(R.id.menu_item_delete).setVisible(false);
        return result;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
             // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, AlarmActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_slideshow) {//location Activity
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, Location.class);
            Bundle bundle = new Bundle();
            bundle.putString("selectedLocation",locationActivitySend);
            intent.putExtras(bundle);
            startActivity(intent);

        } else if (id == R.id.girlsAdd) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, GirlsAddActivity.class);
            startActivity(intent);
            //MainActivity.this.finish();
        }
        else if (id == R.id.guan) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this,developer2.class);
            startActivity(intent);
            //MainActivity.this.finish();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onResume() {
        super.onResume();
        //location bundle ->location_text
        Bundle bundle = this.getIntent().getExtras();
        if(bundle!=null) {
            selectedLocation = bundle.getString("selectedLocation");
            setLocationActivitySend(selectedLocation);
            location_text.setText(locationActivitySend);
        }
    }
    @Override
    public void onStop() {
        super.onStop();
        //location bundle ->location_text
        Bundle bundle = this.getIntent().getExtras();
        if(bundle!=null) {
            selectedLocation = bundle.getString("selectedLocation");
            setLocationActivitySend(selectedLocation);
            location_text.setText(locationActivitySend);
        }
    }
    //location setter & getter
    public void setLocationActivitySend(String location_send){
        locationActivitySend = location_send;
    }
    public String getLocationActivitySend(){
        return locationActivitySend;
    }

}
