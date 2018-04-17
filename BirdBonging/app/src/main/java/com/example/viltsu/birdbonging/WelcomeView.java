package com.example.viltsu.birdbonging;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


public class WelcomeView extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_view);

        releaseMediaPlayer();

        gameAudioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);

        int audiostate = gameAudioManager.requestAudioFocus(gameAudioFocusListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
        //jos saadaan toisto-oikeus:
        if(audiostate == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            //mediaplayer luodaan ja mp3 ladataan
            gameMediaPlayer = MediaPlayer.create(this, R.raw.silenze);
            //toisto mp3
            gameMediaPlayer.start();
            //kun soitto loppu, aja oma releaseMediaPlayer metodi
            gameMediaPlayer.setOnCompletionListener(completionListener);
        }

    }

    public void goBonging(View view){
        Intent intent = new Intent(this, BongingBirds.class);

        startActivity(intent);
    }

    public void seeStats(View view){
        Intent intent = new Intent(this, StatsCenter.class);
        startActivity(intent);
    }

    public void releaseMediaPlayer(){

        if(gameMediaPlayer != null){
            gameMediaPlayer.release();
            gameMediaPlayer = null;
            gameAudioManager.abandonAudioFocus(gameAudioFocusListener);
        }
    }

}
