package com.example.prakhargupta.myapplication5;

import android.app.ProgressDialog;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.prakhargupta.myapplication5.Tabs.Login_Page;
import com.example.prakhargupta.myapplication5.adapter.FeedListAdapter;
import com.example.prakhargupta.myapplication5.app.AppController;
import com.example.prakhargupta.myapplication5.data.FeedItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.prakhargupta.myapplication5.drawer_activities.Contact_Us;
import com.example.prakhargupta.myapplication5.drawer_activities.Interests;
import com.example.prakhargupta.myapplication5.drawer_activities.Profile;

import static android.widget.Toast.LENGTH_SHORT;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

//    Fragment objFragment=null;
//    Class fragmentClass = Fragment_One.class;

    private static final String TAG = MainActivity.class.getSimpleName();
    private ListView listView;
    private FeedListAdapter listAdapter;
    private List<FeedItem> feedItems;
//    private String URL_FEED = "http://api.androidhive.info/feed/feed.json";
    private String URL_FEED = "http://gameworld.pythonanywhere.com/timeline";
    private String temp = URL_FEED;
    String propic = "https://upload.wikimedia.org/wikipedia/en/7/70/Shawn_Tok_Profile.jpg";
    ProgressDialog pDialog;

    Intent intent;

    TextView tv1,tv2;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        SharedPreferences sp = getSharedPreferences("data", MODE_PRIVATE);
        final SharedPreferences.Editor ed = sp.edit();

        String email = sp.getString("email", "N/A");
        String uname = sp.getString("username", "N/A");
        String userid = sp.getString("user_id", "N/A");

        tv1 = (TextView) findViewById(R.id.emailView);
        tv1.setText(email);
        tv2 = (TextView) findViewById(R.id.unameView);
        tv2.setText(uname);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                fragmentClass=Post_Add.class;
