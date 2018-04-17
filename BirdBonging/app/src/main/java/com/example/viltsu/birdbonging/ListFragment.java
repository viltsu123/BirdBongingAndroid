package com.example.viltsu.birdbonging;



import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.viltsu.birdbonging.data.BirdContract;
import com.example.viltsu.birdbonging.data.BirdHelper;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListFragment extends Fragment {

    private BirdHelper birdie;
    private int id;
    private String birdBreed;
    private SQLiteDatabase db;

    public ListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.stat_center, container, false);

        //list for scores:
        ArrayList<Bird> scoreList = new ArrayList<Bird>();

        birdie = new BirdHelper(getActivity());

        db = birdie.getReadableDatabase();
        String [] projection = {
                BirdContract.BirdEntry.COLUMN_BIRD_BREED,
                BirdContract.BirdEntry.COLUMN_BIRD_POINTS,
                BirdContract.BirdEntry.COLUMN_BIRD_PIC,
                BirdContract.BirdEntry._ID,
                BirdContract.BirdEntry.COLUMN_BIRD_MESSAGE
        };
        //String selection = BirdContract.BirdEntry._ID + "<=3";

        Cursor c = db.query(BirdContract.BirdEntry.TABLE_NAME, projection, null, null, null, null, null);
        //TextView display = (TextView) findViewById(R.id.displayStats);

        try{
            int birdBreed = c.getColumnIndex(BirdContract.BirdEntry.COLUMN_BIRD_BREED);
            int birdBonged = c.getColumnIndex(BirdContract.BirdEntry.COLUMN_BIRD_POINTS);
            int birdPic = c.getColumnIndex(BirdContract.BirdEntry.COLUMN_BIRD_PIC);
            int birdId = c.getColumnIndex(BirdContract.BirdEntry._ID);
            int birdMessage = c.getColumnIndex(BirdContract.BirdEntry.COLUMN_BIRD_MESSAGE);
            while(c.moveToNext()){
                String breed = c.getString(birdBreed);
                String bonged = c.getString(birdBonged);
                int imgResId = c.getInt(birdPic);
                int Birdid = c.getInt(birdId);
                Log.i("Linnun ID kannassa: ", Integer.toString(Birdid));
                String message = c.getString(birdMessage);

                scoreList.add(new Bird(breed, bonged, imgResId, Birdid, message));


                /*
                Tapa saada dip arvot inteistä näkymän ohjelmointia varten:
                int dp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());
                img.setMaxWidth(dp);
                img.setMaxHeight(dp);
                LinearLayout rl = (LinearLayout) findViewById(R.id.linearstats);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams. WRAP_CONTENT, dp, 1);

                rl.addView(img, lp);
                */
            }
        }finally{
            c.close();
        }

        //scoreList on nyt täysi ja sitten tulostukseen:
        CustomAdapter statsAdapter = new CustomAdapter(getActivity(), scoreList);

        ListView listView = rootView.findViewById(R.id.statlistview);

        listView.setAdapter(statsAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getActivity(), "Hello",Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }

}
