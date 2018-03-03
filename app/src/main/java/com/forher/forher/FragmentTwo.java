package com.forher.forher;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
//import android.app.Fragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.Vector;

import dmax.dialog.SpotsDialog;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentTwo extends Fragment {
     //initialization for recent events
    private RecyclerView evtRecyclerView;
    private RecyclerView.Adapter evtViewAdapter;
    private RecyclerView.LayoutManager evtViewLayoutManager;
    EvtDataset evtDataset=new EvtDataset("Loading...","Loading...","Loading...","Loading...","Loading...");
    JSONArray evtArray=null;
    Vector<EvtDataset> evtVector=new Vector<EvtDataset>();
    public SpotsDialog cardProgressDialog;
    //end of initialization

    public FragmentTwo() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        System.out.println("Inside onCreateView:Events");
        return inflater.inflate(R.layout.fragment_fragment_two, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
      super.onActivityCreated(savedInstanceState);
        evtVector.add(evtDataset);
        evtRecyclerView=(RecyclerView) getView().findViewById(R.id.events_recycler_view);
        evtViewLayoutManager=new LinearLayoutManager(getActivity());
        evtRecyclerView.setLayoutManager(evtViewLayoutManager);
        evtViewAdapter=new EvtAdapter(evtVector);
        evtRecyclerView.setAdapter(evtViewAdapter);

        getDataFromServer();
        System.out.println("Inside onActivutyCreated:Events");
        //cardProgressDialog= ProgressDialog.show(getActivity(), "Loading", "Values loading", true, false);
        cardProgressDialog=new SpotsDialog(getActivity(),R.style.Custom);
        cardProgressDialog.show();
    }

    class EvtDataset{
        String image;
        String upceHeader;
        String featuredImg;
        String startDate;
        String endDate;
        EvtDataset(String image,String upceHeader,String featuredImg,String startDate,String endDate){
            this.image=image;
            this.upceHeader=upceHeader;
            this.featuredImg=featuredImg;
            this.startDate=startDate;
            this.endDate=endDate;
        }
    }

    public class EvtAdapter extends RecyclerView.Adapter<EvtAdapter.ViewHolder>{
        public Vector<EvtDataset> evtVector=new Vector<EvtDataset>();
        public EvtAdapter(Vector<EvtDataset> evtVector) {
            this.evtVector=evtVector;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.eventpage_card_row,parent,false);
            ViewHolder vh=new ViewHolder( v);
            return vh;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            holder.imageLoc.setText(evtVector.elementAt(position).image);
            //holder.imageLoc.setTypeface(FontManager.getTypeface(getActivity(),FontManager.FONTAWESOME));
            //String htmlbody=evtVector.elementAt(position).upceHeader.toString();
            //holder.titleLoc.loadData(htmlbody,"text/html", "UTF-8");

            String htmlbody=evtVector.elementAt(position).upceHeader.toString();
            System.out.println("location " + htmlbody);
            holder.titleLoc.setText(evtVector.elementAt(position).upceHeader);
            holder.startDateTv.setText(evtVector.elementAt(position).startDate);
            //holder.endDateTv.setText(evtVector.elementAt(position).endDate);
            //holder.titleLoc.loadData(htmlbody, "text/html", "UTF-8");
            String x=evtVector.elementAt(position).featuredImg.toString();
            try{
                Picasso.with(getActivity()).load("http://52.11.116.39/"+x).into((ImageView) holder.featuredImgView);
                holder.featuredImgView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        System.out.println("EventCardNo:"+position+"clicked");
                        //Intent i = new Intent(getActivity(), EventDetails.class);
                        Intent i = new Intent(getActivity(), EventCardExpand.class);
                        i.putExtra("KEY",position);
                        i.putExtra("FROM","EVENTS");
                startActivity(i);
                    }
                });
            }
            catch(Exception e)
            {
                holder.featuredImgView.setImageResource(R.drawable.defimg);
            }

        }



        @Override
        public int getItemCount() {
            return evtVector.size();
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }


        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView imageLoc;
            public TextView titleLoc;
            public ImageView featuredImgView;
            public TextView startDateTv;
            //public TextView endDateTv;
            public ViewHolder(View itemView) {
                super(itemView);
                imageLoc=(TextView)itemView.findViewById(R.id.event_cardrow_title);

                titleLoc=(TextView)itemView.findViewById(R.id.event_cardrow_body);
                featuredImgView=(ImageView)itemView.findViewById(R.id.event_feat_img);
                startDateTv=(TextView)itemView.findViewById(R.id.evt_start_date);
                //endDateTv=(TextView)itemView.findViewById(R.id.evt_end_date);
            }
        }
    }

    public void getDataFromServer(){
        System.out.println("getDataFromServer called");

        RequestQueue requestQueue=VolleySingletonCall.getVolleyInstance().getRequestQueue();

        StringRequest stringRequest=new StringRequest(Request.Method.GET, "http://52.11.116.39/api/v1.0/events", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                System.out.println("Event:getDataFromServer:Response recieved");
                Toast.makeText(getActivity(), "response recieved", Toast.LENGTH_SHORT).show();
                try
                {

                    if(response !=null)
                    {
                        JSONObject jsonObj=new JSONObject(response);
                        evtArray=jsonObj.getJSONArray("posts");
                         evtVector.clear();
                        for(int i=0;i<evtArray.length();i++)
                        {
                            JSONObject c=evtArray.getJSONObject(i);
                            String loc=c.getString("location");
                            String title=c.getString("title");
                            String stDate=c.getString("start_date");
                            String endDate=c.getString("end_date");
                            JSONObject tmp=c.getJSONObject("images");
                            String imgloc=tmp.getString("featured");
                            System.out.println("body is :"+loc);
                           evtDataset=new EvtDataset(title,loc,imgloc,stDate,endDate);
                            evtVector.add(evtDataset);


                        }


                        evtViewAdapter.notifyDataSetChanged();
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

    }
}
