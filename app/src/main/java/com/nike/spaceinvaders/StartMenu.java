package com.nike.spaceinvaders;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class StartMenu extends AppCompatActivity {

    //two parameter will send to spacegame
    private int AiLevel;
    private boolean GravityFlag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_menu);
        //init
        AiLevel=0;
        GravityFlag=true;
    }
    //invoked once start button has been pressed
    public void startgame(View view) {
        Intent intent = new Intent();
        intent.setClass(StartMenu.this, SpaceActivity.class);//Jump from this to SpaceActivity
        //input parameter to spaceActivity
        Bundle bundle=new Bundle();
        bundle.putBoolean("GravityFlag",this.GravityFlag);
        bundle.putInt("AiLevel",this.AiLevel);
        intent.putExtras(bundle);
        this.startActivity(intent);
        //Animation of xml on transition
        overridePendingTransition(R.anim.zoom_in,R.anim.zoom_out);//android.anim.fade_in
    }

    public void settingpage(View view){
        Intent intent = new Intent();
        intent.setClass(StartMenu.this, SettingPage.class);//Jump from this to SpaceActivity
        Bundle bundle=new Bundle();
        bundle.putInt("AiLevel",this.AiLevel);
        bundle.putBoolean("GravityFlag",this.GravityFlag);
        intent.putExtras(bundle);
        startActivityForResult(intent,0);
        //Animation of xml on transition
        overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);//android.anim.fade_in
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bundle bundle=data.getExtras();
        //get setting from setting page
        int AiLevel = bundle.getInt("AiLevel");
        Boolean GravityFlag = bundle.getBoolean("GravityFlag");

        this.AiLevel=AiLevel;
        this.GravityFlag=(GravityFlag);
        //TODO: delete following test after finishing
        Toast.makeText(getApplicationContext(),"AiLevel: "+String.valueOf(this.AiLevel)+" Gravity: "+String.valueOf(this.GravityFlag),Toast.LENGTH_LONG).show();
    }
}
