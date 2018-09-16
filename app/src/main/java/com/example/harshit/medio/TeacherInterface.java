package com.example.harshit.medio;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class TeacherInterface extends AppCompatActivity {

    private SectionsPagerAdapter mSectionPagerAdapter;
    private ViewPager mviewPager;
    public static int position=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_interface);

        mSectionPagerAdapter =new SectionsPagerAdapter(getSupportFragmentManager());
        mviewPager=(ViewPager) findViewById(R.id.container);
        setUpViewPager(mviewPager);

        TabLayout tabLayout=(TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mviewPager);

    }
    private void setUpViewPager(ViewPager viewPager){
        SectionsPagerAdapter adapter=new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Tab2TimeTable(),"Time Table");
        adapter.addFragment(new Tab3Assignments(),"Assignments");
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(position);
    }
}
