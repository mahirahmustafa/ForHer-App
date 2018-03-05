package com.forher.forher;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.util.LruCache;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.solovyev.android.views.llm.DividerItemDecoration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import dmax.dialog.SpotsDialog;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentOne extends Fragment {
    public int[] imageIcon={R.drawable.women1,R.drawable.women2};
    private static final int NUM_PAGES = 5;
    private cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager mPager;
    private PagerAdapter mPagerAdapter;
    JSONArray mainSlider=null,upceArray=null,recevArray=null;
    String[] mDataSet={"Loading...","Loading..."};
    public SpotsDialog cardProgressDialog;
    Vector<Slider> sliderObj=new Vector<Slider>();
    boolean isVolleyProcessed=false;
    String imageStr=null,linkStr=null;
    public static String FILENAME="ForHerInfo";
    private RecyclerView upceRecyclerView;
    private RecyclerView.Adapter upceViewAdapter;
    private RecyclerView.LayoutManager upceViewLayoutManager;
    UpceDataset upceDataset=new UpceDataset("Loading...","Loading...");
    Vector<UpceDataset> upceVector=new Vector<UpceDataset>();
    //initialization for recent events
    private RecyclerView recevRecyclerView;
    private RecyclerView.Adapter recevViewAdapter;
    private RecyclerView.LayoutManager recevViewLayoutManager;
    RecevDataset recevDataset=new RecevDataset("Loading...","Loading...");
    Vector<RecevDataset> recevVector=new Vector<RecevDataset>();
    //end of initialization
    TextView HInHerIcon,EInHerIcon,RInHerIcon,HText,EText,RText;
    //public ProgressDialog cardProgressDialog;
    public FragmentOne() {
        // Required empty public constructor
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        HInHerIcon=(TextView)getView().findViewById(R.id.her_info_card_text);
        HInHerIcon.setTypeface(FontManager.getTypeface(getActivity(),FontManager.FONTAWESOME));
        EInHerIcon=(TextView)getView().findViewById(R.id.her_info_card_text_e);
        EInHerIcon.setTypeface(FontManager.getTypeface(getActivity(),FontManager.FONTAWESOME));
        RInHerIcon=(TextView)getView().findViewById(R.id.her_info_card_text_r);
        RInHerIcon.setTypeface(FontManager.getTypeface(getActivity(),FontManager.FONTAWESOME));
        HText=(TextView)getView().findViewById(R.id.string_h_in_her);
        EText=(TextView)getView().findViewById(R.id.string_e_in_her);
        RText=(TextView)getView().findViewById(R.id.string_r_in_her);
        upceVector.add(upceDataset);
        mPagerAdapter = new ScreenSlidePagerAdapter(getChildFragmentManager());
        mPager = (cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager) getView().findViewById(R.id.main_page_slider);

        mPager.setAdapter(mPagerAdapter);
        mPager.startAutoScroll();
        mPager.setInterval(4000);
        mPager.setCycle(false);

        upceRecyclerView=(RecyclerView) getView().findViewById(R.id.upce_recycler_view);
        //upceViewLayoutManager=new LinearLayoutManager(getActivity());
        upceViewLayoutManager=new org.solovyev.android.views.llm.LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        upceRecyclerView.setLayoutManager(upceViewLayoutManager);
        upceViewAdapter=new UpceAdapter(upceVector);
        upceRecyclerView.setAdapter(upceViewAdapter);
        upceRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), null));
        //upceRecyclerView.setHasFixedSize(true);
        //code to add adapter and layout manager to recent events recyclerview
        recevVector.add(recevDataset);
        recevRecyclerView=(RecyclerView) getView().findViewById(R.id.recev_recycler_view);
        //recevViewLayoutManager=new LinearLayoutManager(getActivity());
        recevViewLayoutManager=new org.solovyev.android.views.llm.LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recevRecyclerView.setLayoutManager(recevViewLayoutManager);
        recevViewAdapter=new RecevAdapter(recevVector);
        recevRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),null));
        recevRecyclerView.setAdapter(recevViewAdapter);
        //recevRecyclerView.setHasFixedSize(true);
        //end of code
        cardProgressDialog=new SpotsDialog(getActivity(),R.style.Custom);

        cardProgressDialog.show();
        //cardProgressDialog=ProgressDialog.show(getActivity(),"Loading","Values loading",true,false);
        sliderObj=getDataFromServer();

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v= inflater.inflate(R.layout.fragment_fragment_one, container, false);

        return v;


    }

    public  class  ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager supportFragmentManager) {
            super(supportFragmentManager);

        }

        @Override
        public Fragment getItem(int position) {
            if(position>2)
            {
                position=position%3;
            }
            if(isVolleyProcessed==false)
            {
                imageStr=null;
                linkStr=null;
            }
            else
            {

                System.out.println("imageStr value s "+imageStr+"linkStr val"+linkStr);
            }
            return  MainPageSliderFragment.init(position,isVolleyProcessed,imageStr,linkStr);
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }


    }
    /*private class ScreenSlidePagerAdapter extends FragmentPagerAdapter {


        public List<Fragment> imageIcon=new ArrayList<>();

        public ScreenSlidePagerAdapter(FragmentManager manager){
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return imageIcon.get(position);
        }

        public void addFragment(Fragment)
        @Override
        public int getCount() {
            return 2;
        }
    }*/

    class Slider{
        public String title;
        public String imgSrc;
        public String desc;
        public String link;
        Slider(String title,String imgSrc,String desc,String link){
            this.title=title;
            this.imgSrc=imgSrc;
            this.desc=desc;
            this.link=link;
        }
    }


    public Vector<Slider> getDataFromServer(){
        System.out.println("getDataFromServer called");
        final RequestFuture<JSONObject> futureRequest = RequestFuture.newFuture();
        RequestQueue requestQueue=VolleySingletonCall.getVolleyInstance().getRequestQueue();

        StringRequest stringRequest=new StringRequest(Request.Method.GET, getString(R.string.ip)+"/api/v1.0/home", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                System.out.println("getDataFromServer:Response recieved");
                Toast.makeText(getActivity(), "response recieved", Toast.LENGTH_SHORT).show();
                try
                {

                    if(response !=null)
                    {
                        JSONObject jsonObj=new JSONObject(response);
                        mainSlider=jsonObj.getJSONArray("slider");
                        upceArray=jsonObj.getJSONArray("upc_events");
                        recevArray=jsonObj.getJSONArray("cmp_events");
                        mDataSet=new String[mainSlider.length()];
                        for(int i=0;i<mainSlider.length();i++)
                        {
                            JSONObject c=mainSlider.getJSONObject(i);
                            String sliderDesc=c.getString("desc");
                            String imgsrc=c.getString("img");
                            String title=c.getString("title");
                            String link=c.getString("link");
                            System.out.println("name is :"+sliderDesc);
                            mDataSet[i]=sliderDesc;
                            sliderObj.add(new Slider(title, imgsrc, sliderDesc,link));

                        }
                        upceVector.clear();
                        for(int i=0;i<upceArray.length();i++)
                        {
                            JSONObject c=upceArray.getJSONObject(i);
                            String imgsrc=c.getString("link");
                            String titlesrc=c.getString("title");
                            upceDataset=new UpceDataset(imgsrc,titlesrc);
                            upceVector.add(upceDataset);
                        }

                        recevVector.clear();
                        for(int i=0;i<recevArray.length();i++)
                        {
                            JSONObject c=recevArray.getJSONObject(i);
                            String imgsrc=c.getString("link");
                            String titlesrc=c.getString("title");
                            recevDataset=new RecevDataset(imgsrc,titlesrc);
                            recevVector.add(recevDataset);
                        }

                        System.out.println("Slider size is "+sliderObj.size());



                        SharedPreferences pref = getActivity().getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        for(int i=0;i<sliderObj.size();i++){
                            Slider obj=sliderObj.elementAt(i);
                            String linkval=obj.link;
                            System.out.println(obj.imgSrc+"---"+obj.title+"---"+obj.desc);
                            String attr="SLIDERTITLE"+i;
                            String linkattr="LINK"+i;
                            String value=obj.title;
                            editor.putString(attr, value);
                            editor.putString(linkattr,linkval);

                        }

                        editor.commit();
                        System.out.println(pref.getString("SLIDERTITLE0", null));


                        isVolleyProcessed=true;
                        mPagerAdapter.notifyDataSetChanged();
                        upceViewAdapter.notifyDataSetChanged();
                        recevViewAdapter.notifyDataSetChanged();
                        cardProgressDialog.dismiss();
                    }
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }

            }
        },new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),"response error",Toast.LENGTH_SHORT).show();
                cardProgressDialog.dismiss();
                System.out.println("error is: "+error);

            }
        });
        requestQueue.add(stringRequest);
        return sliderObj;
    }



