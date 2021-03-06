package com.tmnt.tritontrade.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.tmnt.tritontrade.R;
import com.tmnt.tritontrade.controller.CurrentState;
import com.tmnt.tritontrade.controller.Post;
import com.tmnt.tritontrade.controller.Server;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.tmnt.tritontrade.R.id.bottom_cart;
import static com.tmnt.tritontrade.R.id.bottom_edit_category;
import static com.tmnt.tritontrade.R.id.bottom_mainfeed;
import static com.tmnt.tritontrade.R.id.bottom_profile;
import static com.tmnt.tritontrade.R.id.bottom_upload;
import static com.tmnt.tritontrade.view.MainActivity.CAT_1;
import static com.tmnt.tritontrade.view.MainActivity.CAT_10;
import static com.tmnt.tritontrade.view.MainActivity.CAT_2;
import static com.tmnt.tritontrade.view.MainActivity.CAT_3;
import static com.tmnt.tritontrade.view.MainActivity.CAT_4;
import static com.tmnt.tritontrade.view.MainActivity.CAT_5;
import static com.tmnt.tritontrade.view.MainActivity.CAT_6;
import static com.tmnt.tritontrade.view.MainActivity.CAT_7;
import static com.tmnt.tritontrade.view.MainActivity.CAT_8;
import static com.tmnt.tritontrade.view.MainActivity.CAT_9;

/**
 * Created by Edward Ji
 */

public class Mainfeed extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private CustomAdapter adapter;
    private ListView list;
    private SwipeRefreshLayout swipeContainer;
    private ArrayList<String> lastSearchedTags;
    private Spinner filters;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainfeed);
        setTitle("My Feed");
        list = (ListView) this.findViewById(R.id.listFeed);
        list.setDividerHeight(15);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //DRAWERS
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.left_drawer);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.bringToFront();

        ArrayList<String> tags = new ArrayList<>();
        //fillDefaultTags(tags);
        /***SET DATA***/