//                try {
//                    objFragment = (Fragment) fragmentClass.newInstance();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                android.app.FragmentManager fragmentManager= getFragmentManager();
//                fragmentManager.beginTransaction().replace(R.id.frame, objFragment).commit();
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                intent = new Intent(MainActivity.this,Post_Add.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //*************************************************888
        //NEW PART
        //**************************************************888


        listView = (ListView) findViewById(R.id.list);

        feedItems = new ArrayList<FeedItem>();

        listAdapter = new FeedListAdapter(this, feedItems);
        listView.setAdapter(listAdapter);

        URL_FEED = URL_FEED + userid;
        makeJsonObjectRequest();
        URL_FEED = temp;

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intent = new Intent(MainActivity.this,Expand_Post.class);

//                FeedItem data = feedItems.get(position);

                String uname = feedItems.get(position).getName();
                String date = feedItems.get(position).getDate();
                String title = feedItems.get(position).getTitle();
                String content = feedItems.get(position).getContent();
                String likes = feedItems.get(position).getLikes();
                int type = feedItems.get(position).getType();
                String p_type = feedItems.get(position).getPost_type();
                String likedornot = feedItems.get(position).getLikedornot();

                intent.putExtra("username", uname);
                intent.putExtra("date", date);
                intent.putExtra("title", title);
                intent.putExtra("content", content);
                intent.putExtra("likes", likes);
                intent.putExtra("type", type);
                intent.putExtra("post_type", p_type);
                intent.putExtra("likedornot", likedornot);

                startActivity(intent);
            }
        });
    }


    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


    private void makeJsonObjectRequest() {

        showpDialog();

        StringRequest sr = new StringRequest(Request.Method.POST, URL_FEED, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //System.out.println("0");
                try{

                    System.out.println(response);

                    JSONObject obj1 = new JSONObject(response);
                    int no_of_posts = Integer.parseInt(obj1.getString("count"));
                    JSONArray unames = new JSONArray(obj1.getString("users"));
                    JSONArray post_dates = new JSONArray(obj1.getString("post_date"));
                    JSONArray posts = new JSONArray(obj1.getString("post"));
                    JSONArray post_type = new JSONArray(obj1.getString("post_type"));
                    JSONArray likedornot = new JSONArray(obj1.getString("like"));
                    JSONArray reply_count = new JSONArray(obj1.getString("reply_count"));


                    for (int i = 0; i < no_of_posts ; i++)
                    {
                        FeedItem item = new FeedItem();

                        item.setProfilePic(propic);
                        String name = unames.get(i).toString();
                        item.setName(name);
                        String date = post_dates.get(i).toString();
                        item.setDate(date);
                        JSONObject obj2 = new JSONObject(posts.get(i).toString());
                        String title = obj2.getString("title");
                        item.setTitle(title);
                        String content = obj2.getString("content");
                        item.setContent(content);
                        String likes = obj2.getString("likes");
                        item.setLikes(likes);
                        String p_type = post_type.get(i).toString();
                        item.setPost_type(p_type);
                        String liked = likedornot.get(i).toString();
                        item.setLikedornot(liked);
                        String comments = reply_count.get(i).toString();
                        item.setComments(comments);

                        item.setType(0);

                        feedItems.add(item);
                    }


                    // notify data changes to list adapater
                    listAdapter.notifyDataSetChanged();
                    hidepDialog();

                }catch (Exception e)
                {
                    Toast.makeText(getApplication(), "Something went wrong. Please try again :/", Toast.LENGTH_SHORT).show();
                    System.out.println(e);
                    hidepDialog();
                    finish();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //error.printStackTrace();
                System.out.println(error);
                Toast.makeText(getApplication(),"Server is not working :(",LENGTH_SHORT).show();
                hidepDialog();
                finish();

            }
        }) {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("user_id","123");

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
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
            intent = new Intent(MainActivity.this, About_Us.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        int id = item.getItemId();

        if (id == R.id.nav_refresh) {
//            fragmentClass = Fragment_Two.class;
            intent = new Intent(MainActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        else if (id == R.id.nav_interests) {
//            fragmentClass = Fragment_One.class;
            intent = new Intent(MainActivity.this,Interests.class);
            startActivity(intent);
//            startActivityForResult(intent,1);


//        } else if (id == R.id.nav_games) {
////            fragmentClass = Fragment_Two.class;
//            intent = new Intent(MainActivity.this,Games.class);
//            startActivity(intent);

        } else if (id == R.id.nav_my_profile) {
//            fragmentClass = Fragment_One.class;
            intent = new Intent(MainActivity.this,Profile.class);
            startActivity(intent);

//        } else if (id == R.id.nav_settings) {
////            fragmentClass = Fragment_Two.class;

        }
        else if (id == R.id.nav_logout) {

            AlertDialog.Builder logout = new AlertDialog.Builder(MainActivity.this);
            logout.setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            SharedPreferences sp = getSharedPreferences("data",MODE_PRIVATE);
                            final SharedPreferences.Editor ed = sp.edit();

                            ed.putString("username", "N/A");
                            ed.putString("user_id", "N/A");
                            ed.commit();
                            intent = new Intent(MainActivity.this,Login_Page.class);
                            startActivity(intent);
                            finish();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert = logout.create();
            alert.setTitle("Are you sure?");
            alert.show();
        }
        else if (id == R.id.nav_contact) {
//            fragmentClass = Fragment_Two.class;
            intent = new Intent(MainActivity.this,Contact_Us.class);
            startActivity(intent);

        }
        else if (id == R.id.nav_website) {
//            fragmentClass = Fragment_One.class;
            Uri uri = Uri.parse("http://gameworld.pythonanywhere.com/"); // missing 'http://' will cause crashed
            intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }

//        try {
//            objFragment = (Fragment) fragmentClass.newInstance();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        android.app.FragmentManager fragmentManager= getFragmentManager();
//        fragmentManager.beginTransaction().replace(R.id.frame, objFragment).commit();



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
