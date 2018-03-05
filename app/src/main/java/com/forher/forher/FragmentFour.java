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

import java.util.Vector;

import dmax.dialog.SpotsDialog;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentFour extends Fragment {
    private RecyclerView blogRecyclerView;
    private RecyclerView.Adapter blogViewAdapter;
    private RecyclerView.LayoutManager blogViewLayoutManager;
    BlogDataset blogDataset=new BlogDataset("Loading...","Loading...","Loading...");
    Vector<BlogDataset> blogVector=new Vector<BlogDataset>();
    JSONArray blogArray=null;
    public SpotsDialog cardProgressDialog;
    public FragmentFour() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragment_four, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        blogVector.add(blogDataset);
        blogRecyclerView=(RecyclerView) getView().findViewById(R.id.blog_recycler_view);
        blogViewLayoutManager=new LinearLayoutManager(getActivity());
        blogRecyclerView.setLayoutManager(blogViewLayoutManager);
        blogViewAdapter=new BlogAdapter(blogVector);
        blogRecyclerView.setAdapter(blogViewAdapter);
        getDataFromServer();
        cardProgressDialog=new SpotsDialog(getActivity(),R.style.Custom);
        cardProgressDialog.show();
        //cardProgressDialog= ProgressDialog.show(getActivity(), "Loading", "Values loading", true, false);
    }

    class BlogDataset{
        String image;
        String title;
        String author;
        BlogDataset(String title,String image,String author){
            this.image=image;
            this.title=title;
            this.author=author;
        }
    }

    public class BlogAdapter extends RecyclerView.Adapter<BlogAdapter.ViewHolder>{
        public Vector<BlogDataset> blogVector=new Vector<BlogDataset>();
        public BlogAdapter(Vector<BlogDataset> blogVector) {
            this.blogVector=blogVector;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.blogmain_card_row,parent,false);
            ViewHolder vh=new ViewHolder( v);
            return vh;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {

            //holder.imageLoc.setTypeface(FontManager.getTypeface(getActivity(),FontManager.FONTAWESOME));
            //String htmlbody=evtVector.elementAt(position).upceHeader.toString();
            //holder.titleLoc.loadData(htmlbody,"text/html", "UTF-8");
            holder.titleLoc.setText(blogVector.elementAt(position).title);
            holder.authorText.setText(blogVector.elementAt(position).author);
            String x=blogVector.elementAt(position).image.toString();
            try{
                Picasso.with(getActivity()).load(getString(R.string.ip)+x).into((ImageView) holder.featuredImgView);
                holder.featuredImgView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        System.out.println("EventCardNo:"+position+"clicked");
                        //Intent i = new Intent(getActivity(), EventDetails.class);
                        Intent i = new Intent(getActivity(), EventCardExpand.class);
                        i.putExtra("KEY",position);
                        i.putExtra("FROM","BLOG");
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
            return blogVector.size();
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }


        public class ViewHolder extends RecyclerView.ViewHolder {

            public TextView titleLoc;
            public ImageView featuredImgView;
            public TextView authorText;
            public ViewHolder(View itemView) {
                super(itemView);
                titleLoc=(TextView)itemView.findViewById(R.id.blog_cardrow_title);
                featuredImgView=(ImageView)itemView.findViewById(R.id.blog_feat_img);
                authorText=(TextView)itemView.findViewById(R.id.blog_author_text);
            }
        }
    }


    public void getDataFromServer(){
        System.out.println("getDataFromServer called");

        RequestQueue requestQueue=VolleySingletonCall.getVolleyInstance().getRequestQueue();

        StringRequest stringRequest=new StringRequest(Request.Method.GET, getString(R.string.ip)+"/api/v1.0/blog", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                System.out.println("Blogs:getDataFromServer:Response recieved");
                Toast.makeText(getActivity(), "response recieved", Toast.LENGTH_SHORT).show();
                try
                {

                    if(response !=null)
                    {
                        JSONObject jsonObj=new JSONObject(response);
                        blogArray=jsonObj.getJSONArray("posts");
                         blogVector.clear();
                        for(int i=0;i<blogArray.length();i++)
                        {
                            JSONObject c=blogArray.getJSONObject(i);
                            String title=c.getString("title");
                            JSONObject extractAuthor=c.getJSONObject("author");
                            String author=extractAuthor.getString("name");
                            JSONObject tmp=c.getJSONObject("images");
                            String imgloc=tmp.getString("featured");
                            System.out.println("image  is :"+imgloc+"author name ");
                           blogDataset=new BlogDataset(title,imgloc,author);
                            blogVector.add(blogDataset);


                        }


                        blogViewAdapter.notifyDataSetChanged();
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
