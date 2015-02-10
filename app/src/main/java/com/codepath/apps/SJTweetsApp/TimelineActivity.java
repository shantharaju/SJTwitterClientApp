package com.codepath.apps.SJTweetsApp;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.activeandroid.query.Delete;
import com.codepath.apps.SJTweetsApp.interfaces.EndlessScrollListener;
import com.codepath.apps.SJTweetsApp.models.Tweet;
import com.codepath.apps.SJTweetsApp.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class TimelineActivity extends ActionBarActivity {

    private static final int UPDATE_STATUS = 1;
    private TwitterClient client;
    private ArrayList<Tweet> tweets;
    private TweetsArrayAdapter aTweets;
    private ListView lvTweets;
    private User loggedInUser;

    private SwipeRefreshLayout swipeContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        client = TwitterApp.getRestClient();

        // get views
        getViews();

        // create datasource
        tweets = new ArrayList<Tweet>();

        // create Arrayadapter
        aTweets = new TweetsArrayAdapter(this, tweets);

        // hook up adapter to ListView
        lvTweets.setAdapter(aTweets);

        // Get info about logged in user
        verifyCredentials();

        populateTimeline();
    }

    private void verifyCredentials() {

        client.verifyCredentials( new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                loggedInUser = User.fromJSON(response);
                // Toast.makeText(getBaseContext(), "User: " + loggedInUser.getName(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(getBaseContext(), "Failed to get Logged in User info", Toast.LENGTH_LONG).show();
                Log.d("DEBUG", errorResponse.toString());
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    private void getViews() {
//          lvTweets = (ListView) findViewById(R.id.lvTweets);

//        // Code to enable Infinite scrolling
//        lvTweets.setOnScrollListener(new EndlessScrollListener() {
//            @Override
//            protected void onLoadMore(int page, int totalItemCount) {
//                customLoadMoreDataFromAPI(page);
//            }
//        });


        lvTweets = (ListView) findViewById(R.id.lvTweets);
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // your code to refresh the list here
                //  Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                fetchTimelineAsync(1);
            }
        });

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        // Code to enable Infinite scrolling
        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            protected void onLoadMore(int page, int totalItemCount) {
                customLoadMoreDataFromAPI(page);
            }
        });
    }

    private void fetchTimelineAsync(int page) {
        client.getHomeTimeline(page, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                // Remember to CLEAR OUT old items before appending in the new ones
                aTweets.clear();

                // Clear db
                // TODO: Delete only tweets from given user
                new Delete().from(Tweet.class).execute();
                new Delete().from(User.class).execute();

                // This would have created db again
                aTweets.addAll(Tweet.fromJSONArray(response));



                swipeContainer.setRefreshing(false);
                // Toast.makeText(getBaseContext(), "Login Success", Toast.LENGTH_LONG).show();
                // Log.d("DEBUG", response.toString());

                // Deserialize JSON
                // Crate Models
                // Load Models into the Listview
                aTweets.addAll(Tweet.fromJSONArray(response));
                Log.d("DEBUG", aTweets.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(getBaseContext(), "Login Failure", Toast.LENGTH_LONG).show();
                Log.d("DEBUG", errorResponse.toString());
                swipeContainer.setRefreshing(false);
            }
        });
    }

    private void customLoadMoreDataFromAPI(int page) {
        client.getHomeTimeline(page, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                // Toast.makeText(getBaseContext(), "Login Success", Toast.LENGTH_LONG).show();
                // Log.d("DEBUG", response.toString());

                // Deserialize JSON
                // Crate Models
                // Load Models into the Listview
                aTweets.addAll(Tweet.fromJSONArray(response));
                Log.d("DEBUG", aTweets.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(getBaseContext(), "Login Failure", Toast.LENGTH_LONG).show();
                Log.d("DEBUG", errorResponse.toString());
            }
        });
    }

    private void populateTimeline() {

        // Load from the very beginning
        client.getHomeTimeline(1, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                // Toast.makeText(getBaseContext(), "Login Success", Toast.LENGTH_LONG).show();
                // Log.d("DEBUG", response.toString());

                // Deserialize JSON
                // Crate Models
                // Load Models into the Listview
                aTweets.addAll(Tweet.fromJSONArray(response));
                Log.d("DEBUG", aTweets.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(getBaseContext(), "Login Failure", Toast.LENGTH_LONG).show();
                Log.d("DEBUG", errorResponse.toString());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.miCompose) {
            // Toast.makeText(getBaseContext(), "Compose Clicked", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this, ComposeActivity.class);
            i.putExtra("loggedInUser", loggedInUser);
            startActivityForResult(i, UPDATE_STATUS);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == UPDATE_STATUS) {
            if(RESULT_OK == resultCode) {
                Tweet tweet = (Tweet) data.getSerializableExtra("tweet");
                aTweets.insert(tweet, 0);
            }
            if(RESULT_CANCELED == resultCode) {
                // Do nothing here
            }
        }
    }
}
