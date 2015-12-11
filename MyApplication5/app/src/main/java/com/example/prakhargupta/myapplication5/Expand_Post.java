package com.example.prakhargupta.myapplication5;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.prakhargupta.myapplication5.R;
import com.example.prakhargupta.myapplication5.adapter.FeedListAdapter;
import com.example.prakhargupta.myapplication5.app.AppController;
import com.example.prakhargupta.myapplication5.data.FeedItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.widget.Toast.LENGTH_SHORT;

public class Expand_Post extends AppCompatActivity {

    private ListView listView;
    private FeedListAdapter listAdapter;
    private List<FeedItem> feedItems;
    //    private String URL_FEED = "http://api.androidhive.info/feed/feed.json";
    private String URL_FEED = "http://gameworld.pythonanywhere.com/expand";
//    private String temp = URL_FEED;
    String propic = "https://upload.wikimedia.org/wikipedia/en/7/70/Shawn_Tok_Profile.jpg";

    Intent intent;
    String post_title,post_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expand_post);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = (ListView) findViewById(R.id.list);

        feedItems = new ArrayList<FeedItem>();

        listAdapter = new FeedListAdapter(this, feedItems);
        listView.setAdapter(listAdapter);

        getClickedPost();
        setLikes();
        makeJsonObjectRequest();

//        listAdapter.notifyDataSetChanged();
    }


    private void getClickedPost()
    {
        FeedItem item = new FeedItem();
        intent = getIntent();

        item.setProfilePic(propic);
        String name = intent.getExtras().getString("username");
        item.setName(name);
        String date = intent.getExtras().getString("date");
        item.setDate(date);
        String title = intent.getExtras().getString("title");
        post_title = title;
        item.setTitle(title);
        String content = intent.getExtras().getString("content");
        item.setContent(content);
        String likes = "-99";
        item.setLikes(likes);
        String comments = "-99";
        item.setComments(comments);
        item.setType(0);
        post_type = intent.getExtras().getString("post_type");

        feedItems.add(item);
    }

    private void setLikes()
    {
        FeedItem item = new FeedItem();
        intent = getIntent();
        String likes = intent.getExtras().getString("likes");
        item.setLikes(likes);
        String likedornot = intent.getExtras().getString("likedornot");
        item.setLikedornot(likedornot);
        item.setType(1);

        feedItems.add(item);
    }


    private void makeJsonObjectRequest() {

//        showpDialog();

        StringRequest sr = new StringRequest(Request.Method.POST, URL_FEED, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //System.out.println("0");
                try{
                    //Toast.makeText(getApplication(),response,Toast.LENGTH_LONG).show();
                    System.out.println(response);

                    JSONObject obj1 = new JSONObject(response);
                    String status = obj1.getString("status");

                    if (status.equals("comments")) {
                        JSONArray unames = new JSONArray(obj1.getString("users"));
                        JSONArray comments = new JSONArray(obj1.getString("display"));
                        int no_of_posts = Integer.parseInt(obj1.getString("count"));

                        for (int i = 0; i < no_of_posts; i++) {
                            FeedItem item = new FeedItem();

                            String name = unames.get(i).toString();
                            item.setName(name);
//                        Toast.makeText(getApplication(),name,Toast.LENGTH_LONG).show();

                            JSONObject obj2 = new JSONObject(comments.get(i).toString());
                            String date = obj2.getString("date");
                            item.setDate(date);
//                        Toast.makeText(getApplication(),date,Toast.LENGTH_LONG).show();

                            String content = obj2.getString("content");
                            item.setContent(content);
//                        Toast.makeText(getApplication(),content,Toast.LENGTH_LONG).show();

                            item.setType(2);

                            feedItems.add(item);
                        }


                        // notify data changes to list adapater
                        listAdapter.notifyDataSetChanged();


                    }else if (status.equals("no comments"))
                    {
                        Toast.makeText(getApplication(),"No comments...",Toast.LENGTH_SHORT).show();
                    }

                }catch (Exception e)
                {
                    Toast.makeText(getApplication(), "Something went wrong. Please try again :/", Toast.LENGTH_SHORT).show();
                    System.out.println(e);
//                    hidepDialog();
                    finish();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //error.printStackTrace();
                System.out.println(error);
                Toast.makeText(getApplication(),"Server is not working :(",LENGTH_SHORT).show();
//                hidepDialog();
                finish();

            }
        }) {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                System.out.println(post_title);
                System.out.println(post_type);
                params.put("title", post_title);
                params.put("type", post_type);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(sr);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
