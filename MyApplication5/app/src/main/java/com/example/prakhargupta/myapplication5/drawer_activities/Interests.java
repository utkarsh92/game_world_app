package com.example.prakhargupta.myapplication5.drawer_activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.prakhargupta.myapplication5.MainActivity;
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
import java.util.concurrent.ThreadPoolExecutor;

import static android.widget.Toast.LENGTH_SHORT;

public class Interests extends AppCompatActivity {

    AutoCompleteTextView actv1;
    ImageButton ib1;
//    public static String[] autoData = new String[50];
    private String[] autoData = new String[500];
//    private String[] gameData = new String[50];
    private String new_game, request;
    private String userid;

    private ListView listView, listView1;
    private FeedListAdapter listAdapter, listAdapter1;
    private List<FeedItem> feedItems;

    private String URL_FEED = "http://gameworld.pythonanywhere.com/";
    private String temp = URL_FEED;
    private ProgressDialog pDialog;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.interests);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        for (int j = 0; j < 500; j++)
        {
            autoData[j] = "";
//            gameData[j] = "";
        }

        actv1 = (AutoCompleteTextView) findViewById(R.id.auto);
        listView = (ListView) findViewById(R.id.mygames);
        ib1 = (ImageButton) findViewById(R.id.addInterest);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        feedItems = new ArrayList<FeedItem>();


        SharedPreferences sp = getSharedPreferences("data", MODE_PRIVATE);
        final SharedPreferences.Editor ed = sp.edit();

        userid = sp.getString("user_id", "N/A");

        URL_FEED = URL_FEED + "autocomplete_games" + userid;
        getautoData();
        URL_FEED = temp;


        listAdapter = new FeedListAdapter(this, feedItems);
        listView.setAdapter(listAdapter);


        URL_FEED = URL_FEED + "users_interest" + userid;
        setInterests();
        URL_FEED = temp;


        ib1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new_game = actv1.getText().toString();
                System.out.println(new_game);

                if (new_game.equals(""))
                {
                    Toast.makeText(getApplication(),"Interest cannot be blank :/",LENGTH_SHORT).show();
                    return;
                }else
                {
                    URL_FEED = URL_FEED + "add_interest_app" + userid;
                    addInterest();
                    URL_FEED = temp;
                }
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

    private void func()
    {

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,autoData);
        actv1.setThreshold(1);
        actv1.setAdapter(adapter);

    }

    private void getautoData()
    {
                showpDialog();

        StringRequest sr = new StringRequest(Request.Method.POST, URL_FEED, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //System.out.println("0");
                try{

//                    System.out.println(response);
//                    Toast.makeText(getApplication(),response,Toast.LENGTH_LONG).show();
                    JSONObject obj1 = new JSONObject(response);
                    JSONArray games = new JSONArray(obj1.getString("game_name"));
                    int len = Integer.parseInt(obj1.getString("count"));

                    for (int i = 0; i < len; i++)
                    {
                        String g = games.get(i).toString();
                        autoData[i] = g;
                    }

                    func();

//                    Toast.makeText(getApplication(),"asdf",Toast.LENGTH_LONG).show();
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

//    private void func1()
//    {
//
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,gameData);
//        listView.setAdapter(adapter);
//
//    }


    private void setInterests()
    {
        showpDialog();

        StringRequest sr = new StringRequest(Request.Method.POST, URL_FEED, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //System.out.println("0");
                try{

                    if (response.equals(""))
                    {
                        Toast.makeText(getApplication(),"You don't have any favourite games :/",Toast.LENGTH_LONG).show();
                        return;
                    } else {
//                    System.out.println(response);
//                    Toast.makeText(getApplication(),response,Toast.LENGTH_LONG).show();
                        JSONObject obj1 = new JSONObject(response);
                        JSONArray mygames = new JSONArray(obj1.getString("game_name_user"));
                        int len = mygames.length();

                        for (int i = 0; i < len; i++) {

                            FeedItem item = new FeedItem();

                            JSONObject obj2 = new JSONObject(mygames.get(i).toString());
                            String g = obj2.getString("game_name");
                            item.setName(g);
                            item.setType(3);

                            feedItems.add(item);
//                        System.out.println(g);
//                            gameData[i] = g;
//                        System.out.println(gameData[i]);
                        }

                        //func1();


                        // notify data changes to list adapater
                        listAdapter.notifyDataSetChanged();
                    }
//                    Toast.makeText(getApplication(),"asdf",Toast.LENGTH_LONG).show();
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


    private void addInterest()
    {
        showpDialog();

        request = "{\n" +
                " \"interest_list\" : [\n" +
                "\"" + new_game + "\"\n" +
                "]\n" +
                "}";


        StringRequest sr = new StringRequest(Request.Method.POST, URL_FEED, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //System.out.println("0");
                try{

                    JSONObject obj1 = new JSONObject(response);
                    String str = obj1.getString("status");

                    if (str.equals("success"))
                    {
                        URL_FEED = URL_FEED + "autocomplete_games" + userid;
                        getautoData();
                        URL_FEED = temp;

                        URL_FEED = URL_FEED + "users_interest" + userid;
                        setInterests();
                        URL_FEED = temp;

                        Toast.makeText(getApplication(),"New game added :D",Toast.LENGTH_LONG).show();
                        actv1.setText("");

                        listAdapter = new FeedListAdapter(Interests.this, feedItems);
                        listView1.setAdapter(listAdapter);
                        // notify data changes to list adapater
                        listAdapter.notifyDataSetChanged();

                        //finish();


                    }else if (str.equals("error"))
                    {
                        Toast.makeText(getApplication(),"Failed :(",Toast.LENGTH_LONG).show();
                    }else
                    {
                        Toast.makeText(getApplication(),"wtf prakhar",Toast.LENGTH_LONG).show();
                    }

                    hidepDialog();

                }catch (Exception e)
                {
                    Toast.makeText(getApplication(), "Something went wrong. Please try again :/", Toast.LENGTH_SHORT).show();
                    System.out.println(e);
                    hidepDialog();
//                    finish();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //error.printStackTrace();
                System.out.println(error);
                Toast.makeText(getApplication(),"Server is not working :(",LENGTH_SHORT).show();
                hidepDialog();
//                finish();

            }
        }) {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("interest_list", new_game);
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
        finish();
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
