package com.android.keeper;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class SettingsActivity extends AppCompatActivity {

    private Spinner clockFormatSpinner;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor sharedPreEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        sharedPreferences=getSharedPreferences("keeper_settings", Context.MODE_PRIVATE);
        sharedPreEditor=sharedPreferences.edit();

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Settings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.thirdColor));
        setSettingsOptions();

        clockFormatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String option=parent.getItemAtPosition(position).toString();
                switch(option){
                    case "Automatic":
                        sharedPreEditor.putString("clock_format","auto");
                        break;
                    case "24 Hours":
                        sharedPreEditor.putString("clock_format","24hr");
                        break;
                    case "12 Hours":
                        sharedPreEditor.putString("clock_format","12hr");
                        break;
                }
                sharedPreEditor.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    private void setSettingsOptions(){
        clockFormatSpinner=(Spinner) findViewById(R.id.settings_clock_format_spinner);
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(getApplicationContext(),R.array.clock_format_options,R.layout.item_spinner);
        clockFormatSpinner.setAdapter(adapter);
        String option=sharedPreferences.getString("clock_format","auto");
        switch(option){
            case "auto":
                clockFormatSpinner.setSelection(0);
                break;
            case "24hr":
                clockFormatSpinner.setSelection(1);
                break;
            case "12hr":
                clockFormatSpinner.setSelection(2);
                break;
        }

    }
}
