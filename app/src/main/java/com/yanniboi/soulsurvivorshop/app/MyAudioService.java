package com.yanniboi.soulsurvivorshop.app;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import java.io.IOException;

public class MyAudioService extends Service implements MediaPlayer.OnErrorListener {
    MediaPlayer mMediaPlayer;

    public void initMediaPlayer() throws IOException {


        //mMediaPlayer.setOnErrorListener(this);

        String songName = "Martyn at Church";
        //setUpAsForeground(songName);
        configAndStartMediaPlayer();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        // ... react appropriately ...
        // The MediaPlayer has moved to the Error state, must be reset!
        return true;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    void setUpAsForeground(String text) {
        //Intent i = new Intent(MyAudioService.this, MainActivity.class);
        //PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0,
                //new Intent(getApplicationContext(), MainActivity.class),
                //PendingIntent.FLAG_UPDATE_CURRENT);
        Notification mNotification = new Notification();
        mNotification.tickerText = text;
        mNotification.icon = R.drawable.yes;
        mNotification.flags |= Notification.FLAG_ONGOING_EVENT;
        //mNotification.setLatestEventInfo(getApplicationContext(), "RandomMusicPlayer",
                //text, pi);
        startForeground(1, mNotification);
    }

    void configAndStartMediaPlayer() throws IOException {
        String url = "http://vinelife.co.uk/downloads/2014-03-30-martyn_smith.mp3"; // your URL here
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setDataSource(url);
        mMediaPlayer.prepare(); // might take long! (for buffering, etc)
        mMediaPlayer.start();
    }
}