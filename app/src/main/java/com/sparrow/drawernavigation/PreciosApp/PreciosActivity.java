package com.sparrow.drawernavigation.PreciosApp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.sparrow.drawernavigation.PromvApp.ProVMainActivity;
import com.sparrow.drawernavigation.R;

public class PreciosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_precios);
    }

    public void precios(View view) {
        startActivity(new Intent(getApplicationContext(), ReportPriceActivity.class));
    }
}