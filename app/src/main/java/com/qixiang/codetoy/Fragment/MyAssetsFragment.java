package com.qixiang.codetoy.Fragment;

import android.support.v4.app.Fragment;
//import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qixiang.codetoy.R;

/**
 * Created by Administrator on 2018/7/24.
 */

public class MyAssetsFragment extends Fragment{

    private View mView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        if(mView == null){
            mView = inflater.inflate(R.layout.fragment_assets,null);
        }
        return mView;
    }
}
