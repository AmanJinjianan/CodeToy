package com.qixiang.codetoy.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qixiang.codetoy.R;

//import android.app.Fragment;

/**
 * Created by Administrator on 2018/7/24.
 */

public class MyInfoFragment extends Fragment{

    private View mView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        if(mView == null){
            mView = inflater.inflate(R.layout.fragment_myinfo,null);
        }
        return mView;
    }
}
