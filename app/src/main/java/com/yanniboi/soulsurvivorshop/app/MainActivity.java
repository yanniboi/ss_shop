package com.yanniboi.soulsurvivorshop.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    // Download 'My Talks'.
    public static int siteStatus;
    public static String fileName = "mytalks.json";

    // User context checks.
    static Boolean login;
    static Boolean mytalks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        login = checkLogin();
        mytalks = checkMyTalks();

        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        updateMenuTitles(menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
            return true;
        }
        if (id == R.id.action_refresh) {
            new downloadTalks().execute();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Update action menu between login and logout.
     */
    private void updateMenuTitles(Menu menu) {
        MenuItem settings_login = menu.findItem(R.id.action_settings);
        if (login) {
            settings_login.setTitle(R.string.action_logout);
        } else {
            settings_login.setTitle(R.string.action_login);
        }
    }

    /**
     * Called when the user clicks the My Talks button
     */
    public void goToMyTalks(View view) {
        Intent i = new Intent(this, MyTalksActivity.class);
        startActivity(i);
    }

    /**
     * Called when the user clicks the Sync My Talks button
     */
    public void syncMyTalks(View view) {
        new downloadTalks().execute();
    }

    /**
     * Called when the user clicks the Sync My Talks button
     */
    public void browseShop(View view) {
        //Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://shop.soulsurvivor.com/seminars-talks"));
        //startActivity(browserIntent);
        Intent i = new Intent(this, BrowseActivity.class);
        startActivity(i);
    }

    /**
     * Checks whether user is loggen in.
     */
    public boolean checkLogin() {
        SharedPreferences prefsLogin = getSharedPreferences("PREFS_LOGIN", 0);
        return prefsLogin.getBoolean("auth", false);
    }

    /**
     * Checks whether user is loggen in.
     */
    public boolean checkMyTalks() {
        SharedPreferences prefsLogin = getSharedPreferences("PREFS_LOGIN", 0);
        return prefsLogin.getBoolean("auth", false);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View loginView = inflater.inflate(R.layout.fragment_main, container, false);
            View nologinView = inflater.inflate(R.layout.fragment_main_nologin, container, false);
            View notalksView = inflater.inflate(R.layout.fragment_main_notalks, container, false);

            if (MainActivity.login && MainActivity.mytalks) {
                return loginView;
            }
            else {
                if (MainActivity.login) {
                    return notalksView;
                }
                return nologinView;
            }
        }

    }

    /**
     * Download the program from the internet and save it locally.
     */
    public int getMyTalks() throws IOException {
        siteStatus = -1;

        try {
            //URL url = new URL("http://six-gs.com/ssshop/user/6/licensed-files/android");
            URL url = new URL("http://shop.soulsurvivor.com/android/6/licensed-files");
            HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
            siteStatus = httpConnection.getResponseCode();
            if (siteStatus == 200) {
                InputStream inputStream = httpConnection.getInputStream();

                byte[] buffer = new byte[1024];
                int bufferLength;

                // Write data to local file.

                FileOutputStream fos = openFileOutput(fileName, MODE_PRIVATE);
                while ((bufferLength = inputStream.read(buffer)) > 0) {
                    fos.write(buffer, 0, bufferLength);
                }
                fos.flush();
                fos.close();
            }

            httpConnection.disconnect();
        }
        catch (IOException ignored) {
            IOException test = ignored;
            // @TODO error handling.
        }

        return siteStatus;
    }

    /**
     * Download task.
     */
    class downloadTalks extends AsyncTask<Context, Integer, String> {

        protected String doInBackground(Context... params) {
            try {
                siteStatus = getMyTalks();
            }
            catch (IOException ignored) {
                // @TODO error handling.
                return "fail";

            }

            if (siteStatus == 200) {
                return "success";
            }


            return "fail";
        }


        @Override
        protected void onPostExecute(String sResponse) {
            if (sResponse.equals("fail")) {
                Toast.makeText(getApplicationContext(), "Talks not found", Toast.LENGTH_SHORT).show();
                mytalks = true;
            }
            //else if (sResponse.equals("parsingfailed")) {
            //}
            else {
                Toast.makeText(getApplicationContext(), "Talks found", Toast.LENGTH_SHORT).show();
                mytalks = false;
            }
        }
    }
}
