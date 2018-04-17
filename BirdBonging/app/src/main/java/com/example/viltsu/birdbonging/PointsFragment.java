package com.example.viltsu.birdbonging;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.viltsu.birdbonging.data.BirdContract;
import com.example.viltsu.birdbonging.data.BirdHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 */
public class PointsFragment extends Fragment {

    private SQLiteDatabase db;
    private BirdHelper birdie;
    private int totalPoints;
    private String br = System.getProperty("line.separator");



    public PointsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_points, container, false);

        birdie = new BirdHelper(getActivity());
        db = birdie.getReadableDatabase();

        String [] projection = {
                BirdContract.BirdEntry.COLUMN_BIRD_POINTS
        };

        Cursor c = db.query(BirdContract.BirdEntry.TABLE_NAME, projection, null, null, null, null, null);

        try{

            int birdPoints = c.getColumnIndex(BirdContract.BirdEntry.COLUMN_BIRD_POINTS);

            while(c.moveToNext()){
                totalPoints += c.getInt(birdPoints);
            }

        }finally{
            c.close();
        }

        TextView textone = rootView.findViewById(R.id.pointstext);
        if(totalPoints == 0){
            textone.setText("You have not gathered any points yet :( get playing!");
        }else {
            textone.setText("You have " + totalPoints + " points! Well done!");
        }

        ImageView img = rootView.findViewById(R.id.pointsimage);

        if(totalPoints <= 1000){
            img.setImageResource(R.drawable.duck);
        }else if(totalPoints >= 10000){
            img.setImageResource(R.drawable.mallord);
        }

        ImageView clickr = rootView.findViewById(R.id.pointsimage);
        clickr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmail();
            }
        });

        return rootView;
    }

    //unused method:
    boolean saveBitmapToFile(File dir, String fileName, Bitmap bm,
                             Bitmap.CompressFormat format, int quality) {

            File imageFile = new File(dir, fileName);

            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(imageFile);

                bm.compress(format, quality, fos);

                fos.close();
                Log.i("Filee tallennus", "Tallennus onnistui");
                return true;
            } catch (IOException e) {
                Log.i("Epäonnistui", "tallennus epäonnistui");
                Log.e("app", e.getMessage());
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
            return false;
    }

    public void sendEmail(){
        if(totalPoints >= 1000 && totalPoints <= 2000){

            //tehdään ensin Bitmap; Kuvan teko ei onnistunut, liitetään onnittelu viesti vain...
            /*
            Bitmap duck = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.duck);
            //pyydetään lupaa kirjoittaa muistiin:

            //Sit tallennetaan se paikalliseen muistiin puhelimessa
            saveBitmapToFile(Environment.getDataDirectory(), "ImageDuck", duck, Bitmap.CompressFormat.PNG, 100);
            */
            /**
             * @param dir you can get from many places like Environment.getExternalStorageDirectory() or mContext.getFilesDir() depending on where you want to save the image.
             * @param fileName The file name.
             * @param bm The Bitmap you want to save.
             * @param format Bitmap.CompressFormat can be PNG,JPEG or WEBP.
             * @param quality quality goes from 1 to 100. (Percentage).
             * @return true if the Bitmap was saved successfully, false otherwise.
             */

            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("message/rfc822");
            i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"vpalmgren@gmail.com"});
            i.putExtra(Intent.EXTRA_SUBJECT, "Congrats! You reached " + totalPoints + " in BirdBonger!");
            i.putExtra(Intent.EXTRA_TEXT   , "Here is your congratulation message from the CEO of BirdBonging ltd:" + br
                    + br +"Well done indeed! I thought no one would reach the level you have and look how fast " +
                    "you got there! I bet it was not easy. I await to which hights you will reach as time passes by... I will surely keep my eye on your progress :)" +
                    br +br+ "Sincerely, " + br + br+ "Willie Palmberg"+ br+ " CEO BirdBonging ltd");
            try {
                startActivity(i);//nyt menee suoraan sähköpostiin. Ekan kerran kysyy sovellusta mutta sit skulaa aina!
                //Intent.createChooser(i, "Send mail...") entinen intentti joka käynnistää valitsijan puhelimessa.
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(getActivity(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
            }
        }else if(totalPoints >= 10000){

            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("message/rfc822");
            i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"vpalmgren@gmail.com"});
            i.putExtra(Intent.EXTRA_SUBJECT, "Congrats! You reached " + totalPoints + " in BirdBonger!");
            i.putExtra(Intent.EXTRA_TEXT   , "Holy smokes! You did the unthinkable and conquered the game! CEO Willie Palmberg has an urgent message for you: " + br +
            br+"Oh my god! You did it! The treasure is now yours: Five weeks at the Sandals resort in Jamaica, all inclusive! " + br
                    +"Our company Jet will fly you there. Jet will pick you up tomorrow at around 25:15 so dont be late!" + br+
            "Be sure to pack your bags early and get plenty of sun screen! The sun can be oh so unforgiving in Jamaica..." + br + br+
            "Congratulations again!" + br + br + "Willie Palmberg " + br + "CEO BirdBonging ltd" + br + br + br + "P.S." + br+ "This message will deconstruct if not stored properly.");
            try {
                startActivity(i);
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(getActivity(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
