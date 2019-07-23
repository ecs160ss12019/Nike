package com.nike.spaceinvaders;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class StartingPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting_page);
    }


    public void GameStart(View view) {
        Intent intent=new Intent(this,SpaceActivity.class);
        startActivity(intent);
    }
}
