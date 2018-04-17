package com.example.viltsu.birdbonging;

import android.app.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.viltsu.birdbonging.data.BirdContract;
import com.example.viltsu.birdbonging.data.BirdHelper;
import java.util.ArrayList;

/**
 * Created by ville-pekkapalmgren on 05/03/18.
 */

public class CustomAdapter extends ArrayAdapter<Bird> {

    private int BirdId;
    private SQLiteDatabase db;
    private BirdHelper birdie;
    private Context Mycontext;

    public CustomAdapter(Activity context, ArrayList<Bird> b){
        super(context, 0, b);
        this.Mycontext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View scoreItem = convertView;

        if(scoreItem == null){
            scoreItem = LayoutInflater.from(getContext()).inflate(
                    R.layout.score_item_view, parent, false
            );
        }

        final Bird getBirdie = getItem(position);

        BirdId = getBirdie.getDbId();

        Log.i("Linnun id adapterissa: ", Integer.toString(BirdId));

        TextView first = scoreItem.findViewById(R.id.birdtype);

        first.setText(getBirdie.getBirdType());

        TextView second = scoreItem.findViewById(R.id.message);

        second.setText(getBirdie.getBirdmessage());

        ImageView bird = scoreItem.findViewById(R.id.statsimage);

        bird.setImageResource(getBirdie.getImgResId());

        final Button deletebtn = scoreItem.findViewById(R.id.deletebtn);

        deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder alertDialogBuilder;
                alertDialogBuilder = new AlertDialog.Builder(getContext());
                alertDialogBuilder.setTitle("Modify " + getBirdie.getBirdType() + " entry from database!");
                alertDialogBuilder
                        .setMessage("Modifying id number " + getBirdie.getDbId() + " from database." )
                        .setCancelable(true)
                        .setPositiveButton("Add a comment!", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String message = "Hello, you caught a bird!";
                                updateBird(message);
                                Toast.makeText(getContext(), "Comment added - redirecting to start page", Toast.LENGTH_SHORT).show();
                                //sivun päivitys tyyli nyt: paluu kotivalikkoon ja sit käyttäjä navigoi takas
                                Intent home = new Intent(getContext(), WelcomeView.class);
                                Mycontext.startActivity(home);
                            }
                        })
                        .setNegativeButton("Delete", new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id){
                                deleteId();
                                notifyDataSetChanged();
                                Toast.makeText(getContext(), "Target deleted - redirecting to start page", Toast.LENGTH_SHORT).show();
                                //sivun päivitys tyyli nyt: paluu kotivalikkoon ja sit navigoi takas
                                Intent home = new Intent(getContext(), WelcomeView.class);
                                Mycontext.startActivity(home);
                            }
                        });
                AlertDialog alert = alertDialogBuilder.create();
                alert.show();
            }
        });
        return scoreItem;
    }

    public void updateBird(String message){

        birdie = new BirdHelper(getContext());
        db = birdie.getWritableDatabase();

        ContentValues contval = new ContentValues();
        contval.put(BirdContract.BirdEntry.COLUMN_BIRD_MESSAGE, message);

        String [] whereArgs = {Integer.toString(BirdId)};

        db.update(BirdContract.BirdEntry.TABLE_NAME, contval,BirdContract.BirdEntry._ID+"= ?",whereArgs);
        db.close();
    }

    public void deleteId(){

        birdie = new BirdHelper(getContext());
        db = birdie.getWritableDatabase();

        String [] whereArgs = {Integer.toString(BirdId)};

        db.delete(BirdContract.BirdEntry.TABLE_NAME, BirdContract.BirdEntry._ID+"= ?", whereArgs);
        db.close();
    }
}
