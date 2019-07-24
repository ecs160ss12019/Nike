package com.nike.spaceinvaders;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class StartMenu extends AppCompatActivity {
    TextView startbutton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_menu);

    }

    public void startgame(View view) {
        Toast.makeText(getApplicationContext(),"Test",Toast.LENGTH_LONG).show();
        Intent intent = new Intent();
        intent.setClass(StartMenu.this, SpaceActivity.class);//从MainActivity页面跳转至LoginActivity页面
        this.startActivity(intent);
    }
}
