package com.yanniboi.soulsurvivorshop.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    private Talks.Talk[] talks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }*/

        setContentView(R.layout.list);

        ListView listview = (ListView) findViewById(R.id.list);
        TextView nologin = (TextView) findViewById(R.id.fragment_no_login);

        talks = new Talks().getTalks();

        final SimpleArrayAdapter adapter = new SimpleArrayAdapter(this, talks);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                Intent i = new Intent(getApplication(), TalkActivity.class);

                i.putExtra("first", talks[position].firstValue);
                i.putExtra("second", talks[position].secondValue);
                startActivity(i);
            }

        });

        boolean login = checkLogin();

        if (login) {
            nologin.setVisibility(TextView.GONE);
        } else {
            listview.setVisibility(ListView.GONE);
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
        return super.onOptionsItemSelected(item);
    }

    private void updateMenuTitles(Menu menu) {
        MenuItem settings_login = menu.findItem(R.id.action_settings);
        if (checkLogin()) {
            settings_login.setTitle(R.string.action_logout);
        } else {
            settings_login.setTitle(R.string.action_login);
        }
    }

    /**
     * Checks whether user is loggen in.
     */
    public boolean checkLogin() {
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
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }
}
