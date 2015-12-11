package com.example.prakhargupta.myapplication5.drawer_activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.prakhargupta.myapplication5.MainActivity;
import com.example.prakhargupta.myapplication5.R;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.prakhargupta.myapplication5.app.AppController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.widget.Toast.LENGTH_SHORT;

public class Profile extends AppCompatActivity {

    //ImageView propic;
    TextView tv1,tv2,tv3,tv4,tv5,tv6,tv7,tv8,tv9,tv10;
    String fname,lname,email,uname,fers,fing,age,sex,year,month,day,coun,desc,dob;

    private String urlJsonObj = "http://gameworld.pythonanywhere.com/profile";
    private ProgressDialog pDialog;

    Intent intent;

    private NetworkImageView propic;
    private ImageLoader imageLoader = AppController.getInstance().getImageLoader();
//    String url = "https://www.american.edu/uploads/profiles/large/chris_palmer_profile_11.jpg";
    String url = "https://upload.wikimedia.org/wikipedia/en/7/70/Shawn_Tok_Profile.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences sp = getSharedPreferences("data",MODE_PRIVATE);
        final SharedPreferences.Editor ed = sp.edit();
        String uname = sp.getString("username", "N/A");
        urlJsonObj = urlJsonObj + uname;

        initializeViews();

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        propic = (NetworkImageView) findViewById(R.id.ppView);
        propic.setImageUrl(url, imageLoader);

        makeJsonObjectRequest();

        tv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(Profile.this, Followers.class);
                startActivity(intent);
            }
        });

        tv5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(Profile.this,Following.class);
                startActivity(intent);
            }
        });
    }


//    @Override
//    protected void onStart()
//    {
//        super.onStart();
//
//
//    }

    private void initializeViews()
    {
        tv1 = (TextView) findViewById(R.id.nameView);
        tv2 = (TextView) findViewById(R.id.emailView);
        tv3 = (TextView) findViewById(R.id.unameView);
        tv4 = (TextView) findViewById(R.id.fersView);
        tv5 = (TextView) findViewById(R.id.fingView);
//        tv6 = (TextView) findViewById(R.id.ageView);
        tv7 = (TextView) findViewById(R.id.sexView);
        tv8 = (TextView) findViewById(R.id.dobView);
        tv9 = (TextView) findViewById(R.id.countryView);
        tv10 = (TextView) findViewById(R.id.descView);
    }


    //for back button on action bar
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

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private void setViews()
    {
        tv1.setText(fname + " " + lname);
        tv2.setText(email);
        tv3.setText(uname);
        tv4.setText(fers);
        tv5.setText(fing);
//        tv6.setText(age);

        if (sex.equals("M") || sex.equals("m"))
            tv7.setText("Male");
        else if (sex.equals("F") || sex.equals("f"))
            tv7.setText("Female");
        else
            tv7.setText("Sex is wrong");

        tv8.setText(dob);
        tv9.setText(coun);
        tv10.setText(desc);

    }

    private void makeJsonObjectRequest() {

        showpDialog();

        StringRequest sr = new StringRequest(Request.Method.POST, urlJsonObj, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //System.out.println("0");
                try{

                    System.out.println(response);
                    JSONObject obj1 = new JSONObject(response);
                    JSONObject obj2 = new JSONObject(obj1.getString("user_desp_dict"));
                    JSONArray ar1 =  new JSONArray(obj1.getString("followers"));


                    fname = obj2.getString("fname");
                    lname = obj2.getString("lname");
                    uname = obj1.getString("username");
                    email = obj1.getString("email");
                    sex = obj2.getString("sex");
                    dob = obj2.getString("dob");

                    //age = obj2.getString("age");
//                    year = jsonObject.getString("status");
//                    month = jsonObject.getString("status");
//                    day = jsonObject.getString("status");
                    coun = obj2.getString("country");
                    desc = obj2.getString("descp");
                    fers = obj1.getString("fer_count");
                    fing = obj1.getString("fing_count");


                    setViews();

//                    EditText ed = (EditText) findViewById(R.id.editText9);
//                    ed.setText(response);

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


}
