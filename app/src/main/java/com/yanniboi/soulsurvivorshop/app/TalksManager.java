package com.yanniboi.soulsurvivorshop.app;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;

public class TalksManager {
    // SDCard Path
    //final String MEDIA_PATH = new String("/sdcard/");
    private ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();
    private Context context;
    private static String talkId;

    // Constructor
    public TalksManager(Context context){
      this.context = context;
    }

    /**
     * Function to read all mp3 files from sdcard
     * and store the details in ArrayList
     * */
    public ArrayList<HashMap<String, String>> getPlayList(String talkId) {

        this.talkId = talkId;
        File home = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);

        if (home.listFiles(new FileExtensionFilter()).length > 0) {
            for (File file : home.listFiles(new FileExtensionFilter())) {
                HashMap<String, String> song = new HashMap<String, String>();
                song.put("songTitle", file.getName().substring(0, (file.getName().length() - 4)));
                song.put("songPath", file.getPath());

                // Adding each song to SongList
                songsList.add(song);
            }
        }
        // return songs list array
        return songsList;
    }

    /**
     * Class to filter files which are having .mp3 extension
     * */
    class FileExtensionFilter implements FilenameFilter {
        public boolean accept(File dir, String name) {
            String id = TalksManager.talkId;
                return ((name.endsWith(".mp3") || name.endsWith(".MP3")) && (name.startsWith(id)));
        }
    }
}