//        tags.add("food");
        lastSearchedTags=tags;
        fillDefaultTags(tags);
        setAdapterInfo(tags);


        //PULL REFRESH
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                setAdapterInfo(lastSearchedTags);
                swipeContainer.setRefreshing(false);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_dark,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        //bottom tool bar
        final BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        bottomNavigationView.getMenu().getItem(0).setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener(){
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        bottomNavigationView.getMenu().getItem(0);
                        Intent in;
                        switch(item.getItemId()){
                            case bottom_mainfeed:
                                in=new Intent(getBaseContext(),Mainfeed.class);
                                in.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                startActivity(in);
                                return true;
                            case bottom_edit_category:
                                in = new Intent(getBaseContext(), Edit_Categories.class);
                                in.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                startActivity(in);
                                return true;
                            case bottom_cart:
                                in=new Intent(getBaseContext(),Cart.class);
                                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(in);
                                return true;
                            case bottom_upload:
                                in=new Intent(getBaseContext(),Create_Post.class);
                                in.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                startActivity(in);
                                return true;
                            case bottom_profile:
                                in=new Intent(getBaseContext(),Profile.class);
                                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(in);
                                return true;
                            default:
                                return false;
                        }
                    }
                }
        );
        addItemsOnFiltersSpinner();
        configureFilters();
    }

    /**
     * Fills ArrayList with users preferred tags.
     * @param tags
     */
    private void fillDefaultTags(ArrayList<String> tags){
        if(!CurrentState.getInstance().isLoggedIn()){
            CurrentState.getInstance().killLogin(this);
            return;
        }
        String userID = Integer.toString(CurrentState.getInstance().getCurrentUser().getProfileID());
        SharedPreferences tagNames = getSharedPreferences(userID, Context.MODE_PRIVATE);
        Set<String> tagSet = tagNames.getStringSet(userID,new HashSet<String>());

        System.out.println(tagSet);
        Log.d("DEBUG", "2.set = "+tagNames.getStringSet("set", new HashSet<String>()));
        if(tagSet.isEmpty()){
            return;
        }
        for(String s: tagSet){
            tags.add(s);
        }
    }

    //TODO Frank
    private void configureFilters(){
        if(filters != null && filters.getSelectedItem() != null && filters.getSelectedItem().toString() != null && adapter != null) {
            adapter.setCurrentFilters(filters.getSelectedItem().toString());
        }
    }

    /**
     * Takes in tags and set appropriate content in grid
     * @param tags the tags to be displayed
     */
    private void setAdapterInfo(ArrayList<String> tags){
        new FeedSetupTask().execute(tags);
        final SearchView sv = (SearchView) findViewById(R.id.searchView);
        //Search Bar implementation
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                ArrayList<String> tagsQ = new ArrayList<String>();
                tagsQ.add(query);

                String [] querySplit = query.split(" ");
                for(String s : querySplit){
                    s = s.toLowerCase();

                    if(!s.equals("")){
                        tagsQ.add(s);
                    }

                }

                lastSearchedTags=tagsQ;
                new FeedSetupTask().execute(tagsQ);
                sv.clearFocus();
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                if(adapter != null) {
                    adapter.getFilter().filter(newText);
                }
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            // you shall not pass
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.mainfeed, menu);
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
            Intent intent = new Intent(this, Settings.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Left navigation bar
     * @param item
     * @return
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        int id = item.getItemId();
        ArrayList<String> tags= new ArrayList<>();
        if(id==R.id.following_sidebar){
            fillDefaultTags(tags);
        } else if (id == R.id.clothing_sidebar) {
            tags.add(CAT_1);
        } else if (id == R.id.food_sidebar) {
            tags.add(CAT_2);
        } else if (id == R.id.furniture_sidebar) {
            tags.add(CAT_3);
        } else if (id == R.id.services_sidebar) {
            tags.add(CAT_4);
        } else if (id == R.id.storage_sidebar) {
            tags.add(CAT_5);
        } else if (id == R.id.tickets_sidebar) {
            tags.add(CAT_6);
        } else if (id == R.id.technology_sidebar) {
            tags.add(CAT_7);
        } else if (id == R.id.textbooks_sidebar) {
            tags.add(CAT_8);
        } else if (id == R.id.transportation_sidebar) {
            tags.add(CAT_9);
        } else if (id == R.id.misc_sidebar) {
            tags.add(CAT_10);
        }
        lastSearchedTags=tags;
        setAdapterInfo(tags);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Async task used for initial setup of mainfeed(posts)
     */
    private class FeedSetupTask extends AsyncTask<ArrayList<String>, Void, ArrayList<Post>> {
        private ProgressDialog dialog = new ProgressDialog(Mainfeed.this);

        protected ArrayList<Post> doInBackground(ArrayList<String>... id) {
            try {
                Log.d("DEBUG",id.length + "");
                Log.d("DEBUG", id[0].get(0));
                for (int x = 0; x < id[0].size(); x ++)
                {
                    id[0].set(x,id[0].get(x).toLowerCase());
                }


                ArrayList<Post> posts = Server.searchPostTags(id[0]);
                return posts;
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Loading");
            this.dialog.show();
        }

        protected void onPostExecute(ArrayList<Post> result) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            if (result != null) {
                adapter = new CustomAdapter(Mainfeed.this, result);
                list.setAdapter(adapter);
                list.setOnScrollListener(new AbsListView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(AbsListView view, int scrollState) {
                        int count = list.getCount();
                        if(list.getLastVisiblePosition()>=count-1){
                            adapter.showMore();
                        }

                    }

                    @Override
                    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                       // adapter.showMore();
                    }


                });
            }
        }
    }

    /**
     * Category selection spinner for the post item
     */
    private void addItemsOnFiltersSpinner(){
        filters = (Spinner) findViewById(R.id.filters);
        List<String> filterOptions = new ArrayList<>();
        filterOptions.add("Most Recent");
        filterOptions.add("Price: Lowest to Highest");
        filterOptions.add("Price: Highest to Lowest");
        filterOptions.add("Buying");
        filterOptions.add("Selling");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, filterOptions);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filters.setAdapter(dataAdapter);
        filters.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                configureFilters();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                configureFilters();
            }
        });
    }


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Mainfeed Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}


