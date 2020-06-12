package com.tmstudios.grammymaster;

import android.os.*;
import android.view.*;
import android.support.v7.app.*;
import android.support.v4.view.*;
import android.support.design.widget.*;
import java.util.*;
import android.support.v4.app.*;
import android.support.v7.widget.*;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity 
{
	
	private Toolbar toolbar;

	private ViewPager viewPager;

	private TabLayout tabLayout;

	private ArrayList<Fragment> fragments;

	private HomeFragment viewSearch;

	private HomeFragment viewList;

	private HomeFragment viewYears;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
    super.onCreate(savedInstanceState);
    	initLayout();
		
    }
	public void initLayout(){
		setContentView(R.layout.actvity_main);
		toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.pager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        fragments =new ArrayList<Fragment>();

        fragments.add(viewList = new HomeFragment());
        fragments.add(viewYears = new HomeFragment());
        fragments.add(viewSearch = new HomeFragment());

        FragmentPagerAdapter pagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager(), getApplicationContext(), fragments);
        viewPager.setAdapter(pagerAdapter);

        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_stars_white_24dp);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_date_range_white_24dp);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_search_white_24dp);
        
	}
}
