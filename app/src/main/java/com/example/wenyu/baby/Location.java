package com.example.wenyu.baby;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class Location extends BaseActivity {
    private Spinner spinner;
    private TextView txt;
    private String selectedLocation;
    private String locationActivitySend;
    private Button button_saved;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);


        spinner= (Spinner)findViewById(R.id.spinner);
        txt= (TextView)findViewById(R.id.textView);
      //從 MainActivity 抓 location
        Bundle bundle = this.getIntent().getExtras();
        if(bundle!=null) {
            selectedLocation = "";
            if(bundle.getString("selectedLocation")!=null)
            selectedLocation = bundle.getString("selectedLocation");
            setLocationActivitySend(selectedLocation);
            txt.setText("目前地區: " + locationActivitySend);

        }


        ArrayAdapter<CharSequence> locationList = ArrayAdapter.createFromResource(Location.this,
                R.array.location,
                android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(locationList);//設定spinner資料來源
        spinner.setOnItemSelectedListener(spinnerListener);
        button_saved = (Button)findViewById(R.id.location_saved);
        button_saved.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Location.this, MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("selectedLocation",selectedLocation);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }

    private Spinner.OnItemSelectedListener spinnerListener =
            new Spinner.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View v, int position, long id){
                    String location_selected = parent.getSelectedItem().toString();
                    selectedLocation = location_selected;
            }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            };

    public void locationClick(View view)
    {
    }



    //location setter & getter
    public void setLocationActivitySend(String location_send){
        locationActivitySend = location_send;
    }
    public String getLocationActivitySend(){
        return locationActivitySend;
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
