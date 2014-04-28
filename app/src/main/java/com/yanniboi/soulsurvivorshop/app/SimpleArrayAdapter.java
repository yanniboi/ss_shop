package com.yanniboi.soulsurvivorshop.app;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class SimpleArrayAdapter extends ArrayAdapter<Talks.Talk> {
    private final Context context;
    private final Talks.Talk[] talks;


    public SimpleArrayAdapter(Context context, Talks.Talk[] talks) {
        super(context, R.layout.list_item, talks);
        this.context = context;
        this.talks = talks;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_item, parent, false);
        TextView firstTextView = (TextView) rowView.findViewById(R.id.firstLine);
        TextView secondTextView = (TextView) rowView.findViewById(R.id.secondLine);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        firstTextView.setText(talks[position].firstValue);
        secondTextView.setText(talks[position].secondValue);
        // change the icon for Windows and iPhone
        String s = talks[position].firstValue;
        if (s.startsWith("iPhone")) {
            imageView.setImageResource(R.drawable.no);
        } else {
            imageView.setImageResource(R.drawable.ok);
        }

        return rowView;
    }
} 