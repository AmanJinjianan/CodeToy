package com.qixiang.codetoy;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.qixiang.codetoy.Fragment.MyAssetsFragment;
import com.qixiang.codetoy.Fragment.MyInfoFragment;
import com.qixiang.codetoy.ViewAdapter.ViewPagerFragmentAdapter;

import java.util.ArrayList;
import java.util.List;

public class MyActivity extends AppCompatActivity implements View.OnClickListener {
    public MyAssetsFragment msf;
    public MyInfoFragment mif;

    List<Fragment> mFragmentList = new ArrayList<Fragment>();
    ViewPager mViewpager;
    ViewPagerFragmentAdapter mViewPagerFragmentAdapter;
    FragmentManager mFragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);


        mFragmentManager = getSupportFragmentManager();
        msf = new MyAssetsFragment();
        mif = new MyInfoFragment();

        mFragmentList.add(msf);
        mFragmentList.add(mif);

        findViewById(R.id.btn_my_title).setOnClickListener(this);
        findViewById(R.id.btn_my_info).setOnClickListener(this);
        findViewById(R.id.btn_my_assets).setOnClickListener(this);

        mViewPagerFragmentAdapter =   new ViewPagerFragmentAdapter(mFragmentManager,mFragmentList);
        mViewpager = (ViewPager) findViewById(R.id.ViewPagerLayout_my);
        //mViewpager.addOnPageChangeListener(new ControlMainAct.ViewPagerOnPagerChangedLisenter());
        mViewpager.setAdapter(mViewPagerFragmentAdapter);
        mViewpager.setCurrentItem(0);
    }

    void initView(){

    }
    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btn_my_title:
                mViewpager.setCurrentItem(0);
                break;
            case R.id.btn_my_info:
                mViewpager.setCurrentItem(1);
                break;
            case R.id.btn_my_assets:
                mViewpager.setCurrentItem(0);
                break;
        }
    }
}
