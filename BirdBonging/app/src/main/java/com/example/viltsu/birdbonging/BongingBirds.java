package com.example.viltsu.birdbonging;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.ContentValues;

import com.example.viltsu.birdbonging.data.BirdContract;
import com.example.viltsu.birdbonging.data.BirdContract.BirdEntry;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.viltsu.birdbonging.data.BirdHelper;

/**
 * Created by ville-pekkapalmgren on 18/01/18.
 */

/*
 *Toimii SQL puoli kokonaan. Lisäys, luku ja poisto ominaisuudet. Lintu häviää painalluksen jälkeen myös. Animointi idea pois
 * ennen kuin löytyy tehokkaampi tapa tehdä se.
 */

public class BongingBirds extends WelcomeView{

    private BirdHelper birdie;
    private SQLiteDatabase db;
    int totalpoints = 0;

    ViewGroup masterLay;

    //Linnun lisäys kantaan:
    private void insertBird(String breed, int nro, String points){
        birdie = new BirdHelper(this);

        SQLiteDatabase db = birdie.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(BirdEntry.COLUMN_BIRD_BREED, breed);
        values.put(BirdEntry.COLUMN_BIRD_POINTS, points);
        values.put(BirdEntry.COLUMN_BIRD_PIC, nro);
        long newRowId = db.insert(BirdEntry.TABLE_NAME, null, values);
    }
    //  Animointi osuus linnuille

    private void animateDuck(Button button){
        final Animation bird = new TranslateAnimation(0, -200, 0 , -1000);
        bird.setDuration(3000);
        bird.setRepeatCount(3);
        bird.setRepeatMode(2);
        bird.setInterpolator(new Interpolator() {
            @Override
            public float getInterpolation(float v) {
                return v;
            }
        });
        button.startAnimation(bird);

    }