public class UpceAdapter extends RecyclerView.Adapter<UpceAdapter.ViewHolder>{
    public Vector<UpceDataset> upceVector=new Vector<UpceDataset>();
    public UpceAdapter(Vector<UpceDataset> upceVector) {
        this.upceVector=upceVector;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.upc_event_row,parent,false);
            ViewHolder vh=new ViewHolder( v);
            return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //holder.imageLoc.setText(upceVector.elementAt(position).image);
        holder.imageLoc.setTypeface(FontManager.getTypeface(getActivity(), FontManager.FONTAWESOME));
        holder.titleLoc.setText(upceVector.elementAt(position).upceHeader);
    }



    @Override
    public int getItemCount() {
        return upceVector.size();
    }

    @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView imageLoc;
        public TextView titleLoc;

        public ViewHolder(View itemView) {
            super(itemView);
            imageLoc=(TextView)itemView.findViewById(R.id.image_str);
            titleLoc=(TextView)itemView.findViewById(R.id.title_str);
        }
    }
}



    public class RecevAdapter extends RecyclerView.Adapter<RecevAdapter.ViewHolder>{
        public Vector<RecevDataset> recevVector=new Vector<RecevDataset>();
        public RecevAdapter(Vector<RecevDataset> recevVector) {
            this.recevVector=recevVector;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_event_row,parent,false);
            ViewHolder vh=new ViewHolder( v);
            return vh;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            //holder.imageLoc.setText(recevVector.elementAt(position).image);
            holder.imageLoc.setTypeface(FontManager.getTypeface(getActivity(), FontManager.FONTAWESOME));
            holder.titleLoc.setText(recevVector.elementAt(position).upceHeader);
        }



        @Override
        public int getItemCount() {
            return recevVector.size();
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }


        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView imageLoc;
            public TextView titleLoc;

            public ViewHolder(View itemView) {
                super(itemView);
                imageLoc=(TextView)itemView.findViewById(R.id.rec_image_str);

                titleLoc=(TextView)itemView.findViewById(R.id.rec_title_str);
            }
        }
    }


    class UpceDataset{
        String image;
        String upceHeader;
        UpceDataset(String image,String upceHeader){
            this.image=image;
            this.upceHeader=upceHeader;
        }
    }

    class RecevDataset{
        String image;
        String upceHeader;
        RecevDataset(String image,String upceHeader){
            this.image=image;
            this.upceHeader=upceHeader;
        }
    }

}


