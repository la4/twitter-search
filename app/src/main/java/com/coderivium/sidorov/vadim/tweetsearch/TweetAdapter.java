package com.coderivium.sidorov.vadim.tweetsearch;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import twitter4j.Status;

public class TweetAdapter extends ArrayAdapter<Status> {

    private static final String LOG_TAG = TweetAdapter.class.getSimpleName();

    LayoutInflater inflater;

    public TweetAdapter(Context context, int textViewResourceId, ArrayList<Status> items) {
        super(context, textViewResourceId, items);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = inflater.inflate(R.layout.element_list, parent, false);
        }

        Status tweet = getItem(position);

        ((TextView) view.findViewById(R.id.nicknameTextView)).setText("@" + tweet.getUser().getScreenName());
        ((TextView) view.findViewById(R.id.fullNameTextView)).setText(tweet.getUser().getName());
        ((TextView) view.findViewById(R.id.contentTextView)).setText(tweet.getText());



        return view;
    }
}
