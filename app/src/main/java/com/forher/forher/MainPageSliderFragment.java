package com.forher.forher;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
//import android.app.Fragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.Vector;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


/**
 * A simple {@link Fragment} subclass.
 */
public  class MainPageSliderFragment extends Fragment {
    int pos;
    String imageStr=null,linkStr=null;
    public static String FILENAME="ForHerInfo";

    int counter=0;

     private RequestQueue mQueue;
    public static final String REQUEST_TAG = "VolleyBlockingRequestActivity";
    public MainPageSliderFragment() {
        // Required empty public constructor
    }

    public static MainPageSliderFragment init(int position,boolean isVolleyProcessed,String imageStr,String linkStr){
        MainPageSliderFragment fragmentSlider=new MainPageSliderFragment();
        Bundle args=new Bundle();
        args.putInt("position",position);
        args.putString("imageStr", imageStr);
        args.putString("linkStr",linkStr);
        fragmentSlider.setArguments(args);
        return fragmentSlider;
    }
    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        pos=getArguments()!=null?getArguments().getInt("position"):1;
        imageStr=getArguments()!=null?getArguments().getString("imageStr"):null;
        linkStr=getArguments()!=null?getArguments().getString("linkStr"):null;
        System.out.println("position is "+pos+"image esource "+imageStr);




    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        System.out.println("inside onCreateView: pos "+pos);

        View layoutView=inflater.inflate(R.layout.fragment_main_page_slider, container, false);
        View tv=layoutView.findViewById(R.id.image_slider);
        View textview=layoutView.findViewById(R.id.image_slider_title);
        final SharedPreferences pref = getActivity().getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Image "+pos+"clicked");
                String link=pref.getString("LINK"+pos,null);
                Intent i = new Intent(getActivity(), EventCardExpand.class);
                        i.putExtra("KEY",pos);
                i.putExtra("FROM","SLIDER");
                startActivity(i);
            }
        });
       // ImageLoader imageLoader = VolleySingletonCall.getVolleyInstance().getImageLoader();

        String slidertitle=null;
            if(pos==1)
            {
               Picasso.with(getActivity()).load("http://34.216.167.184:8088/assets/images/slider-2.jpg").into((ImageView) tv);
                slidertitle=pref.getString("SLIDERTITLE1", null);
                ((TextView) textview).setText(slidertitle);

            }
            else if(pos==2)
            {

               Picasso.with(getActivity()).load("http://34.216.167.184:8088/assets/images/slider-3.jpg").into((ImageView) tv);
                slidertitle=pref.getString("SLIDERTITLE2", null);
                ((TextView) textview).setText(slidertitle);
            }
        else{
                Picasso.with(getActivity()).load("http://34.216.167.184:8088/assets/images/slider-1.jpg").into((ImageView) tv);
                slidertitle=pref.getString("SLIDERTITLE0", null);
                ((TextView) textview).setText(slidertitle);
            }
        System.out.println("Slidertitle "+slidertitle);
        //((TextView) textview).setText(slidertitle);
       /* if(pos==1)
        {
            //((ImageView) tv).setImageResource(R.drawable.women1);
            Picasso.with(getActivity()).load("http://i.imgur.com/DvpvklR.png").into((ImageView) tv);
        }
        else
        {
            ((ImageView)tv).setImageResource(R.drawable.women2);
        }*/
        return layoutView;
    }




    private class CallingServerData extends AsyncTask<Void, Void, JSONObject>{
        private Context mContext;

        public CallingServerData(Context ctx)
        {
            mContext=ctx;
        }
        @Override
        protected JSONObject doInBackground(Void... params) {
            final RequestFuture<JSONObject> futureRequest = RequestFuture.newFuture();
            mQueue = VolleySingletonCall.getVolleyInstance().getRequestQueue();
            String url = getString(R.string.ip)+"/api/home";
            final JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method
                    .GET, url,
                    new JSONObject(), futureRequest, futureRequest);
            jsonRequest.setTag(REQUEST_TAG);
            mQueue.add(jsonRequest);
             try {
                return futureRequest.get(10, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}

