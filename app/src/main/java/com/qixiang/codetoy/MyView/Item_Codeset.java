package com.qixiang.codetoy.MyView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.qixiang.codetoy.R;

/**
 * Created by Administrator on 2019/10/4.
 */

public class Item_Codeset extends RelativeLayout{
    public Item_Codeset(Context context) {
        super(context);
        View view = LayoutInflater.from(context).inflate(R.layout.item_studyset, this);
    }

}
