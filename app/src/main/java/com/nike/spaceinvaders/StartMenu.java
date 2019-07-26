package com.nike.spaceinvaders;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class StartMenu extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_menu);

    }
    //invoked once start button has been pressed
    public void startgame(View view) {
        //Toast.makeText(getApplicationContext(),"Test",Toast.LENGTH_LONG).show();
        Intent intent = new Intent();
        intent.setClass(StartMenu.this, SpaceActivity.class);//Jump from this to SpaceActivity
        this.startActivity(intent);
        //Animation of xml on transition
        overridePendingTransition(R.anim.zoom_in,R.anim.zoom_out);//android.anim.fade_in
    }
}
