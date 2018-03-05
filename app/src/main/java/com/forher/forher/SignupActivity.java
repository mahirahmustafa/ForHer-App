package com.forher.forher;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {
    EditText editEmail,editUsername,editDob,editPwd,editChkPwd,editPhone;
    Button submitButton;
    RadioGroup sexRadioGroup;
    RadioButton getRadioBtn;
    public static String FILENAME="ForHerInfo";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sexRadioGroup=(RadioGroup)findViewById(R.id.signup_radiogrp);
        //maleRadioBtn=(RadioButton)findViewById(R.id.m_radiobtn);
        //femaleRadioButton=(RadioButton)findViewById(R.id.f_radiobtn);
        editEmail=(EditText)findViewById(R.id.signup_email);
        editUsername=(EditText)findViewById(R.id.signup_username);
        editDob=(EditText)findViewById(R.id.signup_dob);
        editPwd=(EditText)findViewById(R.id.signup_pwd);
        editChkPwd=(EditText)findViewById(R.id.signup_cnfrmpwd);
        editPhone=(EditText)findViewById(R.id.signup_phone);
        editUsername.setHint("Username");
        editEmail.setHint("abc@xyz.com");
        editDob.setHint("dd/mm/yyyy");
        editPwd.setHint("Password");
        editChkPwd.setHint("Re-enter Password");
        editPhone.setHint("9900990099");
        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }); */
        submitButton=(Button)findViewById(R.id.signup_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(),"Signup clicked", Toast.LENGTH_LONG).show();
                final String emailStr=editEmail.getText().toString();
                final String usernameStr=editUsername.getText().toString();
                final String dobStr=editDob.getText().toString();
                final String pwdStr=editPwd.getText().toString();
                final String chkPwdStr=editChkPwd.getText().toString();
                final String phoneStr=editPhone.getText().toString();
                int selectedGenderId=sexRadioGroup.getCheckedRadioButtonId();
                getRadioBtn=(RadioButton)findViewById(selectedGenderId);
                System.out.println("gender is "+getRadioBtn.getText().toString());

                if(pwdStr.equalsIgnoreCase(chkPwdStr) && pwdStr.length()>7)
                {
                    //send data toserver
                     RequestQueue requestQueue=VolleySingletonCall.getVolleyInstance().getRequestQueue();
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, getString(R.string.ip)+"/api/v1.0/signup",
                    new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject resp=new JSONObject(response);
                            String status=resp.getString("status");
                            {
                                if(status.equalsIgnoreCase("true"))
                                {
                                    Toast.makeText(getBaseContext(),"successfully logged in",Toast.LENGTH_LONG).show();
                                    System.out.println("Successful response" + response);
                                    //Write into sharedPreferences
                                    SharedPreferences pref = getApplicationContext().getSharedPreferences(FILENAME, MODE_PRIVATE);
                                    SharedPreferences.Editor editor = pref.edit();
                                    editor.putString("USERNAME", usernameStr);
                                    editor.putString("GENDER",getRadioBtn.getText().toString());
                                    editor.putString("DOB",dobStr);
                                    editor.commit();
                                    //end of entering data into sharedPreferences
                                    Intent i = new Intent(SignupActivity.this, MainActivity.class);
                                    startActivity(i);
                                }
                                else
                                {
                                  Toast.makeText(getBaseContext(),"Invalid credentials... Please try again",Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                        catch(Exception e)
                        {
                            e.printStackTrace();
                             Toast.makeText(getBaseContext(),"error getting result",Toast.LENGTH_LONG).show();
                        }



                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getBaseContext(),"Error signing up... Please check username and password ",Toast.LENGTH_LONG).show();
                    }
                }){
                 @Override
                    protected Map<String,String> getParams(){
                    Map<String,String> params = new HashMap<String, String>();
                    params.put("email",emailStr);
                    params.put("passwd",pwdStr);
                     params.put("name",usernameStr);
                     params.put("dob",dobStr);
                     params.put("phone",phoneStr);
                     params.put("sex",getRadioBtn.getText().toString());

                    return params;
            }

        };


        requestQueue.add(stringRequest);
                }
                else
                {
                   Toast.makeText(getBaseContext(),"Passwords donot match...",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}
