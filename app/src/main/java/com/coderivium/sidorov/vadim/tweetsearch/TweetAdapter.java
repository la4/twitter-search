package com.coderivium.sidorov.vadim.tweetsearch;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class TweetAdapter extends ArrayAdapter<TweetData> {

    LayoutInflater inflater;

    public TweetAdapter(Context context, int textViewResourceId, ArrayList<TweetData> items) {
        super(context, textViewResourceId, items);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    private TweetHolder tweetHolder;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = inflater.inflate(R.layout.element_list, parent, false);
        }

        TweetData tweet = getItem(position);

        ((TextView) view.findViewById(R.id.nicknameTextView)).setText(tweet.nickname);
        ((TextView) view.findViewById(R.id.fullNameTextView)).setText(tweet.authorFullName);
        ((TextView) view.findViewById(R.id.contentTextView)).setText(tweet.tweetContent);
        ((ImageView) view.findViewById(R.id.authorAvatarImagewView)).setImageBitmap(tweet.authorAvatar);

        return view;
    }

    private static class TweetHolder {
        private Bitmap authorAvatar;
        private TextView nickname;
        private TextView authorFullName;
        private TextView tweetContent;
    }

}
