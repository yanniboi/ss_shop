package com.yanniboi.soulsurvivorshop.app;

/**
 * Created by yan on 28/04/14.
 */
public class Talks {
    private String[] firstValues;
    private String[] secondValues;
    private Boolean[] downloadValues;

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

        this.downloadValues = new Boolean[] { true, true, false };


    }

    public Talk[] getTalks() {
        int length = firstValues.length;
        Talk[] talks = new Talk[length];

        for (int i = 0; i < length; i++) {
            talks[i] = new Talk(firstValues[i], secondValues[i], downloadValues[i]);
        }

        return talks;
    }

    public class Talk {
        public String firstValue;
        public String secondValue;
        public Boolean downloadValue;

        public Talk(String firstValue, String secondValue, Boolean downloadValue) {
            this.firstValue = firstValue;
            this.secondValue = secondValue;
            this.downloadValue = downloadValue;
        }
    }



}
