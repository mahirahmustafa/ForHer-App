package com.forher.forher;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dmax.dialog.SpotsDialog;

import static android.app.PendingIntent.getActivity;


public class WelcomeActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private static int SPLASH_TIME_OUT = 3000;
    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "SignInActivity";
    private GoogleApiClient mGoogleApiClient;
    private ProgressDialog mProgressDialog;
    private LoginButton fBloginButton;
    private CallbackManager callBackManager;
    EditText usernameText,pwdText;
    Button loginButton;
    public SpotsDialog cardProgressDialog;
    public static String FILENAME="ForHerInfo";
    ImageView logoIcon;
    TextView signuplink,leftArrowText,rightArrowText;
        //Button loginButton;
    SharedPreferences sharedpreferences;
    @Override
    protected void  onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_welcome);
        View parentLayout=findViewById(R.id.welcome_rootid);
        logoIcon=(ImageView)findViewById(R.id.login_logo);
        usernameText=(EditText)findViewById(R.id.editText);
        usernameText.setHint(R.string.username_hint);
        pwdText=(EditText)findViewById(R.id.editText_pwd);
        pwdText.setHint(R.string.password_hint);
       // leftArrowText=(TextView)findViewById(R.id.left_arrow);
        //leftArrowText.setTypeface(FontManager.getTypeface(getBaseContext(),FontManager.FONTAWESOME));
        //rightArrowText=(TextView)findViewById(R.id.right_arrow);
        //rightArrowText.setTypeface(FontManager.getTypeface(getBaseContext(),FontManager.FONTAWESOME));
        signuplink=(TextView)findViewById(R.id.signup_link);
        signuplink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(),"Signin clicked",Toast.LENGTH_LONG).show();
                Intent i = new Intent(WelcomeActivity.this, SignupActivity.class);
                startActivity(i);
            }
        });
        loginButton=(Button)findViewById(R.id.custom_loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardProgressDialog=new SpotsDialog(WelcomeActivity.this,R.style.Custom);
                cardProgressDialog.show();
               final String cloginUsername=usernameText.getText().toString();
                final String cloginPwd=pwdText.getText().toString();
                System.out.println("username is "+cloginUsername+"password is "+cloginPwd);
                Pattern p = Pattern.compile(".+@.+\\.[a-z]+");
                Matcher m = p.matcher(cloginUsername);
                boolean matchFound = m.matches();
                if(matchFound && cloginPwd.length()>5)
                {
                    RequestQueue requestQueue=VolleySingletonCall.getVolleyInstance().getRequestQueue();
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://52.11.116.39/api/v1.0/signin",
                    new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject parsedResponse=new JSONObject(response);
                            String status=parsedResponse.getString("status");
                            if(status.equalsIgnoreCase("true"))
                            {
                                SharedPreferences pref = getApplicationContext().getSharedPreferences(FILENAME, MODE_PRIVATE);
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putString("USERNAME", cloginUsername);
                                    editor.putString("LOGINTYPE","custom");
                                    editor.commit();
                                Toast.makeText(getBaseContext(),"successfully logged in",Toast.LENGTH_LONG).show();
                                cardProgressDialog.dismiss();
                                System.out.println("Successful response" + response);
                                Intent i = new Intent(WelcomeActivity.this, MainActivity.class);
                                startActivity(i);
                            }
                            else
                            {
                                if(status.equalsIgnoreCase("false"))
                                {
                                    JSONArray notification=parsedResponse.getJSONArray("notifications");
                                    if(notification.get(0).toString().contains("Wrong"))
                                    {
                                        cardProgressDialog.dismiss();
                                        Toast.makeText(getBaseContext(),"Either username or password is wrong... Please try again",Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                        }
                        catch(Exception e)
                        {
                            cardProgressDialog.dismiss();
                            e.printStackTrace();
                            Toast.makeText(getBaseContext(),"Error occured while logging in",Toast.LENGTH_LONG).show();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        cardProgressDialog.dismiss();
                        Toast.makeText(getBaseContext(),"Error in logging in... Please check username and password ",Toast.LENGTH_LONG).show();
                    }
                }){
                 @Override
                    protected Map<String,String> getParams(){
                    Map<String,String> params = new HashMap<String, String>();
                    params.put("username",cloginUsername);
                    params.put("passwd",cloginPwd);

                    return params;
            }

        };


        requestQueue.add(stringRequest);
                }
                else if(cloginPwd.length()<5)
                {
                    Toast.makeText(getBaseContext(), "Please use a password of minimum 6 characters", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(getBaseContext(), "Incorrect username format... Please try again.", Toast.LENGTH_LONG).show();
                }

            }
        });
        //code to check for sharedpreferences

        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(FILENAME,MODE_PRIVATE);
        try{
          String username= sharedPref.getString("USERNAME", null);
            if(username==null)
            {
               // SharedPreferences.Editor editor = sharedPref.edit();
                //editor.putString("USERNAME", "TEST");
            }
            else
            {
                Intent i = new Intent(WelcomeActivity.this, MainActivity.class);
                startActivity(i);
            }

        }catch(Exception e)
        {
            e.printStackTrace();
        }

        //end of code

        //setContentView(R.layout.activity_welcome);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }); */
        try{
            PackageInfo info = getPackageManager().getPackageInfo("com.forher.forher", PackageManager.GET_SIGNATURES);
             for (Signature signature : info.signatures) {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(signature.toByteArray());
            Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
                 System.out.println("Keyhash is "+Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        callBackManager = CallbackManager.Factory.create();
        fBloginButton = (LoginButton)findViewById(R.id.fb_login_button);
        fBloginButton.setReadPermissions("email");
        fBloginButton.setReadPermissions("user_birthday");

        fBloginButton.registerCallback(callBackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                cardProgressDialog=new SpotsDialog(WelcomeActivity.this,R.style.Custom);
                cardProgressDialog.show();
                System.out.println("fb loginResult" + loginResult);

                View v=findViewById(R.id.fb_login_button);
                                fBloginButton.setVisibility(v.GONE);
                                usernameText.setVisibility(View.GONE);
                                pwdText.setVisibility(View.GONE);
                                loginButton.setVisibility(View.GONE);
                                signuplink.setVisibility(View.GONE);
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {
                                // Application code

                                Log.v("LoginActivity***FB", response.toString());
                                Log.v("LoginActivity***FB2", object.toString());
                                String username=null,dob=null,gender=null;
                                //Parse FB json here
                                try{
                                    JSONObject jsonObject=response.getJSONObject();
                                     username=jsonObject.getString("name");
                                     dob=jsonObject.getString("birthday");
                                     gender=jsonObject.getString("gender");
                                }catch(Exception e)
                                {
                                    e.printStackTrace();
                                }
                                SharedPreferences pref = getApplicationContext().getSharedPreferences(FILENAME, MODE_PRIVATE);
                                String uname=pref.getString("USERNAME", null);
                                if(uname==null)
                                {
                                    SharedPreferences.Editor editor = pref.edit();
                                    editor.putString("USERNAME", username);
                                    editor.putString("GENDER",gender);
                                    editor.putString("DOB",dob);
                                    editor.putString("LOGINTYPE","facebook");
                                    editor.commit();
                                }

                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender, birthday");
                request.setParameters(parameters);
                request.executeAsync();
                cardProgressDialog.dismiss();
                Intent i = new Intent(WelcomeActivity.this, MainActivity.class);
                startActivity(i);
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
        .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
        .build();
        SignInButton gsignInButton = (SignInButton) findViewById(R.id.google_sign_in_button);
        gsignInButton.setSize(SignInButton.SIZE_STANDARD);
        gsignInButton.setScopes(gso.getScopeArray());
        gsignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId()==R.id.google_sign_in_button)
                {
                   if(!mGoogleApiClient.isConnected())
                   {
                      // mGoogleApiClient.connect();
                       Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                        startActivityForResult(signInIntent, RC_SIGN_IN);

                       System.out.println("Signed in with google+");
                   }
                    else if(mGoogleApiClient.isConnected())
                   {
                       mGoogleApiClient.clearDefaultAccountAndReconnect();
                       System.out.println("Reconnection:Signed in with google+");
                   }
                }
            }
        });


       /* new Handler().postDelayed(new Runnable() {



            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(WelcomeActivity.this, MainActivity.class);
                startActivity(i);

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT); */



    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        System.out.println("Connection failed");
    }
    @Override
    public void onStart(){
        super.onStart();
       /* OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
         opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    //hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });*/
        /*if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        }
        else{
            //showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    //hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }*/
    }

     @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);

        }else{
            callBackManager.onActivityResult(requestCode, resultCode, data);
            updateUI(true);
        }

    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            String personName = acct.getDisplayName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();
            System.out.println("personName "+personName+"personEmail"+personEmail+"personId"+personId+"personPhoto"+personPhoto);
            SharedPreferences pref = getBaseContext().getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("USERNAME",personEmail);
            editor.putString("LOGINTYPE","google");
            editor.putString("PHOTO",personPhoto.toString());
            editor.commit();
            //Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);


            //mStatusTextView.setText(getString(R.string.signed_in_fmt, acct.getDisplayName()));
            updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
            updateUI(false);
        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        updateUI(false);
                        // [END_EXCLUDE]
                    }
                });
    }

    private void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        updateUI(false);
                        // [END_EXCLUDE]
                    }
                });
    }

   private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }


    private void updateUI(boolean signedIn) {

        if (signedIn) {
            findViewById(R.id.google_sign_in_button).setVisibility(View.GONE);
            View v=findViewById(R.id.fb_login_button);
                                fBloginButton.setVisibility(v.GONE);
                                fBloginButton.setVisibility(v.GONE);
                                usernameText.setVisibility(View.GONE);
                                pwdText.setVisibility(View.GONE);
                                loginButton.setVisibility(View.GONE);
                                signuplink.setVisibility(View.GONE);
            Intent i = new Intent(WelcomeActivity.this, MainActivity.class);
            startActivity(i);
            //findViewById(R.id.sign_out_and_disconnect).setVisibility(View.VISIBLE);
        } else {
            //mStatusTextView.setText(R.string.signed_out);

            findViewById(R.id.google_sign_in_button).setVisibility(View.VISIBLE);
            //findViewById(R.id.sign_out_and_disconnect).setVisibility(View.GONE);
        }
    }




}
