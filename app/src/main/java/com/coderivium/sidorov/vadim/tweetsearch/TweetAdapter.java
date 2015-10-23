package com.coderivium.sidorov.vadim.tweetsearch;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import twitter4j.Status;

public class TweetAdapter extends ArrayAdapter<Status> {

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

        ((TextView) view.findViewById(R.id.nicknameTextView)).setText(tweet.getUser().getName());
        ((TextView) view.findViewById(R.id.fullNameTextView)).setText(tweet.getUser().getScreenName());
        ((TextView) view.findViewById(R.id.contentTextView)).setText(tweet.getText());
        //((ImageView) view.findViewById(R.id.authorAvatarImageView)).setImageBitmap(tweet.authorAvatar);

        return view;
    }
}
