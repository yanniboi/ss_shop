package com.yanniboi.soulsurvivorshop.app;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FilenameFilter;

public class Talks {
    private static Context context;
    private String[] firstValues;
    private String[] secondValues;
    private Boolean[] downloadValues;
    private String[] talkIds;

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

    public Talk[] getTalks(Context context) {
        this.context = context;
        int length = firstValues.length;
        Talk[] talks = new Talk[length];

        for (int i = 0; i < length; i++) {
            talks[i] = new Talk(firstValues[i], secondValues[i], talkIds[i]);
        }

        return talks;
    }

    public class Talk {
        public String firstValue;
        public String secondValue;
        public Boolean downloadValue;
        public String talkId;

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
