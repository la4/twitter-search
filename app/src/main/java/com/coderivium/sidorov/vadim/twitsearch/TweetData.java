package com.coderivium.sidorov.vadim.twitsearch;

import android.graphics.Bitmap;

public class TweetData {

    Bitmap authorAvatar;

    String authorFullName;

    String nickname;

    String tweetContent;

    TweetData(Bitmap authorAvatar, String nickname, String authorName, String tweetContent) {
        this.nickname = nickname;
        this.authorAvatar = authorAvatar;
        this.authorFullName = authorName;
        this.tweetContent = tweetContent;
    }

}
