package com.codepath.apps.SJTweetsApp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.SJTweetsApp.models.Tweet;
import com.codepath.apps.SJTweetsApp.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONObject;
import android.util.Log;

public class ComposeActivity extends ActionBarActivity {

    private TwitterClient client;

    private User loggedInUser;

    private ImageView ivProfile;
    private TextView tvName;
    private TextView tvScreenName;
    private EditText etBody;
    private TextWatcher twBody;
    private TextView tvBodyCount;


    private Context ctx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        client = TwitterApp.getRestClient();
        // Get the loggedInUser info from the intent
        loggedInUser = (User) getIntent().getSerializableExtra("loggedInUser");
        // Toast.makeText(getBaseContext(), "User " + loggedInUser.getName(), Toast.LENGTH_SHORT).show();
        populateViews();

    }

    private void populateViews() {

        ctx = this;
        ivProfile = (ImageView) findViewById(R.id.ivProfile);
        tvName = (TextView) findViewById(R.id.tvName);
        tvScreenName = (TextView) findViewById(R.id.tvScreenName);
        etBody = (EditText) findViewById(R.id.etBody);
        tvBodyCount = (TextView) findViewById(R.id.tvBodyCount);

        ivProfile.setImageResource(android.R.color.transparent);
        Picasso.with(this).load(loggedInUser.getProfileImageUrl()).into(ivProfile);

        tvName.setText(loggedInUser.getName());
        tvScreenName.setText("@" + loggedInUser.getScreenName());

        twBody = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Update a TextView to current char count
                tvBodyCount.setText(String.valueOf(140 - s.length()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        etBody.addTextChangedListener(twBody);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_compose, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onTweet(View view) {
        String body = etBody.getText().toString();

        if((body == null) || (body.isEmpty())) {
            Toast.makeText(getBaseContext(), "Say something!", Toast.LENGTH_SHORT).show();
            return;
        }
        postStatus(body);
    }

    private void postStatus(String body) {
        client.postStatus(body, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                // response JSONObject has Tweet. Return it to parent activity to insert into tweets
                Tweet tweet = Tweet.fromJSON(response);
                Intent i = new Intent();
                i.putExtra("tweet", tweet);
                setResult(RESULT_OK, i);
                finish();
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(getBaseContext(), throwable.toString(), Toast.LENGTH_SHORT).show();
                Log.d("DEBUG", errorResponse.toString());

            }
        });
    }

    public void onCancel(View view) {
        Intent i = new Intent();
        setResult(RESULT_CANCELED);
        finish();
    }
}