    private void animatePecker(Button button){
        //final Animation bird = new TranslateAnimation(0,-300,0,900);
        ObjectAnimator bird = ObjectAnimator.ofFloat(button, "rotationY", -90f, 0f);
        bird.setDuration(3000);
        bird.setInterpolator(new Interpolator(){
            @Override
            public float getInterpolation(float v) {
                float x = 2.0f * v - 1.0f;
                return 0.5f * (x * x * x + 1.0f);
            }
        });
    }
    private void animatePenguin(Button button){
        //final Animation bird = new TranslateAnimation(0,200,0,600);
        ScaleAnimation bird = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f);
        bird.setRepeatCount(3);
        bird.setRepeatMode(2);
        bird.setInterpolator(new Interpolator() {
            @Override
            public float getInterpolation(float v) {
                float y = 1.5f * v + 1.0f;
                return y;
            }
        });
        bird.setDuration(3000);
        button.startAnimation(bird);
    }

    //musiikkiosuus pelille, privaatit metodit:
    private MediaPlayer gameMediaPlayer;
    private MediaPlayer.OnCompletionListener completionListener= new MediaPlayer.OnCompletionListener(){
        @Override
        public void onCompletion(MediaPlayer mp){
            releaseMediaPlayer();
        }
    };
    private AudioManager gameAudioManager;
    private AudioManager.OnAudioFocusChangeListener gameAudioFocusListener = new AudioManager.OnAudioFocusChangeListener(){
        @Override
        public void onAudioFocusChange(int focusChange) {
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT || focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK){
                //audio focus lähti hetkeksi, tulee takas. Mitä teet? Pause ja alkuun, soitto jatkuu focuksen tultua takasin.
                gameMediaPlayer.pause();
                gameMediaPlayer.seekTo(0);
            }else if(focusChange == AudioManager.AUDIOFOCUS_GAIN){
                //focus tulee takas niin soitto alkaa.
                gameMediaPlayer.start();
            }else if(focusChange == AudioManager.AUDIOFOCUS_LOSS){
                //vapauttaa resurssit taas.
                releaseMediaPlayer();
            }
        }
    };

    //lintujen äänet pelissä

    private MediaPlayer gameSoundPlayer;
    private MediaPlayer.OnCompletionListener soundComplete = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            releaseGameSound();
        }
    };
    private AudioManager gameSoundAudioManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bird_bonging);
        //up näppäin kotinavigointiin:
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //pisteet:
        readPoints();
        //ääniosuus:
        gameMusicStart();

        //animointiosuus:

        masterLay = (ViewGroup) findViewById(R.id.masterLayout);

        final Button button1 = (Button) findViewById(R.id.pecker);
        final Button button2 = (Button) findViewById(R.id.prikki);
        final Button button3 = (Button) findViewById(R.id.kukko);

        animateDuck(button1);
        animatePecker(button2);
        animatePenguin(button3);

        button1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0){

                gameSoundDuck();

                AlertDialog.Builder alertDialogBuilder;
                alertDialogBuilder = new AlertDialog.Builder(BongingBirds.this);
                alertDialogBuilder.setTitle("Im a Bird!");
                alertDialogBuilder
                        .setMessage("You got a Duck!")
                        .setCancelable(false)
                        .setNegativeButton("OK!", new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id){
                                int nro = R.drawable.duck;
                                insertBird("Duck", nro, "100pts");
                                dialog.cancel();
                                button1.setVisibility(View.INVISIBLE);
                            }
                        });

                AlertDialog alert = alertDialogBuilder.create();

                alert.show();

            }

        });

        button2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0){

                gameSoundMallord();

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(BongingBirds.this);
                alertDialogBuilder.setTitle("Im a Bird!");
                alertDialogBuilder
                        .setMessage("You got a WoodPecker!")
                        .setCancelable(false)
                        .setNegativeButton("OK!", new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id){
                                int nro = R.drawable.mallord;
                                insertBird("Wood Pecker", nro, "200pts");
                                dialog.cancel();
                                button2.setVisibility(View.INVISIBLE);
                            }
                        });

                AlertDialog alert = alertDialogBuilder.create();

                alert.show();
            }

        });

        button3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0){

                gameSoundPenguin();

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(BongingBirds.this);
                alertDialogBuilder.setTitle("Im a Bird!");
                alertDialogBuilder
                        .setMessage("You got a goddamn Penguino!! The rarest of them all!!!")
                        .setCancelable(false)
                        .setNegativeButton("OK!", new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id){
                                int nro = R.drawable.penguin;
                                insertBird("Penguino", nro, "600pts");
                                dialog.cancel();
                                button3.setVisibility(View.INVISIBLE);
                            }
                        });

                AlertDialog alert = alertDialogBuilder.create();

                alert.show();
            }

        });

    }

    /*
    //liikuta keltasta lintua
    public void moveDuck(){
        View duckbtn = findViewById(R.id.pecker);

        TransitionManager.beginDelayedTransition(masterLay);

        RelativeLayout.LayoutParams rulez = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        rulez.addRule(RelativeLayout.LEFT_OF, R.id.prikki);

        duckbtn.setLayoutParams(rulez);
    }
    //liikuta punasta lintua
    public void movePecker(){
        View peckerbtn = findViewById(R.id.prikki);

        TransitionManager.beginDelayedTransition(masterLay);

        RelativeLayout.LayoutParams rulez = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        rulez.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        rulez.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);

        peckerbtn.setLayoutParams(rulez);
    }

    //liikuta pingviiniä

    public void movePenguin(){
        View penguinbtn = findViewById(R.id.kukko);

        TransitionManager.beginDelayedTransition(masterLay);

        RelativeLayout.LayoutParams rulez = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        rulez.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,RelativeLayout.TRUE);
        rulez.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);

        penguinbtn.setLayoutParams(rulez);
    }

    public void startBonging(View view){
        Button start = (Button) findViewById(R.id.start);

        moveDuck();
        movePecker();
        movePenguin();

        start.setVisibility(View.INVISIBLE);
    }
    */

    public void releaseMediaPlayer(){
        if(gameMediaPlayer != null) {
            gameMediaPlayer.release();
            gameMediaPlayer = null;
            gameAudioManager.abandonAudioFocus(gameAudioFocusListener);
        }
        if(gameSoundPlayer != null){
            gameSoundPlayer.release();
            gameSoundPlayer = null;
            gameAudioManager.abandonAudioFocus(gameAudioFocusListener);
        }

    }

    public void releaseGameSound(){
        if(gameSoundPlayer != null){
            gameSoundPlayer.release();
            gameSoundPlayer = null;

        }
    }

    public void goHome(View view){
        releaseMediaPlayer();
        Intent intent = new Intent(this, WelcomeView.class);
        finish();
        startActivity(intent);
    }

    public void gameMusicStart(){
        releaseMediaPlayer();

        gameAudioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);

        int audiostate = gameAudioManager.requestAudioFocus(gameAudioFocusListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
        //jos saadaan toisto-oikeus:
        if(audiostate == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            //mediaplayer luodaan ja mp3 ladataan
            gameMediaPlayer = MediaPlayer.create(this, R.raw.birdgamemix);
            //toisto mp3
            gameMediaPlayer.start();
            //kun soitto loppu, aja oma releaseMediaPlayer metodi
            gameMediaPlayer.setOnCompletionListener(completionListener);
        }
    }

    public void gameSoundDuck(){
        releaseGameSound();
        gameSoundPlayer = MediaPlayer.create(BongingBirds.this, R.raw.shortduck);
        gameSoundPlayer.start();
        gameSoundPlayer.setOnCompletionListener(soundComplete);
    }

    public void gameSoundMallord(){
        releaseGameSound();
        gameSoundPlayer = MediaPlayer.create(BongingBirds.this, R.raw.shortmallord);
        gameSoundPlayer.start();
        gameSoundPlayer.setOnCompletionListener(soundComplete);
    }

    public void gameSoundPenguin(){
        releaseGameSound();
        gameSoundPlayer = MediaPlayer.create(BongingBirds.this, R.raw.shortpenguin);
        gameSoundPlayer.start();
        gameSoundPlayer.setOnCompletionListener(soundComplete);
    }

    public void readPoints() {

        birdie = new BirdHelper(BongingBirds.this);
        db = birdie.getReadableDatabase();

        String [] projection = {
                BirdContract.BirdEntry.COLUMN_BIRD_POINTS
        };

        Cursor c = db.query(BirdContract.BirdEntry.TABLE_NAME, projection, null, null, null, null, null);

        try{
            int birdBonged = c.getColumnIndex(BirdContract.BirdEntry.COLUMN_BIRD_POINTS);

                while(c.moveToNext()) {
                    totalpoints += c.getInt(birdBonged);
                }
            }finally{
            c.close();
        }
        Toast.makeText(BongingBirds.this, "Points gathered : " + totalpoints + " pts", Toast.LENGTH_SHORT).show();

        //TextView pointsview = findViewById(R.id.pointsview);

        //pointsview.setText("Points Gathered : " + totalpoints);

    }
}
