package com.example.prakhargupta.myapplication5;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.prakhargupta.myapplication5.Tabs.Login_Page;
import com.example.prakhargupta.myapplication5.app.AppController;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static android.widget.Toast.LENGTH_SHORT;

/**
 * Created by Pewds on 11-Oct-15.
 */
public class Sign_Up_Page extends Activity {

    EditText ed1,ed2,ed3,ed4,ed5,ed6,ed7,ed8;
    String fname,lname,uname,email,pass,cpass,sex,dob,coun,desc;
    //String sex,coun;
    Spinner spin1;
    int pstatus = 0;
    RadioButton btn1;
    Button b1,b2;

    int year_x,month_x,day_x;
    static final int DIALOG_ID = 0;
    final Calendar cal = Calendar.getInstance();

    private String urlJsonObj = "http://gameworld.pythonanywhere.com/signup";
    private ProgressDialog pDialog;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_page);
        b1 = (Button) findViewById(R.id.button1);
        b2 = (Button) findViewById(R.id.button2);

        ed1 = (EditText) findViewById(R.id.editText1);
        ed2 = (EditText) findViewById(R.id.editText2);
        ed3 = (EditText) findViewById(R.id.editText3);
        ed4 = (EditText) findViewById(R.id.editText4);
        ed5 = (EditText) findViewById(R.id.editText5);
        ed6 = (EditText) findViewById(R.id.editText6);
        btn1 = (RadioButton) findViewById(R.id.radioButton);
        ed7 = (EditText) findViewById(R.id.editText7);
        spin1 = (Spinner) findViewById(R.id.spinner1);
        ed8 = (EditText) findViewById(R.id.editText8);

        year_x = cal.get(Calendar.YEAR);
        month_x = cal.get(Calendar.MONTH);
        day_x = cal.get(Calendar.DAY_OF_MONTH);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);


        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                assignValues();

                checkPassword();

                if(pstatus == 2) {
                    Toast.makeText(getApplication(),"Password cannot be blank :/",Toast.LENGTH_LONG).show();
                    return;
                }else if (pstatus == 0)
                {
                    Toast.makeText(getApplication(), "Passwords do not match :/", Toast.LENGTH_LONG).show();
                    return;
                }else if (pstatus == 1)
                {
                    makeJsonObjectRequest();
                }
            }
        });

        b2.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDialog(DIALOG_ID);
                    }
                }
        );
    }

    private void assignValues()
    {
        fname = ed1.getText().toString();
        lname = ed2.getText().toString();
        uname = ed3.getText().toString();
        email = ed4.getText().toString();
        pass = ed5.getText().toString();
        cpass = ed6.getText().toString();
        checkSex();
        dob = ed7.getText().toString();
        coun = spin1.getSelectedItem().toString();
        desc = ed8.getText().toString();
    }

    private void checkSex()
    {
        if(btn1.isChecked() == true)
            sex = "M";
        else
            sex = "F";
    }

    private void checkPassword()
    {
        pstatus = 0;
        if (pass.equals(""))
        {
            pstatus = 2;
        }
        else if (pass != null && cpass != null)
        {
            if (pass.equals(cpass))
            {
                pstatus = 1;
            }
        }

//        Toast.makeText(getApplication(),"pass:" + pass + ",pstatus: " + pstatus,Toast.LENGTH_LONG).show();
    }

    @Override
    protected Dialog onCreateDialog(int id)
    {
        if(id == DIALOG_ID)
            return new DatePickerDialog(this, dpickerListener, year_x, month_x, day_x);
        return null;
    }

    private DatePickerDialog.OnDateSetListener dpickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            year_x = year;
            month_x = monthOfYear + 1;
            day_x = dayOfMonth;
            ed7.setText(year_x + "-" + month_x + "-" + day_x);
        }
    };

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private void makeJsonObjectRequest()
    {
//        Toast.makeText(getApplication(),fname + "," + lname + "," + uname + "," + email + "," + pass + "," + cpass + "," +
//                sex + "," + dob + "," + coun + "," + desc,Toast.LENGTH_LONG).show();

        showpDialog();

        StringRequest sr = new StringRequest(Request.Method.POST, urlJsonObj, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try{
                    JSONObject jsonObject = new JSONObject(response);

                    String status = jsonObject.getString("status");

                    if (status.equals("success"))
                    {
                        Toast.makeText(getApplication(),"Account Successfully Created :D",Toast.LENGTH_SHORT).show();
                        intent = new Intent(Sign_Up_Page.this,Login_Page.class);
                        startActivity(intent);
                    }else if (status.equals("error"))
                    {
                        Toast.makeText(getApplication(),"Email or Username already exists :/",Toast.LENGTH_SHORT).show();
                    }else
                    {
                        Toast.makeText(getApplication(),"wtf server",Toast.LENGTH_SHORT).show();
                    }

                }catch (Exception e)
                {
                    Toast.makeText(getApplication(), "Sign Up Failed :(", Toast.LENGTH_SHORT).show();
                    System.out.println(e);
                }

                hidepDialog();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplication(),"No internet connection",LENGTH_SHORT).show();
                System.out.println(error);
                hidepDialog();
            }
        }) {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("firstname",fname);
                params.put("lastname",lname);
                params.put("username",uname);
                params.put("email",email);
                params.put("pwd",pass);
//                params.put("day",Integer.toString(day_x));
//                params.put("month",Integer.toString(month_x));
//                params.put("year",Integer.toString(year_x));
                params.put("dob",dob);
                params.put("sex",sex);
                params.put("descp",desc);
                params.put("country",coun);

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
