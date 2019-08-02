package com.nike.spaceinvaders;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Logger;

public class LeaderboardPage extends AppCompatActivity {
    private static final String TAG = "LeaderboardPage";
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        recyclerView = (RecyclerView) findViewById(R.id.leaderlist);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        String[] tempDate= readFileAsString("leaderboard.txt").split(" ");
        ArrayList<String> myDataset = sortData(tempDate);
        // specify an adapter (see also next example)
        mAdapter = new MyAdapter(myDataset);
        recyclerView.setAdapter(mAdapter);
    }
//sort String
    private ArrayList<String> sortData(String[] tempDate) {
        ArrayList<Integer> temp=new ArrayList<Integer>();
        int num = tempDate.length;
        //change String[] to ArrayList<Int>
        for (int i=0; i < tempDate.length;i++){
            if (tempDate[i] !=""){
                int j = Integer.valueOf(tempDate[i]);
                temp.add(j);
            }
        }
        Collections.sort(temp, Collections.reverseOrder());
        ArrayList<String> rt = new ArrayList<String>();
        for (int i=0; i < num;i++){
            rt.add(String.valueOf(temp.get(i)));
        }
        return rt;
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

    public String readFileAsString(String fileName) {
        Context context = getApplicationContext();
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        BufferedReader in = null;

        try {
            in = new BufferedReader(new FileReader(new File(context.getFilesDir(), fileName)));
            while ((line = in.readLine()) != null) stringBuilder.append(line);

        } catch (FileNotFoundException e) {
            Log.e(TAG, String.valueOf(e));
        } catch (IOException e) {
            Log.e(TAG, String.valueOf(e));
        }

        return stringBuilder.toString();
    }
}
