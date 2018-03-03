package com.forher.forher;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by root on 21/1/16.
 */
public class SplashActivity extends AppCompatActivity {
    public static String FILENAME="ForHerInfo";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CheckNetworkConnection cn=new CheckNetworkConnection(getApplicationContext());
        boolean isNetOn=cn.isConnectedToNet();
        System.out.println("Net connection status" + isNetOn);
        if(isNetOn==false){
            //Snackbar.make(findViewById(R.id.), "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        Toast.makeText(getBaseContext(), "Unable to connect to Internet... Please try again later", Toast.LENGTH_LONG).show();

        }
        else
        {
            SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(FILENAME,MODE_PRIVATE);
            try{
                 String username= sharedPref.getString("USERNAME", null);
                if(username==null)
                {
                    Intent intent = new Intent(this, WelcomeActivity.class);
            startActivity(intent);
            finish();
                }
                else{
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }catch (Exception e)
            {
                e.printStackTrace();
            }

        }

    }

    public class CheckNetworkConnection {
        protected Context netContext;

        private CheckNetworkConnection(Context c) {
            this.netContext = c;
        }

        public boolean isConnectedToNet() {
            boolean bool = false;
            ConnectivityManager conMgr = (ConnectivityManager) netContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (conMgr != null) {
                NetworkInfo netInfo[] = conMgr.getAllNetworkInfo();
                if (netInfo != null) {
                    for (int i = 0; i < netInfo.length; i++) {
                        if (netInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                            bool = true;
                        }
                    }
                }

            } else {
                bool = false;
            }
            System.out.println("Connection state of app is " + bool);
            return bool;

        }
    }
}
