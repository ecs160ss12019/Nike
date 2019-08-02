package com.nike.spaceinvaders;

/*
 Handle the pop-up window
 */


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Logger;


public class Pop extends AppCompatActivity {
    private static final Object TAG = "POP";
    public String str;//store signal
    public int ScoreRecord;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();

        Intent i = getIntent();
        String sig = i.getStringExtra("insignal");//in-signal differ from signal that will be output
        ScoreRecord = Integer.valueOf(i.getStringExtra("score"));
        this.str=sig;
        switch (sig){
            case "pause":
                setContentView(R.layout.pop_pause);
                break;
            case "gameover":
                setContentView(R.layout.pop_gameover);
                RecordScore(ScoreRecord);
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

    private void RecordScore(int scoreRecord) {
        Context context = getApplicationContext();
        try {
            FileWriter out = new FileWriter(new File(context.getFilesDir(), "leaderboard.txt"));
            out.write(String.valueOf(scoreRecord));
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //resume button pressed
    public void resume(View view){
        Intent intent=this.getIntent();
        Bundle bundle=new Bundle();
        bundle.putString("signal","resume");
        intent.putExtras(bundle);
        this.setResult(Activity.RESULT_OK,intent);
        this.finish();
        overridePendingTransition(R.anim.zoom_in,R.anim.zoom_out);//android.anim.fade_in
    }
    //retry button pressed
    public void retry(View view) {
        Intent intent=this.getIntent();
        Bundle bundle= new Bundle();
        //Test only
        bundle.putString("signal","restart");
        intent.putExtras(bundle);
        this.setResult(Activity.RESULT_OK,intent);
        this.finish();
        overridePendingTransition(R.anim.zoom_in,R.anim.zoom_out);//android.anim.fade_in
    }
    //when Back to Title is pressed
    public void backToTitle(View view){
        Intent intent=this.getIntent();
        Bundle bundle= new Bundle();
        //Test only
        bundle.putString("signal","backtotitle");
        intent.putExtras(bundle);
        this.setResult(Activity.RESULT_OK,intent);
        this.finish();
        overridePendingTransition(R.anim.zoom_in,R.anim.zoom_out);//android.anim.fade_in
    }


    @Override
    public void finish(){

        Intent intent=this.getIntent();
        Bundle bundle=intent.getExtras();
        if (!bundle.containsKey("signal")){
            if (this.str.equals("pause")) {
                bundle.putString("signal","resume");
                intent.putExtras(bundle);
                this.setResult(Activity.RESULT_OK,intent);
            }else {
                bundle.putString("signal","restart");
                intent.putExtras(bundle);
            }
        }
        super.finish();
        overridePendingTransition(R.anim.zoom_in,R.anim.zoom_out);
    }
}
