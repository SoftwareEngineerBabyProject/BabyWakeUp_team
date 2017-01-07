package com.example.wenyu.baby;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class GirlsAddActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.girls_main);


    }

    public void voiceClick(View view)
    {
       new AlertDialog.Builder(this).setItems(
               new String[] { "本地上傳", "開啟錄音機" }, null).setNegativeButton(
               "確定", null).show();
    }

    public void newSisClick(View view)
    {
        //連接到主畫面
    }

    public void imageClick(View view)
    {
        new AlertDialog.Builder(this).setItems(
                new String[] { "本地上傳", "開啟相機" }, null).setNegativeButton(
                "確定", null).show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);
        menu.findItem(R.id.menu_item_new).setVisible(false);
        menu.findItem(R.id.menu_item_save).setVisible(false);
        menu.findItem(R.id.menu_item_delete).setVisible(false);
        return result;
    }
}
