package com.example.prakhargupta.myapplication5;

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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.prakhargupta.myapplication5.R;
import com.example.prakhargupta.myapplication5.app.AppController;
import com.ns.developer.tagview.entity.Tag;
import com.ns.developer.tagview.widget.TagCloudLinkView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.widget.Toast.LENGTH_SHORT;

public class Post_Add extends AppCompatActivity {

    CheckBox c1,c2;
    private String post_type,title,content,userid;
    private String[] tagsData = new String[50];
    private String[] selectedTags = new String[50];
    private int tagCount;
    private EditText ed1,ed2;
    private AutoCompleteTextView actv1;
    private TagCloudLinkView tagView;
    private ImageButton ib1;
    private Button b1;

    private String URL_FEED = "http://gameworld.pythonanywhere.com/";
    private String temp = URL_FEED;
    private ProgressDialog pDialog;
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_add);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        for (int j = 0; j < 50; j++)
        {
            tagsData[j] = "";
            selectedTags[j] = "";
        }
        tagCount = 0;

        c1 = (CheckBox) findViewById(R.id.checkBox);
        c2 = (CheckBox) findViewById(R.id.checkBox2);
        ed1 = (EditText) findViewById(R.id.newTitle);
        ed2 = (EditText) findViewById(R.id.newContent);
        actv1 = (AutoCompleteTextView) findViewById(R.id.autoTags);
        tagView = (TagCloudLinkView) findViewById(R.id.tagView);
        ib1 = (ImageButton) findViewById(R.id.addTag);
        b1 = (Button) findViewById(R.id.addPost);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        SharedPreferences sp = getSharedPreferences("data", MODE_PRIVATE);
        final SharedPreferences.Editor ed = sp.edit();
        userid = sp.getString("user_id", "N/A");

        URL_FEED = URL_FEED + "tags";
        System.out.println("0");
        getautoData();
        URL_FEED = temp;

        c1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (c1.isChecked() == true) {
                    c2.setChecked(false);
                }
            }
        });

        c2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (c2.isChecked() == true) {
                    c1.setChecked(false);
                }
            }
        });


        ib1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newTag = actv1.getText().toString();
//                System.out.println(newTag);
                tagView.add(new Tag(tagCount, newTag));
                tagView.drawTags();

                actv1.setText("");
                tagCount++;
            }
        });

        tagView.setOnTagDeleteListener(new TagCloudLinkView.OnTagDeleteListener() {
            @Override
            public void onTagDeleted(Tag tag, int i) {
                System.out.println(tag.getText());
                tagCount--;
            }
        });


        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (c1.isChecked())
                    post_type = "QS";
                else
                    post_type = "AR";

                title = ed1.getText().toString();
                content = ed2.getText().toString();

                if (title.equals("")) {
                    Toast.makeText(getApplication(), "Title cannot be empty :/", LENGTH_SHORT).show();
                    return;
                } else if (content.equals("")) {
                    Toast.makeText(getApplication(), "Content cannot be empty :/", LENGTH_SHORT).show();
                    return;
                }

//                List<Tag> t1 = tagView.getTags();
//                ArrayList<String> list = new ArrayList<String>();
//
//                for (int j = 0; j < tagCount; j++)
//                {
//                    String str = t1.get(j).getText();
////                    System.out.println(str);
//                    selectedTags[j] = str;
////                    System.out.println(selectedTags[j]);
//                    list.add(str);
//                }
//
//                JSONArray arr1 = new JSONArray(list);
//                System.out.println(arr1.toString());

                URL_FEED = URL_FEED + "add_post_app" + userid;
                makeJsonObjectRequest();
                URL_FEED = temp;
//                System.out.println("1");
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

//        System.out.println("2");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, tagsData);
        actv1.setThreshold(1);
        actv1.setAdapter(adapter);
//        System.out.println("3");
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
                    JSONArray tags = new JSONArray(obj1.getString("tags_name"));
                    int len = tags.length();

                    for (int i = 0; i < len; i++)
                    {
                        tagsData[i] = tags.get(i).toString();
                    }

//                    System.out.println("1");
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


    private void makeJsonObjectRequest()
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
                    String status = obj1.getString("status");

                    if (status.equals("success"))
                    {
                        Toast.makeText(getApplication(),"Post successfully added :D",Toast.LENGTH_SHORT).show();
//                        intent = new Intent(Post_Add.this,MainActivity.class);
//                        startActivity(intent);
                        finish();

                    }else if (status.equals("error"))
                    {
                        Toast.makeText(getApplication(),"Sorry, there was an error :/",Toast.LENGTH_SHORT).show();
                    }else
                    {
                        Toast.makeText(getApplication(),"wtf server",Toast.LENGTH_SHORT).show();
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
                params.put("type",post_type);
                params.put("title",title);
                params.put("content",content);

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
