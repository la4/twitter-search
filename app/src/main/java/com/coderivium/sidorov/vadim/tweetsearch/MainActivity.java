package com.coderivium.sidorov.vadim.tweetsearch;

import android.app.SearchManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

public class MainActivity extends AppCompatActivity {

    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    //Bitmap avatarPlaceholder;
    private Twitter twitter;
    private TweetAdapter adapter;
    private final AccessToken accessToken
            = new AccessToken(TwitterConstants.TWITTER_ACCES_TOKEN, TwitterConstants.TWITTER_ACCES_TOKEN_SECRET);

    private static final String TWEETS_GETTING = "Searching for tweets...";

    private List<Status> tweets;
    private static final int TWEETS_AMOUNT = 20;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ListView listView = (ListView) findViewById(R.id.listView);

        setSupportActionBar(toolbar);


        Bitmap sampleAvatar = BitmapFactory.decodeResource(getResources(), R.drawable.avatar_demo);


        tweets = new ArrayList<>();

        adapter = new TweetAdapter(this, R.layout.element_list, (ArrayList) tweets);
        listView.setAdapter(adapter);


        twitter = new TwitterFactory().getInstance();
        twitter.setOAuthConsumer(TwitterConstants.TWITTER_CONSUMER_KEY, TwitterConstants.TWITTER_CONSUMER_SECRET);
        twitter.setOAuthAccessToken(accessToken);

        //avatarPlaceholder = BitmapFactory.decodeResource(getResources(), R.drawable.avatar_demo);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();

        if (searchView != null) {
            searchView.setSearchableInfo(searchManager
                    .getSearchableInfo(getComponentName()));
            searchView.setIconifiedByDefault(true);
            //searchView.setMaxWidth();
        }

        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            public boolean onQueryTextChange(String newText) {
                // This is my adapter that will be filtered
                return true;
            }

            public boolean onQueryTextSubmit(String query) {
                if (searchView != null) {
                    searchView.clearFocus();
                }

                searchAction(query);
                return true;
            }
        };

        if (searchView != null) {
            searchView.setOnQueryTextListener(queryTextListener);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    private void searchAction(String searchString) {
        TweetsGetter tweetsGetter = new TweetsGetter();
        tweetsGetter.execute(searchString);
    }

    class TweetsGetter extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
            Toast.makeText(getApplicationContext(), TWEETS_GETTING, Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                Log.d(LOG_TAG, params[0]);

                Query query = new Query(params[0]);
                QueryResult result;
                query.setCount(TWEETS_AMOUNT);
                result = twitter.search(query);

                if (result.getTweets().size() > 0) {
                    tweets.addAll(0, result.getTweets());
                    Log.d(LOG_TAG, "Tweets added");
                }
            } catch (TwitterException te) {
                te.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void result) {
            adapter.notifyDataSetChanged();
        }
    }
}
