package com.example.indra.musicplayerproject;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity {

    Button btn_next,btn_previous,btn_pause;
    TextView songTextLabel;
    SeekBar songSeekbar;
    String sname;

    static MediaPlayer myMediaPlayer;
    int pstn;

    ArrayList<File> mysongs;
    Thread updateseekbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        btn_next=(Button)findViewById(R.id.next);
        btn_previous=(Button)findViewById(R.id.previous);
        btn_pause=(Button)findViewById(R.id.pause);
        songTextLabel=(TextView)findViewById(R.id.songLabel);
        songSeekbar=(SeekBar)findViewById(R.id.seekBar);

        /*getSupportActionBar().setTitle("Now Playing");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        */


        updateseekbar =new Thread(){

            @Override
            public void run() {
                int totalDuration=myMediaPlayer.getDuration();
                int currentPosition=0;
                while(currentPosition<totalDuration)
                {
                    try {

                        sleep(500);
                        currentPosition=myMediaPlayer.getCurrentPosition();
                        songSeekbar.setProgress(currentPosition);

                    }

                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }

                }
            }
        };

        if(myMediaPlayer!=null)
        {
            myMediaPlayer.stop();
            myMediaPlayer.release();
        }
        Intent i=getIntent();
        Bundle bundle=i.getExtras();

        mysongs=(ArrayList) bundle.getParcelableArrayList("songs");

        sname=mysongs.get(pstn).getName().toString();
        String songName=i.getStringExtra("songname");
        songTextLabel.setText(songName);
        songTextLabel.setSelected(true);

        pstn= bundle.getInt("pos",0);
        Uri u=Uri.parse(mysongs.get(pstn).toString());

        myMediaPlayer = MediaPlayer.create(getApplicationContext(),u);
        myMediaPlayer.start();
        songSeekbar.setMax(myMediaPlayer.getDuration());
        updateseekbar.start();

      /*  songSeekbar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary)
        ,PorterDuff.Mode.MULTIPLY);
        songSeekbar.getThumb().setColorFilter(getResources().getColor(R.color.colorPrimary)
        ,PorterDuff.Mode.SRC_IN);
        */

        songSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                myMediaPlayer.seekTo(seekBar.getProgress());
            }
        });


        btn_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                songSeekbar.setMax(myMediaPlayer.getDuration());

                if(myMediaPlayer.isPlaying())
                {
                    btn_pause.setBackgroundResource(R.drawable.icon_play);
                    myMediaPlayer.pause();
                }
                else
                {
                    btn_pause.setBackgroundResource(R.drawable.icon_pause);
                    myMediaPlayer.start();
                }
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myMediaPlayer.stop();
                myMediaPlayer.release();
                pstn=((pstn+1)%mysongs.size());

                Uri u=Uri.parse(mysongs.get(pstn).toString());
                myMediaPlayer =MediaPlayer.create(getApplicationContext(),u);

                sname=mysongs.get(pstn).getName().toString();
                songTextLabel.setText(sname);
                myMediaPlayer.start();
            }
        });

        btn_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myMediaPlayer.stop();
                myMediaPlayer.release();
                pstn=((pstn-1)<0)?(mysongs.size()-1):(pstn-1);
                Uri u=Uri.parse(mysongs.get(pstn).toString());
                myMediaPlayer=MediaPlayer.create(getApplicationContext(),u);

                sname=mysongs.get(pstn).getName().toString();
                songTextLabel.setText(sname);
                myMediaPlayer.start();
            }
        });


    }

   /* @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home)
        {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }*/
}
