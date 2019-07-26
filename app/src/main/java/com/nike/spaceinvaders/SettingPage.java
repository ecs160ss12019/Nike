package com.nike.spaceinvaders;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class SettingPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_page);
    }

    public void back(View view){
        //TODO: should bring back some parameter to Spacegame
        finish();
        overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
    }
}
