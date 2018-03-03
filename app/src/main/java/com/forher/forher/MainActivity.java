package com.forher.forher;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity  {
    private DrawerLayout profileDrawerLayout;
    private NavigationView ProfileDrawer;
    private RecyclerView profileDrawerRecyclerView;
    private RecyclerView.Adapter mainRecyclerViewAdapter;
    private RecyclerView.LayoutManager mainRecyclerViewLayoutMgr;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TextView profileicon;
    private static Context mContext;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=getApplicationContext();
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        profileDrawerLayout=(DrawerLayout)findViewById(R.id.main_drawer_layout);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        profileicon=(TextView)findViewById(R.id.profile_icon);
        profileicon.setTypeface(FontManager.getTypeface(this, FontManager.FONTAWESOME));
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        final NavDrawerFragment drawerFragment=(NavDrawerFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_nav_drawer_id);
        drawerFragment.setUp((DrawerLayout) findViewById(R.id.main_drawer_layout),toolbar);
        tabLayout = (TabLayout) findViewById(R.id.tabs);

        tabLayout.setupWithViewPager(viewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Link for donation... To be developed", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                Intent i = new Intent(getBaseContext(), EventCardExpand.class);
                i.putExtra("KEY",1);
                i.putExtra("FROM","FAB");
                startActivity(i);
            }
        });
        profileicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileDrawerLayout.openDrawer(findViewById(R.id.fragment_nav_drawer_id));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FragmentOne(), "HOME");
        adapter.addFragment(new FragmentTwo(), "EVENTS");
        adapter.addFragment(new FragmentFour(),"BLOGS");
        adapter.addFragment(new FragmentFounder(), "TEAM");
        viewPager.setAdapter(adapter);

    }



    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();
        private int[] imageResId = {
        R.drawable.ic_home,
        R.drawable.ic_pin_drop,
                R.drawable.ic_create,
        R.drawable.ic_timer_auto };

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Drawable image = ContextCompat.getDrawable(mContext, imageResId[position]);
            image.setBounds(0, 0, image.getIntrinsicWidth()+5, image.getIntrinsicHeight()+5);
            SpannableString sb = new SpannableString(" ");
            ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
            sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            //return mFragmentTitleList.get(position);
            return sb;
        }
    }

}
