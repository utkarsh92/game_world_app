package com.example.prakhargupta.myapplication5.drawer_activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
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

/**
 * Created by Pewds on 30-Nov-15.
 */
public class Following extends AppCompatActivity {

    private ListView listView;
    private FeedListAdapter listAdapter;
    private List<FeedItem> feedItems;

    private String urlJsonObj = "http://gameworld.pythonanywhere.com/profile";
    private ProgressDialog pDialog;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.followers);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        SharedPreferences sp = getSharedPreferences("data",MODE_PRIVATE);
        final SharedPreferences.Editor ed = sp.edit();
        String uname = sp.getString("username", "N/A");
        urlJsonObj = urlJsonObj + uname;

        listView = (ListView) findViewById(R.id.list);

        feedItems = new ArrayList<FeedItem>();

        listAdapter = new FeedListAdapter(this, feedItems);
        listView.setAdapter(listAdapter);

        makeJsonObjectRequest();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView t1 = (TextView) view.findViewById(R.id.item1);

                String other_uname = t1.getText().toString();

                ed.putString("other_profile", other_uname);
                ed.commit();

                intent = new Intent(Following.this, Others_Profile.class);
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

        StringRequest sr = new StringRequest(Request.Method.POST, urlJsonObj, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //System.out.println("0");
                try{

                    JSONObject obj1 = new JSONObject(response);
                    if (response.equals(""))
                    {
                        Toast.makeText(getApplication(),"You're not following anyone :/",Toast.LENGTH_LONG).show();
                        return;
                    } else
                    {
                        JSONArray ar1 = new JSONArray(obj1.getString("following"));

                        for (int i = 0; i < ar1.length(); i++) {
                            FeedItem item = new FeedItem();

                            String name = ar1.get(i).toString();
                            item.setName(name);
                            item.setType(3);

                            feedItems.add(item);
                        }


                        // notify data changes to list adapater
                        listAdapter.notifyDataSetChanged();
                    }
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
                error.printStackTrace();
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
