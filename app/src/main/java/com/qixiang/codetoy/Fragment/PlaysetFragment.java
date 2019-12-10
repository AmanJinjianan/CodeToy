package com.qixiang.codetoy.Fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.qixiang.codetoy.ControlMainAct;
import com.qixiang.codetoy.MyView.Item_Playset;
import com.qixiang.codetoy.R;
import com.qixiang.codetoy.Util.DpUtils;

import java.util.ArrayList;

//import android.app.Fragment;

/**
 * Created by Administrator on 2018/7/19.
 */

public class PlaysetFragment extends Fragment {
    View mView;

    private HorizontalScrollView horizontalScrollView;
    public LinearLayout container;
    public Activity context1;
    public Handler theHandler;

    public void setTheHandler(Handler handler){
        theHandler = handler;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        if(mView == null){
            mView = inflater.inflate(R.layout.fragment_playset,null);
        }

        return mView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Activity context) {
        context1 = context;
        super.onAttach(context);
    }

    private String cities[] = new String[]{"London", "Bangkok", "Paris", "Dubai", "Istanbul", "New York",
            "Singapore", "Kuala Lumpur", "Hong Kong", "Tokyo", "Barcelona",
            "Vienna", "Los Angeles", "Prague", "Rome", "Seoul", "Mumbai", "Jakarta",
            "Berlin", "Beijing", "Moscow", "Taipei", "Dublin", "Vancouver"};
    private ArrayList<String> data = new ArrayList<>();
    @Override
    public void onResume() {
        //Collections.addAll(data, cities);
        horizontalScrollView = (HorizontalScrollView) context1.findViewById(R.id.horizontalScrollView);
        container = (LinearLayout) context1.findViewById(R.id.horiziner334);

        //避免重复添加item
        if(container.getChildCount() == 0){
            int height = ControlMainAct.height - DpUtils.dp2px(getContext(),65);
            int width = (int)(height/1.5);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width,height);
            layoutParams.gravity = Gravity.CENTER;
            layoutParams.setMargins(40, 10, 40, 10);

            for (int i = 0; i < 4; i++) {
                Item_Playset ip = new Item_Playset(context1,i,theHandler);
                ip.setLayoutParams(layoutParams);
                container.addView(ip);
                container.invalidate();
            }
        }

        super.onResume();
    }

    public void addItem(byte[] id){
//        Item_Playset ip = new Item_Playset(context1,7,theHandler);
//        ip.setLayoutParams(layoutParams);
//        if(container != null){
//            container.addView(ip);
//        }
    }

    public Item_Playset getItemByIndex(int index){
        if(container.getChildCount()>0)
            return  (Item_Playset)(container.getChildAt(index));
        else
            return null;
    }
}


