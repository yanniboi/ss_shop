package com.yanniboi.soulsurvivorshop.app;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.PowerManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;


public class TalkActivity extends ActionBarActivity {

    ProgressDialog mProgressDialog;
    String talkFileName;
    String talkId;
    String talkUri;
    static Boolean downloaded;
    String user_id;
    String firstLine;
    public static Notification talkNotification;
    static NotificationManager notificationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talk);

        Intent intent = getIntent();
        firstLine = intent.getStringExtra("first");
        String secondLine = intent.getStringExtra("second");
        downloaded = intent.getBooleanExtra("downloaded", false);
        talkId = intent.getStringExtra("id");
        talkUri = intent.getStringExtra("uri");

        // Get logged in user id.
        SharedPreferences prefsLogin = getSharedPreferences("PREFS_LOGIN", 0);
        user_id = prefsLogin.getString("user_id", "0");

        TextView tv1 = (TextView) findViewById(R.id.first);
        tv1.setText(firstLine);
        TextView tv2 = (TextView) findViewById(R.id.second);
        tv2.setText(secondLine);

        Button play = (Button) findViewById(R.id.play_talk);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplication(), TalkPlayerActivity.class);
                i.putExtra("id", talkId);
                i.putExtra("refresh", true);
                startActivity(i);
                sendNotification();
            }
        });

        Button download = (Button) findViewById(R.id.download_talk);
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startDownload();
            }
        });

        if (downloaded) {
            download.setVisibility(Button.GONE);
        }
        else {
            play.setVisibility(Button.GONE);
        }


    }

    /**
     * Initiates progress dialog and starts download task.
     */
    private void startDownload() {
        mProgressDialog = new ProgressDialog(TalkActivity.this);
        mProgressDialog.setMessage("Downloading talk: " + firstLine );
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(true);

        URL url = null;
        try {
            String path = talkUri.split("private://")[1];

            //url = new URL("http://vinelife.co.uk/downloads/2014-03-30-martyn_smith.mp3");
            String urlString = "http://shop.soulsurvivor.com/?q=system/files/" + path;
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            // @TODO Error handling
        }

        final DownloadTalkTask downloadTask = new DownloadTalkTask(getApplicationContext());
        downloadTask.execute(url);

        mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                downloadTask.cancel(true);
            }
        });
    }

    private void deleteTalk() {
        File home = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);

        // Check file names and delte.
        File[] files = home.listFiles();
        for (File file : files) {
            if (file.getName().contains(talkId)) {
                file.delete();
                break;
            }
        }
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
            startDownload();
            return true;
        }
        if (id == R.id.action_delete) {
            deleteTalk();
            Intent intent = getIntent();
            intent.putExtra("downloaded", false);
            finish();
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void playTalk() throws IOException {
        //MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.splash);
        //mediaPlayer.start();

        new MyAudioService().initMediaPlayer();
    }

    private class DownloadTalkTask extends AsyncTask<URL, Integer, Boolean> {
        private Context context;
        private PowerManager.WakeLock mWakeLock;

        public DownloadTalkTask(Context context) {
            this.context = context;
        }

        protected Boolean doInBackground(URL... url) {
            try {
                //set the download URL, a url that points to a file on the internet
                //this is the file to be downloaded

                //create the new connection
                HttpURLConnection urlConnection = (HttpURLConnection) url[0].openConnection();

                urlConnection.setRequestMethod("POST");
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                urlConnection.connect();
                urlConnection.getOutputStream().write(("{\"uid\":\"" + user_id + "\"}").getBytes());

                File SDCardRoot = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);

                // @TODO Create/Check for Talks directory

                talkFileName = url[0].getFile().substring(url[0].getFile().lastIndexOf('/') + 1);
                File talkFile = new File(SDCardRoot, talkFileName);

                //this will be used to write the downloaded data into the file we created
                FileOutputStream fileOutput = new FileOutputStream(talkFile);

                //this will be used in reading the data from the internet
                InputStream inputStream = urlConnection.getInputStream();

                //this is the total size of the file
                int totalSize = urlConnection.getContentLength();
                //variable to store total downloaded bytes
                int downloadedSize = 0;

                //create a buffer...
                byte[] buffer = new byte[1024];
                int bufferLength = 0; //used to store a temporary size of the buffer

                //now, read through the input buffer and write the contents to the file
                while ( (bufferLength = inputStream.read(buffer)) > 0 ) {
                    //add the data in the buffer to the file in the file output stream (the file on the sd card
                    fileOutput.write(buffer, 0, bufferLength);
                    //add up the size so we know how much is downloaded
                    downloadedSize += bufferLength;
                    //this is where you would do something to report the prgress, like this maybe
                    //final int progress = (int) ((((double) downloadedSize) / totalSize)  * 100);
                    publishProgress((int) ((((double) downloadedSize) / totalSize)  * 100));
                    //updateProgress(downloadedSize, totalSize);

                }
                fileOutput.close();
                inputStream.close();

                if (urlConnection != null) {
                    urlConnection.disconnect();
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return false;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }



            return true;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // take CPU lock to prevent CPU from going off if the user
            // presses the power button during download
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    getClass().getName());
            mWakeLock.acquire();
            mProgressDialog.show();
        }

        protected void onProgressUpdate(Integer... progress) {
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setMax(100);
            mProgressDialog.setProgress(progress[0]);        }

        protected void onPostExecute(Boolean success) {
            mWakeLock.release();
            mProgressDialog.dismiss();
            if (success) {
                /*SharedPreferences prefsFiles = getSharedPreferences("PREFS_FILES", 0);
                SharedPreferences.Editor sessionEdit = prefsFiles.edit();
                sessionEdit.putBoolean(talkFileName, true);
                sessionEdit.commit();*/

                Toast.makeText(context,"File downloaded", Toast.LENGTH_SHORT).show();
                Intent intent = getIntent();
                intent.putExtra("downloaded", true);
                finish();
                startActivity(intent);
            } else {
                Toast.makeText(context, "Download error...", Toast.LENGTH_LONG).show();
            }
        }
    }


    public void sendNotification() {
        String msgText = "To go back to the audio player, click below.";

        if (talkNotification != null) {
            Intent intent = new Intent(this, StopPlayerService.class);
            intent.setAction("com.yanniboi.soulsurvivorshop.app.action.STOP");
            intent.putExtra("com.yanniboi.soulsurvivorshop.app.extra.TALK", talkId);
            startService(intent);
            notificationManager.cancelAll();
        }
        notificationManager = getNotificationManager();
        PendingIntent piAction = getPendingIntentAction();
        PendingIntent piDelete = getPendingIntentDelete();
        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle("Talk Player Title")
                .setContentText("Talk Player")
                .setSmallIcon(R.drawable.ic_launcher)
                .addAction(R.drawable.btn_play, "Back to player", piAction)
                .setAutoCancel(true)
                .setDeleteIntent(piDelete);


        talkNotification = new Notification.BigTextStyle(builder)
                .bigText(msgText)
                .build();
        notificationManager.notify(0, talkNotification);
    }

    public PendingIntent getPendingIntentAction() {
        Intent intent = new Intent(this, TalkPlayerActivity.class);
        intent.putExtra("id", talkId);

        return PendingIntent.getActivity(this, 0, intent, 0);
    }

    public PendingIntent getPendingIntentDelete() {
        Intent intent = new Intent(this, StopPlayerService.class);
        intent.setAction("com.yanniboi.soulsurvivorshop.app.action.STOP");
        intent.putExtra("com.yanniboi.soulsurvivorshop.app.extra.TALK", talkId);

        return PendingIntent.getService(this, 0, intent, 0);
    }

    public NotificationManager getNotificationManager() {
        return (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }
}
