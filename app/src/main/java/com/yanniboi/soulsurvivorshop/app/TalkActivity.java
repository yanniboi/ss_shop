package com.yanniboi.soulsurvivorshop.app;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;


public class TalkActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talk);

        Intent intent = getIntent();
        String firstLine = intent.getStringExtra("first");
        String secondLine = intent.getStringExtra("second");

        TextView tv1 = (TextView) findViewById(R.id.first);
        tv1.setText(firstLine);
        TextView tv2 = (TextView) findViewById(R.id.second);
        tv2.setText(secondLine);

        Button play = (Button) findViewById(R.id.play_talk);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    playTalk();
                } catch (IOException e) {
                    // @TODO Error handling
                }
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.talk, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void playTalk() throws IOException {
        //MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.splash);
        //mediaPlayer.start();

        new MyAudioService().initMediaPlayer();
    }
}
