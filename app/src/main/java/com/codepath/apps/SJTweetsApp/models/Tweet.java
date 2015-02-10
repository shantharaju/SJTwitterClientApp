package com.codepath.apps.SJTweetsApp.models;

/*
 *  [
 *      {
 *          text : "just another test",
 *          in_reply_to_screen_name : null,
 *          in_reply_to_status_id_str : null,
 *          "id": 240558470661799936,
 *          "in_reply_to_user_id": null,
 *          "source": "<a href="//realitytechnicians.com\"" rel="\"nofollow\"">OAuth Dancer Reborn</a>",
 *          "favorited": false,
 *          "retweet_count": 0,
 *          "created_at": "Tue Aug 28 21:16:23 +0000 2012",
 *          "favourites_count": 7,
 *          "user": {
 *              "name": "OAuth Dancer",
 *              "id_str": "119476949",
 *              "favourites_count": 7,
 *              "description": "",
 *              "created_at": "Wed Mar 03 19:37:35 +0000 2010",
 *              "friends_count": 701,
 *              "followers_count": 18752,
 *              "verified": false,
 *              "screen_name": "raffi"
 *              "profile_background_image_url": "http://a0.twimg.com/images/themes/theme1/bg.png",
 *              "profile_image_url": "http://a0.twimg.com/profile_images/1270234259/raffi-headshot-casual_normal.png",
 *      }
 *  ]
 */


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by sjayanna on 2/8/15.
 *
 * 1. Parse the JSON + store the data
 * 2. Encapsulate state logic or display logic
 *
 */
@Table(name = "Tweets")
public class Tweet extends Model implements Serializable {


    @Column(name = "body")
    private String body;

    @Column(name = "uid", unique = true)
    private long uid; // unique id for the tweet

    // TODO: create foreign key constraint
    @Column(name = "user")
    private User user;

    @Column(name = "created_at")
    private String createdAt;

    public String getBody() {
        return body;
    }

    public long getUid() {
        return uid;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public User getUser() {
        return user;
    }


    // Make sure to have a default constructor for every ActiveAndroid model
    public Tweet() {
        super();
    }

    // Deserialize JSON
    public static Tweet fromJSON(JSONObject jsonObject) {
        Tweet tweet = new Tweet();
        try {
            tweet.body = jsonObject.getString("text");
            tweet.uid = jsonObject.getLong("id");
            tweet.createdAt = jsonObject.getString("created_at");
            tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        tweet.save();
        return tweet;
    }

    public static ArrayList<Tweet> fromJSONArray(JSONArray jsonArray) {
        ArrayList<Tweet> tweets = new ArrayList<>();
        //Iterate JSONArray
        for(int i = 0; i < jsonArray.length(); ++i) {
            try {
                JSONObject tweetJSON = jsonArray.getJSONObject(i);
                Tweet tweet = Tweet.fromJSON(tweetJSON);
                if(tweet != null) {
                    tweets.add(tweet);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
        }
        return tweets;
    }
}
