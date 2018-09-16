package com.example.harshit.medio;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class StudentInterface extends AppCompatActivity {

    private SectionsPagerAdapter mSectionPagerAdapter;
    private ViewPager mviewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_interface);

        mSectionPagerAdapter =new SectionsPagerAdapter(getSupportFragmentManager());
        mviewPager=(ViewPager) findViewById(R.id.container);
        setUpViewPager(mviewPager);

        TabLayout tabLayout=(TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mviewPager);


    }
    private void setUpViewPager(ViewPager viewPager){
        SectionsPagerAdapter adapter=new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new StudentTab1Attendance(),"Attendance");
        adapter.addFragment(new StudentTab2TimeTable(),"Time Table");
        adapter.addFragment(new StudentTab3Assignments(),"Assignments");
        viewPager.setAdapter(adapter);
    }
}
