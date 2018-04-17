package com.example.viltsu.birdbonging;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class BirdInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.birdinfo_view, new BirdInfoFragment())
                .commit();
    }
}
