package com.coderivium.sidorov.vadim.tweetsearch;

import android.app.SearchManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
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

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final String TWEETS_GETTING = "Searching for tweets";
    private static final int MAX_TWEETS_LOAD = 100;
    private static final int TWEETS_AMOUNT = 20;

    private final AccessToken accessToken = new AccessToken(TwitterConstants.TWITTER_ACCES_TOKEN, TwitterConstants.TWITTER_ACCES_TOKEN_SECRET);

    private Twitter twitter;
    private TweetAdapter adapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private List<Status> tweets;
    private String currentSearchQuery = null;
    ListView listView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView = (ListView) findViewById(R.id.listView);

        tweets = new ArrayList<>();

        adapter = new TweetAdapter(this, R.layout.element_list, (ArrayList) tweets);
        listView.setAdapter(adapter);


        twitter = new TwitterFactory().getInstance();
        twitter.setOAuthConsumer(TwitterConstants.TWITTER_CONSUMER_KEY, TwitterConstants.TWITTER_CONSUMER_SECRET);
        twitter.setOAuthAccessToken(accessToken);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_main_swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.blue, R.color.green, R.color.orange);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshContent();
            }
        });
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
                currentSearchQuery = query;
                searchAction(currentSearchQuery);
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

    private void refreshContent() {
        TweetsRefresher refresherTask = new TweetsRefresher();
        refresherTask.execute(currentSearchQuery);
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
                tweets.clear();

                if (result.getTweets().size() > 0) {
                    tweets.addAll(0, result.getTweets()); // Is it thread safe?
                    Log.d(LOG_TAG, "Added " + String.valueOf(result.getTweets().size()) + " tweets.");
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

    class TweetsRefresher extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            try {
                if (currentSearchQuery == null) return null;

                Query query = new Query(params[0]);
                QueryResult result;
                query.sinceId(tweets.get(0).getId());
                query.setCount(MAX_TWEETS_LOAD); //To prevent overloading if a lot of updates
                result = twitter.search(query);

                if (result.getTweets().size() > 0) {
                    tweets.addAll(0, result.getTweets()); // Is it thread safe?
                    Log.d(LOG_TAG, "Added " + String.valueOf(result.getTweets().size()) + " tweets.");
                }
            } catch (TwitterException te) {
                te.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            adapter.notifyDataSetChanged();
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }
}
