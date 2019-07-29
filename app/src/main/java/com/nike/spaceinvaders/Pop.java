package com.nike.spaceinvaders;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;

public class Pop extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();

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
    //resume button pressed
    public void resume(View view){
        Intent intent=this.getIntent();
        Bundle bundle=intent.getExtras();
        //Test only
        bundle.putString("signal","resume");
        intent.putExtras(bundle);
        this.setResult(Activity.RESULT_OK,intent);
        this.finish();
        overridePendingTransition(R.anim.zoom_in,R.anim.zoom_out);//android.anim.fade_in
    }
    //retry button pressed
    public void retry(View view) {
        //TODO: restart game
    }

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.zoom_in,R.anim.zoom_out);
    }
}
