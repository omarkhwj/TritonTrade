package com.tmnt.tritontrade.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.tmnt.tritontrade.R;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }


    public void sendToDevTeam(View view){
        Intent intent = new Intent(this, DeveloperTeam.class);
        startActivity(intent);
    }
}
