package com.nike.spaceinvaders;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;

public class Pop extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
/*
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getActionBar().hide();*/

        Intent i = getIntent();
        String sig = i.getStringExtra("signal");
        switch (sig){
            case "pause":
                setContentView(R.layout.pop_pause);
                break;
            case "gameover":
                setContentView(R.layout.pop_gameover);
                break;
            default:
                break;
        }

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.5),(int)(height*.5));


    }
    public void resume(View view){
        finish();
    }

    public void retry(View view) {
        //TODO: restart game
    }
}
