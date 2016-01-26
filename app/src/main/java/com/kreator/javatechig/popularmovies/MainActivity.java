package com.kreator.javatechig.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private MovieListAdapter adapter;
    private List<FeedItem> feedsList;

    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;

        }
        /*else if (id == R.id.action_refresh) {
            updateMovieList();
        }*/

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateMovieList();
    }

    protected void updateMovieList() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences
                (getApplicationContext());
        String sortMethod = sharedPreferences.getString(getString(R.string.pref_sort_key), getString(R
                .string
                .pref_sortby_popularity));

        // set a deafault value for the url
        url = "http://api.themoviedb.org/3/discover/movie?sort_by=popularity." +
                "desc&api_key=acac23a9cfbb42129cbd85c065c739a5";

        // set the url from the preferences
        if (sortMethod.equals(getString(R.string.pref_sortby_popularity))) {
            url = "http://api.themoviedb.org/3/discover/movie?sort_by=popularity." +
                    "desc&api_key=acac23a9cfbb42129cbd85c065c739a5";
        } else if (sortMethod.equals(getString(R.string.pref_sortby_rating))) {
            url = "http://api.themoviedb.org/3/discover/movie?sort_by=vote_average." +
                    "desc&api_key=acac23a9cfbb42129cbd85c065c739a5";
        }

        new AsyncHttpTask().execute(url);
    }

    @Override
    public void onClick(View v) {
    }

    public class AsyncHttpTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPreExecute() {
            setProgressBarIndeterminateVisibility(true);
        }

        @Override
        protected Integer doInBackground(String... params) {
            Integer result = 0;
            HttpURLConnection connection;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                int statusCode = connection.getResponseCode();

                if (statusCode == 200) {
                    BufferedReader r = new BufferedReader(new InputStreamReader(connection
                            .getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;

                    while ((line = r.readLine()) != null) {
                        response.append(line);
                    }

                    parseResult(response.toString());

                    result = 1;
                } else {
                    result = 0;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return result;

        }

        @Override
        protected void onPostExecute(Integer result) {
            progressBar.setVisibility(View.GONE);

            if (result == 1) {
                adapter = new MovieListAdapter(MainActivity.this, feedsList);
                recyclerView.setAdapter(adapter);
                adapter.setOnItemClickListener(new MovieListAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        int itemPosition = recyclerView.getChildAdapterPosition(view);
                        FeedItem item = feedsList.get(itemPosition);

                        Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                        Bundle extras = new Bundle();

                        extras.putString("poster_path", item.getImage());
                        extras.putString("original_title", item.getOriginalTitle());
                        extras.putString("backdrop_path", item.getBackdropPath());
                        extras.putString("release_date", item.getReleaseDate());
                        extras.putString("synopsis", item.getSynopsis());
                        extras.putString("user_rating", item.getUserRating());

                        intent.putExtras(extras);

                        startActivity(intent);
                    }
                });

            } else {
                Toast.makeText(MainActivity.this, "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }

        }

        private void parseResult(String result) {
            try {
                JSONObject response = new JSONObject(result);
                JSONArray posterList = response.optJSONArray("results");
                feedsList = new ArrayList<>();

                for (int i = 0; i < posterList.length(); i++) {
                    JSONObject movie = posterList.optJSONObject(i);
                    FeedItem item = new FeedItem();

                    item.setImage(movie.optString("poster_path"));
                    item.setOriginalTitle(movie.optString("original_title"));
                    item.setSynopsis(movie.optString("overview"));
                    item.setBackdropPath(movie.optString("backdrop_path"));
                    item.setReleaseDate(movie.optString("release_date"));
                    item.setUserRating(movie.optString("vote_average"));

                    feedsList.add(item);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } // end parseResult


    }
}
