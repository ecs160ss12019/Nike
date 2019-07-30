package com.nike.spaceinvaders;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

public class SettingPage extends AppCompatActivity {

    public int gravityflag;//0 is off, 1 is on
    public int ailevel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_page);
        //init
        gravityflag=1;
        ailevel=0;
        getSettingValue();
    }

    private void getSettingValue() {
        SeekBar AiLevelBar= findViewById(R.id.ailevelbar);
        SeekBar GravityBar= findViewById(R.id.gravitybar);
        AiLevelBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ailevel=progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        GravityBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                gravityflag=progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

    }

    public void back(View view){
        Intent intent=this.getIntent();
        Bundle bundle=new Bundle();
        //Test only
        bundle.putString("AiLevel",String.valueOf(ailevel));
        bundle.putString("GravityFlag",String.valueOf(gravityflag));
        intent.putExtras(bundle);
        this.setResult(Activity.RESULT_OK, intent);
        this.finish();
        overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);//slide_right
    }
//Example of return value
/*    Intent intent=this.getIntent();
    Bundle bundle=intent.getExtras();
    //Test only
        bundle.putString("signal","resume");
        intent.putExtras(bundle);
        this.setResult(Activity.RESULT_OK, intent);
        this.finish();
    overridePendingTransition(R.anim.zoom_in,R.anim.zoom_out);//android.anim.fade_in*/
}
