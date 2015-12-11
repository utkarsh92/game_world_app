package com.example.prakhargupta.myapplication5.Tabs;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.prakhargupta.myapplication5.MainActivity;
import com.example.prakhargupta.myapplication5.R;
import com.example.prakhargupta.myapplication5.Sign_Up_Page;
import com.example.prakhargupta.myapplication5.app.AppController;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.widget.Toast.LENGTH_SHORT;

/**
 * Created by Prakhar Gupta on 30/11/2015.
 */
public class Tab4 extends Fragment {

    EditText ed1,ed2;
    Button b;
    TextView tv1;
    int loginStatus;
    private View rootView;
    Intent intent;

    private String urlJsonObj = "http://gameworld.pythonanywhere.com/";
    private ProgressDialog pDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.login_page, container, false);

        b= (Button) rootView.findViewById(R.id.button);
        SharedPreferences sp = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        final SharedPreferences.Editor ed = sp.edit();

        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ed1 = (EditText) rootView.findViewById(R.id.editText3);
                ed2 = (EditText) rootView.findViewById(R.id.editText4);

                ed.putString("email", ed1.getText().toString());
                ed.putString("password", ed2.getText().toString());
                ed.commit();

                makeJsonObjectRequest();
            }
        });

        tv1 = (TextView) rootView.findViewById(R.id.textView5);
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getContext(),Sign_Up_Page.class);
                startActivity(intent);
            }
        });


        return rootView;
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

        SharedPreferences sp = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        final SharedPreferences.Editor ed = sp.edit();

        StringRequest sr = new StringRequest(Request.Method.POST, urlJsonObj, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(getApplication(),"1",LENGTH_SHORT).show();
                try{
                    JSONObject jsonObject = new JSONObject(response);

                    String status = jsonObject.getString("status");
                    String userid = jsonObject.getString("user_id");
                    String uname = jsonObject.getString("username");

//                    ed.putString("user_id",userid);
//                    ed.commit();

                    if (status.equals("success"))
                    {
//                        Toast.makeText(getApplication(),"yay",Toast.LENGTH_SHORT).show();
                        intent = new Intent(getContext(),MainActivity.class);
                        //ed2.setText("");

                        ed.putString("username",uname);
                        ed.putString("user_id",userid);
                        ed.commit();

                        startActivity(intent);
                        getActivity().finish();

                    }else if (status.equals("error"))
                    {
                        Toast.makeText(getActivity(), "Invalid username or password", Toast.LENGTH_SHORT).show();
                    }else
                    {
                        Toast.makeText(getActivity(),"wtf server",Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e)
                {
                    Toast.makeText(getActivity(), "Login Failed :(", Toast.LENGTH_SHORT).show();
                }
                hidepDialog();
                //mPostCommentResponse.requestCompleted();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),"No internet connection",LENGTH_SHORT).show();
                //mPostCommentResponse.requestEndedWithError(error);
                hidepDialog();
            }
        }) {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("email",ed1.getText().toString());
                params.put("password",ed2.getText().toString());

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };

//        queue.add(sr);
//        StringRequest srr=sr;
//        Toast.makeText(getApp
// lication(),"4",Toast.LENGTH_LONG).show();

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(sr);
    }
}
