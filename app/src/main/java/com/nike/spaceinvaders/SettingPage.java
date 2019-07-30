package com.nike.spaceinvaders;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

public class SettingPage extends AppCompatActivity {

    SeekBar AiLevelBar,GravityBar;
    public boolean gravityflag;//0 is off, 1 is on
    public int ailevel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_page);
        //init
        AiLevelBar= findViewById(R.id.ailevelbar);
        GravityBar=findViewById(R.id.gravitybar);
        Bundle bundle=getIntent().getExtras();
        gravityflag=bundle.getBoolean("GravityFlag");
        ailevel=bundle.getInt("AiLevel");

        changeSeekBar();
        getSettingValue();
    }

    //it can save changed seekbar
    private void changeSeekBar() {
        //reload saved value
        AiLevelBar.setProgress(ailevel);
        GravityBar.setProgress(gravityflag ?1:0);
    }
    //two listener for changed seekbar
    private void getSettingValue() {
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
                gravityflag =(progress != 0);
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
        bundle.putInt("AiLevel",ailevel);
        bundle.putBoolean("GravityFlag",gravityflag);
        intent.putExtras(bundle);
        this.setResult(Activity.RESULT_OK, intent);
        this.finish();
        overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);//slide_right
    }

}
