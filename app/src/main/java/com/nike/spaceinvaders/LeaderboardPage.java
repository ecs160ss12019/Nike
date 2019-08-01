package com.nike.spaceinvaders;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

public class LeaderboardPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
    }
    public void back(View view){
        this.finish();
        overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);//slide_right
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);//slide_right
    }
}
