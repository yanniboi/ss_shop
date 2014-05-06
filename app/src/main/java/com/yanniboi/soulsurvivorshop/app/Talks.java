package com.yanniboi.soulsurvivorshop.app;

import android.content.Context;
import android.os.Environment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Talks {
    private static Context context;
    private String[] firstValues;
    private String[] secondValues;
    private Boolean[] downloadValues;
    private String[] talkIds;
    public List<Talk> talks;

    public static String fileName = "mytalks.json";





    public Talks() {

        this.firstValues = new String[] { "33001 - How to interpret the Bible", "03101 - Main Meeting Day 1 pm", "05101 - Main Meeting Day 1 Evening" };

        this.secondValues = new String[] {
                "How is a book written thousands of years ago still relevant? Chris will look at how we can interpret the Bible and apply it to our lives today.",
                "SSA Day 1, Evening Meeting\n" +
                        "\n" +
                        "Ali Martin- This is my Son whom I love",
                "Soul Survivor Week C: Main Meeting Day 1 Evening\n" +
                        " \n" +
                        "Mike Pilavachi: The call to Christ" };

        this.downloadValues = new Boolean[] { false, false, false };

        this.talkIds = new String[] { "33101", "03101", "05101" };

    }

    public List<Talk> getTalks(Context context)  {
        this.context = context;
        try {

            JSONObject jsonFile = null;
            String json = new BufferedReader(new InputStreamReader(context.openFileInput(fileName), "UTF-8")).readLine();

            try {
                jsonFile = new JSONObject(json);
            } catch (Exception ignored) {
                // @TODO error handling.
            }

            // Return early if necessary.
            if (jsonFile == null) {
                // @TODO error handling.
            }

            try {
                JSONArray jsonTalks = jsonFile.getJSONArray("talks");

                // Get number of sessions.
                int numberOfTalks = jsonTalks.length();
                talks = new ArrayList<Talk>();

                for (int i = 0; i < numberOfTalks; i++) {
                    JSONObject jsonTalk = jsonTalks.getJSONObject(i).getJSONObject("talk");

                    // Create new talk object.
                    Talk talk = new Talks.Talk(jsonTalk);
                    if (talk.type.equals("mp3")) {
                        talks.add(new Talks.Talk(jsonTalk));
                    }
                }
            }
            catch (Exception ignored) {
                Exception test = ignored;

            }
        }
        catch (IOException ignored) {
            Exception test = ignored;

        }

        return talks;
    }




    public class Talk {
        public String firstValue;
        public String secondValue;
        public Boolean downloadValue;
        public String talkId;
        public String name;
        public String type;
        public String uri;

        public Talk(JSONObject jsonTalk) {
            try {
                this.firstValue = jsonTalk.getString("title");
                this.secondValue = jsonTalk.getString("body");
                this.name = jsonTalk.getString("name");
                this.type = jsonTalk.getString("type");
                this.uri = jsonTalk.getString("uri");
                //this.talkId = jsonTalk.getString("name").replaceAll( "[^\\d]", "" );
                this.talkId = jsonTalk.getString("name").split("[.]")[0];
                this.downloadValue = checkDownload(Talks.context);
            }
            catch (JSONException ignored) {
                JSONException test = ignored;
            }

            //this.talkId = jsonTalk.getString("");
            //this.talkId = jsonTalk.getString("");
            //this.downloadValue = checkDownload(Talks.context);
        }

        public Talk(String firstValue, String secondValue, String talkId) {
            this.firstValue = firstValue;
            this.secondValue = secondValue;
            this.talkId = talkId;
            this.downloadValue = checkDownload(Talks.context);
        }

        public Boolean checkDownload(Context context) {
            File home = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);


            FilenameFilter textFilter = new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    if (name.startsWith(talkId)) {
                        return true;
                    } else {
                        return false;
                    }
                }
            };

            File[] files = new File[0];
            if (home != null) {
                files = home.listFiles(textFilter);
            }
            return files.length > 0;

        }
    }



}
