package com.example.wenyu.baby;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;

import com.example.wenyu.baby.preferences.AlarmPreferencesActivity;
import com.example.wenyu.baby.service.AlarmServiceBroadcastReciever;

import java.lang.reflect.Field;

//import com.example.wenyu.baby.service.AlarmServiceBroadcastReciever;

/**
 * Created by wenyu on 2016/12/6.
 */
public class BaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,android.view.View.OnClickListener {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if(menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception ex) {
            // Ignore
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new:
                Intent newAlarmIntent = new Intent(this, AlarmPreferencesActivity.class);
                startActivity(newAlarmIntent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {//location Activity
            Intent intent = new Intent();
            intent.setClass(this, Location.class);
           // Bundle bundle = new Bundle();
           // bundle.putString("selectedLocation",locationActivitySend);
           // intent.putExtras(bundle);
            startActivity(intent);

        } else if (id == R.id.girlsAdd) {
            Intent intent = new Intent();
            intent.setClass(this, GirlsAddActivity.class);
            startActivity(intent);
            //MainActivity.this.finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    protected void callMathAlarmScheduleService() {
        Intent mathAlarmServiceIntent = new Intent(this, AlarmServiceBroadcastReciever.class);
        sendBroadcast(mathAlarmServiceIntent, null);
    }

    @Override
    public void onClick(View v) {

    }
}
