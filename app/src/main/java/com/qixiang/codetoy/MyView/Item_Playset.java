package com.qixiang.codetoy.MyView;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.qixiang.codetoy.BLE.Tools;
import com.qixiang.codetoy.ControlMainAct;
import com.qixiang.codetoy.R;
import com.qixiang.codetoy.Util.DpUtils;
import com.qixiang.codetoy.Util.Utils;

import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Administrator on 2019/10/4.
 */

public class Item_Playset extends RelativeLayout{
    public Handler myHandler;
    private Context myContext;
    private int toggleFlag = 0;
    RelativeLayout rl;
    TextView tv_item;
    public AnimationDrawable animationDrawable = null;
    public Item_Playset(Context context, int s, Handler theHandler) {
        super(context);

        toggleFlag = 0;
        myContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.item_playset, this);
        myHandler = theHandler;
        //
        rl = (RelativeLayout)findViewById(R.id.rl_play_center);
        ImageButton ib = (ImageButton)findViewById(R.id.iv_center_play);

        tv_item = (TextView)findViewById(R.id.tv_item_name);


        switch (s){
            case 0: rl.setBackgroundResource(R.drawable.anim_ble_con);
                    animationDrawable = (AnimationDrawable) rl.getBackground();

                    rl.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            animationDrawable.start();
                            //animationDrawable.stop();
                            if(Utils.myConnectState == Utils.ConnectState.DEFAULT){
                                if(myHandler != null){
                                    //开始连接
                                    myHandler.sendEmptyMessage(4601);
                                    rl.setEnabled(false);
                                }
                            }else{
                                Toast.makeText(myContext, "未初始状态，请稍等..", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                break;
            case 1: findViewById(R.id.rl_play_center).setBackgroundResource(R.drawable.img_item_playset7);
                findViewById(R.id.iv_center_play).setVisibility(View.VISIBLE);
                findViewById(R.id.iv_center_play).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(myHandler != null){
                            if(toggleFlag % 2 ==0){
                                if(Utils.myConnectState == Utils.ConnectState.PRECONNECT){
                                    myHandler.sendEmptyMessage(4603);
                                    findViewById(R.id.iv_center_play).setBackgroundResource(R.drawable.btn_con_ble1);
                                    Utils.myConnectState = Utils.ConnectState.CONNECTED;
                                    toggleFlag ++;
                                }else {
                                    Toast.makeText(myContext, "连接未准备，请稍等..", Toast.LENGTH_LONG).show();
                                }
                            }else {
                                if(Utils.myConnectState == Utils.ConnectState.CONNECTED){
                                    myHandler.sendEmptyMessage(46033);
                                    findViewById(R.id.iv_center_play).setBackgroundResource(R.drawable.btn_con_ble2);
                                    Utils.myConnectState = Utils.ConnectState.DEFAULT;
                                    toggleFlag ++;
                                }else {
                                    Toast.makeText(myContext, "不能断开，请稍等..", Toast.LENGTH_LONG).show();
                                }
                            }

                        }

                    }
                });
                break;
            case 2: findViewById(R.id.rl_play_center).setBackgroundResource(R.drawable.img_item_playset2);break;
            case 3: findViewById(R.id.rl_play_center).setBackgroundResource(R.drawable.img_item_playset3);break;
            case 4: findViewById(R.id.rl_play_center).setBackgroundResource(R.drawable.img_item_playset4);break;
            case 5: findViewById(R.id.rl_play_center).setBackgroundResource(R.drawable.img_item_playset5);break;
        }
    }

    public void viewReset(){
        rl.setEnabled(true);
        toggleFlag = 0;
    }
    public void setItemName(String s){
        tv_item.setText(s);
    }
}
