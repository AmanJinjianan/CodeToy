package com.qixiang.codetoy.MyView;

import android.view.View;

/**
 * Created by Administrator on 2018/8/13.
 */

public class MyClickLister implements View.OnClickListener{
    public int MyPosition;
    public MyClickLister(int position){
        this.MyPosition = position;
    }
    @Override
    public void onClick(View view) {
    }
}
