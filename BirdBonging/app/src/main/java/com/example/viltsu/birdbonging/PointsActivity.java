package com.example.viltsu.birdbonging;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class PointsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.points_view, new PointsFragment())
                .commit();
    }
}
