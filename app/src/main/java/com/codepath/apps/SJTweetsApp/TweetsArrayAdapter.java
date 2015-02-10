package com.codepath.apps.SJTweetsApp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.SJTweetsApp.models.Tweet;
import com.codepath.apps.SJTweetsApp.utilities.ParseRelativeDate;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by sjayanna on 2/8/15.
 */
public class TweetsArrayAdapter extends ArrayAdapter<Tweet> {
    /**
     * Constructor
     *
     * @param context  The current context.
     * @param objects  The objects to represent in the ListView.
     */
    public TweetsArrayAdapter(Context context, List<Tweet> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }

    //TODO: Implement ViewHolder pattern here
    /**
     * {@inheritDoc}
     *
     * @param position
     * @param convertView
     * @param parent
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        /*
            1. Get the tweet
            2. Find or inflate the template
            3. Find the subview to fill with data in the template
            4. Populate data into the subview
            5. Return the view to be inserted into the ListView
         */
        Tweet tweet = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet, parent, false);
        }

        // get all the views required for constructing a tweet view
        ImageView ivProfile = (ImageView) convertView.findViewById(R.id.ivProfile);
        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        TextView tvScreenName = (TextView) convertView.findViewById(R.id.tvScreenName);
        TextView tvRelativeCreatedAt = (TextView) convertView.findViewById(R.id.tvRelativeCreatedAt);
        TextView tvBody = (TextView) convertView.findViewById(R.id.tvBody);

        // load data into tweet view
        ivProfile.setImageResource(android.R.color.transparent);
        Picasso.with(getContext()).load(tweet.getUser().getProfileImageUrl()).into(ivProfile);

        tvName.setText(tweet.getUser().getName());
        tvScreenName.setText("@" + tweet.getUser().getScreenName());
        tvRelativeCreatedAt.setText(ParseRelativeDate.getRelativeTimeAgo(tweet.getCreatedAt()));
        tvBody.setText(tweet.getBody());

        return convertView;
    }
}
