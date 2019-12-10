package com.qixiang.codetoy;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.qixiang.codetoy.BLE.MyListener;
import com.qixiang.codetoy.BLE.SendBle;
import com.qixiang.codetoy.BLE.Tools;
import com.qixiang.codetoy.MyView.MyRockerView;

import java.util.Timer;
import java.util.TimerTask;

import static com.qixiang.codetoy.ControlMainAct.Action_control_data;

public class ControllerProActivity extends Activity implements View.OnClickListener, MyRockerView.OnShakeListener {

    public boolean reveiveFlag = false;

    private Intent intent = new Intent(Action_control_data);

    private MyTimerTask mt34;
    private Timer timer34;

    //指令参数
    byte[] commandTwoBytes = new byte[2];
    //保存下位机设备ID
    public byte theOneByte=0;

    String data = "";
    private SendBle mSendBle;
    private MyRockerView myRockerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setFullScreen();
        setContentView(R.layout.layout_controlpro);
        //checkBluetoothPermission();
        reveiveFlag = false;
        initView();

        FrameLayout.LayoutParams ll = new FrameLayout.LayoutParams((int)(ControlMainAct.height*1.77),ControlMainAct.height);
        ll.gravity = Gravity.CENTER_HORIZONTAL;

        reveiveFlag = false;
        mSendBle = new SendBle(this);
    }
    void initView(){
        findViewById(R.id.ib_control_pro_back).setOnClickListener(this);

        findViewById(R.id.btn_left_down).setOnTouchListener(MyTai);
        findViewById(R.id.btn_left_up).setOnTouchListener(MyTai);
        findViewById(R.id.btn_right_down).setOnTouchListener(MyTai);
        findViewById(R.id.btn_right_up).setOnTouchListener(MyTai);

        findViewById(R.id.btn_control_start).setOnClickListener(this);

        findViewById(R.id.btn_left_pro).setOnTouchListener(MyTai);
        findViewById(R.id.btn_right_pro).setOnTouchListener(MyTai);

        myRockerView = (MyRockerView) findViewById(R.id.act_myrockview);
        myRockerView.setOnShakeListener(MyRockerView.DirectionMode.DIRECTION_8,this);
    }
    void setFullScreen(){
        // 隐藏标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 隐藏状态栏
        // 定义全屏参数
        int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        // 获得当前窗体对象
        Window window = ControllerProActivity.this.getWindow();
        // 设置当前窗体为全屏显示
        window.setFlags(flag, flag);
    }
    public MyListener listener = new MyListener() {
        @Override
        public void readData(byte[] var1, String var2) {}

        @Override
        public void onSendStatus(Boolean var1) {
            Tools.setLog("log1", "..........................onSendStatus......");
        }
    };

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.ib_control_pro_back){
            this.finish();
            return;
        }else if(v.getId() == R.id.btn_control_start){
            commandTwoBytes[0] = (byte)0x71;commandTwoBytes[1] = (byte)0x00;
            intent.putExtra("data",commandTwoBytes);
            sendBroadcast(intent);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void direction(MyRockerView.Direction direction,float distance) {
        switch (direction){
            case DIRECTION_LEFT://左 A6B9
                commandTwoBytes[0] = (byte)0xA6;commandTwoBytes[1] = (byte)0xB9; touchModel = 1;
                break;
            case DIRECTION_RIGHT://右 A9B6
                commandTwoBytes[0] = (byte)0xA9;commandTwoBytes[1] = (byte)0xB6; touchModel = 1;
                break;
            case DIRECTION_UP://上 AABA
                commandTwoBytes[0] = (byte)0xAA;commandTwoBytes[1] = (byte)0xBA; touchModel = 1;
                break;
            case DIRECTION_DOWN://下 A5B5
                commandTwoBytes[0] = (byte)0xA5;commandTwoBytes[1] = (byte)0xB5; touchModel = 1;
                break;
            case DIRECTION_UP_LEFT://左上 A2B8
                commandTwoBytes[0] = (byte)0xA2;commandTwoBytes[1] = (byte)0xB8; touchModel = 1;
                break;
            case DIRECTION_UP_RIGHT://右上 A8B2
                commandTwoBytes[0] = (byte)0xA8;commandTwoBytes[1] = (byte)0xB2; touchModel = 1;
                break;
            case DIRECTION_DOWN_LEFT://左下 A4B1
                commandTwoBytes[0] = (byte)0xA4;commandTwoBytes[1] = (byte)0xB1; touchModel = 1;
                break;
            case DIRECTION_DOWN_RIGHT://右下 A1B4
                commandTwoBytes[0] = (byte)0xA1;commandTwoBytes[1] = (byte)0xB4; touchModel = 1;
                break;
            case DIRECTION_CENTER://右下 A1B4
                if(touchModel == 1){
                    commandTwoBytes[0] = (byte)0x00;commandTwoBytes[1] = (byte)0x00;
                }
                break;
        }
        intent.putExtra("data",commandTwoBytes);
        sendBroadcast(intent);
        Tools.setLog("Rocker","Direction:::::"+direction +" distance:"+distance);
    }

    @Override
    public void onFinish() {}


    class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            intent.putExtra("data",commandTwoBytes);
            sendBroadcast(intent);
        }
    }
    // 0: 非八项控制摇杆指令    1：八项控制摇杆指令
    int touchModel = -1;
    private View.OnTouchListener MyTai = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if(event.getAction()==MotionEvent.ACTION_CANCEL || event.getAction()==MotionEvent.ACTION_UP){
                Toast.makeText(ControllerProActivity.this, "UP", Toast.LENGTH_SHORT).show();
                if(touchModel == 0){
                    commandTwoBytes[0] = (byte)0x00;commandTwoBytes[1] = (byte)0x00;
                    intent.putExtra("data",commandTwoBytes);
                    sendBroadcast(intent);
                }
            } else  if(event.getAction()==MotionEvent.ACTION_DOWN){
                Toast.makeText(ControllerProActivity.this, "ACTION_DOWN", Toast.LENGTH_SHORT).show();
                touchModel = 0;
                switch (v.getId()){
                    case R.id.btn_left_pro:
                        commandTwoBytes[0] = (byte)0xA6;commandTwoBytes[1] = (byte)0xB6;
                        break;
                    case R.id.btn_right_pro:
                        commandTwoBytes[0] = (byte)0xA9;commandTwoBytes[1] = (byte)0xB9;
                        break;

                    case R.id.btn_left_up:
                        commandTwoBytes[0] = (byte)0xA6;commandTwoBytes[1] = (byte)0xB0;
                        break;
                    case R.id.btn_right_up:
                        commandTwoBytes[0] = (byte)0xA9;commandTwoBytes[1] = (byte)0xB0;
                        break;
                    case R.id.btn_left_down:
                        commandTwoBytes[0] = (byte)0xA0;commandTwoBytes[1] = (byte)0xB9;
                        break;
                    case R.id.btn_right_down:
                        commandTwoBytes[0] = (byte)0xA0;commandTwoBytes[1] = (byte)0xB6;
                        break;
                }
                intent.putExtra("data",commandTwoBytes);
                sendBroadcast(intent);
            }
            return false;
        }
    };
}

