package com.forher.forher;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by root on 11/1/16.
 */
public class VolleySingletonCall extends Application {

    private static VolleySingletonCall volleyInstance=null;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private static VolleySingletonCall mInstance;
    private VolleySingletonCall(){

        mRequestQueue= Volley.newRequestQueue(MyApplication.getAppContext());
    }

    public  static VolleySingletonCall getVolleyInstance(){
        if(volleyInstance==null)
        {
            volleyInstance=new VolleySingletonCall();
        }
        return volleyInstance;
    }

    public  RequestQueue getRequestQueue()
    {
        return mRequestQueue;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance=this;
    }


}


