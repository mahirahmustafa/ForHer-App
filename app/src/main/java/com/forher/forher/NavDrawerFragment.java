package com.forher.forher;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
//import android.app.Fragment;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.Vector;


/**
 * A simple {@link Fragment} subclass.
 */
public class NavDrawerFragment extends Fragment {
    private ActionBarDrawerToggle mainActionBarDrawerToggle;
    private DrawerLayout mainDrawerLayout;
    private RecyclerView navRecyclerView;
    private RecyclerView.Adapter navViewAdapter;
    private RecyclerView.LayoutManager navViewLayoutManager;
    private GoogleApiClient mGoogleApiClient;
    NavDataset navDataset=new NavDataset("Loading...","Loading...");
    Vector<NavDataset> navVector=new Vector<NavDataset>();
    TextView nav_icon;
    public static String FILENAME="ForHerInfo";
    public NavDrawerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_nav_drawer, container, false);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        //navVector.add(navDataset);
    super.onActivityCreated(savedInstanceState);


        //navRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), null));

    }

    private void getDatasetValues() {
       navVector.add(new NavDataset("x","Profile"));
       navVector.add(new NavDataset("x","My Donations"));
        navVector.add(new NavDataset("x","OurTeam"));
       navVector.add(new NavDataset("x1","Logout"));
    }

    public void setUp(DrawerLayout drawerLayout,Toolbar toolbar) {
        mainDrawerLayout=drawerLayout;
        //nav_icon=(TextView)getView().findViewById(R.id.nav_row_icon);
        //nav_icon.setTypeface(FontManager.getTypeface(getActivity(),FontManager.FONTAWESOME));
        getDatasetValues();
        navRecyclerView=(RecyclerView) getView().findViewById(R.id.nav_recycler_view);
        navRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity()).build());
        //navViewLayoutManager=new org.solovyev.android.views.llm.LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        navViewLayoutManager=new LinearLayoutManager(getActivity());
        navRecyclerView.setLayoutManager(navViewLayoutManager);
        navViewAdapter=new NavAdapter(navVector);
        navRecyclerView.setAdapter(navViewAdapter);
        mainActionBarDrawerToggle=new ActionBarDrawerToggle(getActivity(),mainDrawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close){
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        mainDrawerLayout.setDrawerListener(mainActionBarDrawerToggle);

    }

    class NavDataset{
        String icon;
        String menu_name;
        NavDataset(String icon,String menu_name){
            this.icon=icon;
            this.menu_name=menu_name;
        }
    }

    public class NavAdapter extends RecyclerView.Adapter<NavAdapter.ViewHolder>{
        public Vector<NavDataset> navVector=new Vector<NavDataset>();
        public NavAdapter(Vector<NavDataset> navVector) {
            this.navVector=navVector;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_row,parent,false);
            ViewHolder vh=new ViewHolder( v);
            return vh;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            if(position==1)
            {
                holder.iconLoc.setText(R.string.icon_h_in_her);
                holder.iconLoc.setTypeface(FontManager.getTypeface(getActivity(),FontManager.FONTAWESOME));
            }
            else if(position==3)
            {
               holder.iconLoc.setText(R.string.logout);
                holder.iconLoc.setTypeface(FontManager.getTypeface(getActivity(),FontManager.FONTAWESOME));
            }
            else if(position==2){
               holder.iconLoc.setText(R.string.group_icon);
                holder.iconLoc.setTypeface(FontManager.getTypeface(getActivity(),FontManager.FONTAWESOME));
            }
            else
            {
                holder.iconLoc.setText(R.string.icon_e_in_her);
                holder.iconLoc.setTypeface(FontManager.getTypeface(getActivity(),FontManager.FONTAWESOME));
            }


            holder.nameLoc.setText(navVector.elementAt(position).menu_name);
            holder.nameLoc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(position==0)//Profile
                    {
                        Intent i = new Intent(getActivity(), EventCardExpand.class);
                        i.putExtra("KEY",position);
                        i.putExtra("FROM","PROFILE");
                        startActivity(i);
                    }
                    else if(position==1)
                    {
                        System.out.println("Position "+position);

                    }
                    else if(position==2){//OurTeam
                        Intent i = new Intent(getActivity(),TeamActivity.class);

                        startActivity(i);
                    }
                    else if(position==3)//Logout
                    {
                        SharedPreferences pref = getActivity().getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
                        String logintype=pref.getString("LOGINTYPE", null);
                        if(logintype.equalsIgnoreCase("facebook"))
                        {
                            LoginManager.getInstance().logOut();
                        }
                        else if(logintype.equalsIgnoreCase("google"))
                        {

                        }
                        SharedPreferences.Editor editor = pref.edit();

                        editor.remove("USERNAME");
                        editor.remove("GENDER");
                        editor.remove("DOB");
                        editor.remove("LOGINTYPE");
                        editor.commit();

                        Toast.makeText(getActivity(), "Logged Out successfully", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(getActivity(), SplashActivity.class);
                        startActivity(i);
                    }
                    else
                    {
                        System.out.println("Position:else "+position);
                    }
                }
            });
        }



        @Override
        public int getItemCount() {
            return navVector.size();
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }


        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView iconLoc;
            public TextView nameLoc;

            public ViewHolder(View itemView) {
                super(itemView);
                iconLoc=(TextView)itemView.findViewById(R.id.nav_row_icon);

                nameLoc=(TextView)itemView.findViewById(R.id.nav_row_name);
            }
        }
    }

}
