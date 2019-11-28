package com.qixiang.codetoy;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.qixiang.codetoy.BLE.BLEService;
import com.qixiang.codetoy.BLE.MyListener;
import com.qixiang.codetoy.BLE.SendBle;
import com.qixiang.codetoy.BLE.Tools;
import com.qixiang.codetoy.Util.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/7/28.
 */

public class BleDataActivityCopy extends Activity implements View.OnClickListener{

    private final static int REQUEST_ENABLE_BT=2001;
    private BluetoothDevice theDevice;
    private List<String> fdArrayList = new ArrayList<String>();
    private boolean connected_flag;
    private boolean exit_activity = false;
    public String tmp,hex;
    public boolean reveiveFlag = false;

    public  byte theRandowData = 0;
    public  byte[] theTwoByte = new byte[]{0x00,0x00};

        private BLEService.OnWriteOverCallback mOnWriteOverCallback = new BLEService.OnWriteOverCallback(){

        @Override
        public void OnWriteOver(BluetoothGatt gatt,BluetoothGattCharacteristic characteristic, int statue) {

            if(statue == BluetoothGatt.GATT_SUCCESS){
                Log.e(Utils.TAG,"OnWriteOver..............");
                if(fdArrayList.size()!= 0){
                    fdArrayList.remove(0);
//                    if(fdArrayList.size()!= 0){
//                        spliteArray =fdArrayList.get(0).split(",");
//                        if (spliteArray[0].equals("#")) {//常规指令
//                            //SendDataString(spliteArray[1]);
//                            Tools.mBleService.characterWrite1.setValue(hexToBytes(spliteArray[1]));
//                            Tools.mBleService.mBluetoothGatt.writeCharacteristic(Tools.mBleService.characterWrite1);
//                        }else {//速度指令
//                            //SendControllerSpeed(spliteArray[1]);
//                            Tools.mBleService.characterWrite2.setValue(hexToBytes(spliteArray[1]));
//                            Tools.mBleService.mBluetoothGatt.writeCharacteristic(Tools.mBleService.characterWrite2);
//                        }
//                    }
                }
            }else if(statue == BluetoothGatt.GATT_FAILURE){
                myHandler.sendEmptyMessage(8);
//                if (fdArrayList.size()!= 0){
//                    spliteArray =fdArrayList.get(0).split(",");
//                    if (spliteArray[0].equals("#")) {//常规指令
//                        //SendDataString(spliteArray[1]);
//                        Tools.mBleService.characterWrite1.setValue(hexToBytes(spliteArray[1]));
//                        Tools.mBleService.mBluetoothGatt.writeCharacteristic(Tools.mBleService.characterWrite1);
//                    }else {//速度指令
//                        //SendControllerSpeed(spliteArray[1]);
//                        Tools.mBleService.characterWrite2.setValue(hexToBytes(spliteArray[1]));
//                        Tools.mBleService.mBluetoothGatt.writeCharacteristic(Tools.mBleService.characterWrite2);
//                    }
//
//                }
            }
        }

    };
    public static String byteArrayToStr(byte[] byteArray) {
        if (byteArray == null) {
            return null;
        }
        String str = new String(byteArray);
        return str;
    }
    public  String byteArrayToHexStr(byte[] byteArray) {
        if (byteArray == null){
            return null;
        }
        char[] hexArray = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[byteArray.length * 2];
        for (int j = 0; j < byteArray.length; j++) {
            int v = byteArray[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    String data = "",theReceiveData;

    int ppCount = 0;
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {

        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            if(!reveiveFlag)
                return;

            if(null == device.getName())
            {
                if(identifySysID(scanRecord)){
                    Tools.setLog("log1", "系统值匹配成功.......222222222222222222...LLL:");
                    if(identifyRanData(scanRecord)){//匹配校验发送的随机数
                        Tools.setLog("log1", "随机数匹配成功......22222222222222222222222222....LLL:");
                        if(0x02 == (scanRecord[11] & 0x0F)){//  和0x0F校验 == 0
                            Tools.setLog("log1", "和0x0F校验成功.....222222222222222222222222222222222.....LLL:");
                            theTwoByte[0] =scanRecord[12];
                            theTwoByte[1] =scanRecord[13];

                            theReceiveData = bytesToHexFun3(scanRecord);
                            myHandler.sendEmptyMessage(13141);
                        }
                    }
                    theReceiveData = bytesToHexFun3(scanRecord);
                    //myHandler.sendEmptyMessage(13141);
                }
                //
                device.getUuids();
                Tools.setLog("log1", device.getName()+"LeScanCallback..........ppp:\n"+ device.getUuids());
                //myHandler.sendEmptyMessage(1314);
                if(!byteArrayToHexStr(scanRecord).substring(0,18).equals(data))
                {
                    //data = byteArrayToHexStr(scanRecord).substring(0,18);
                    //Tools.setLog("log1", device.getName()+"LeScanCallback..........ppp:"+ byteArrayToHexStr(scanRecord).length());
                    //myHandler.sendEmptyMessage(1313);
                }
            }else{
                Tools.setLog("log1", device.getName()+"被发现..........LLL:");
            }

            /*if(null != device && null != device.getName()){
                if (theDevice == null) {
                    if (device.getName().equals("TSnet")) {
                        theDevice = device;

                        myHandler.sendEmptyMessage(13);
                    }
                }
            }*/
        }
    };

        private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onServiceConnected(ComponentName arg0, IBinder service) {
            // TODO Auto-generated method stub

            BLEService.LocalBinder binder = (BLEService.LocalBinder)service;
            Tools.mBleService = binder.getService();
            if (Tools.mBleService.initBle()) {
                if(!Tools.mBleService.mBluetoothAdapter.isEnabled()){
                    final Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                }else {
                    myHandler.sendEmptyMessage(11);
                    //scanBle();
                }
            }
        }
    };


        int numcount=0;
    int numcount2=0;
        private Handler myHandler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            //Tools.setLog("log1", "myHandler.........."+msg.what);
            switch (msg.what) {
                case 13141:
                    Tools.setLog("log1", "..........................................................................13141");
                    numcount2++;
                    if(numcount2<2){
                        stopSendData();
                        theReveieData.setText(theReceiveData);

                        theReveieTimes.setText("收到："+numcount2+"次");
                        maxSendData(false);
                    }

                    break;
                case 131415:
                    Tools.setLog("log1", "............theNext.........."+msg.what);
                    break;
                case 111:
                    //Toast.makeText(MainActivity.this, "startActivityForResult..", Toast.LENGTH_SHORT).show();
                    //Intent intent = new Intent(MainActivity.this,VideoActivity.class);
                    //startActivityForResult(intent, 12);
                    break;
                case 110:
                    Toast.makeText(BleDataActivityCopy.this, "广播成功（一次）............", Toast.LENGTH_SHORT).show();
                    break;
                case 112:
                    Toast.makeText(BleDataActivityCopy.this, "广播失败............", Toast.LENGTH_SHORT).show();
                    break;
                case 1110:
                    Toast.makeText(BleDataActivityCopy.this, "持续广播成功............", Toast.LENGTH_SHORT).show();
                    break;
                case 11120:
                    Toast.makeText(BleDataActivityCopy.this, "停止广播成功.............", Toast.LENGTH_SHORT).show();
                    break;
                case 11121:
                    Toast.makeText(BleDataActivityCopy.this, "停止广播失败.............", Toast.LENGTH_SHORT).show();
                    break;
                case 10:
                    bindService(new Intent(BleDataActivityCopy.this,BLEService.class), connection, Context.BIND_AUTO_CREATE);
                    break;
                case 11:
                    if(Tools.mBleService != null)
                        Tools.mBleService.scanBle(mLeScanCallback);
                    break;
                case 1313:
                    numcount++;
                    //thetr.setText(data+"  共"+numcount+"次");
                    Toast.makeText(BleDataActivityCopy.this, "连接成功:"+data+" ", Toast.LENGTH_LONG).show();
                    break;
                case 1314:
                    ppCount++;
                    //thetr.setText("sleep100ms 共:"+ppCount+"次");
                    //Toast.makeText(BleConnectActivity.this, "连接成功:"+data+" ", Toast.LENGTH_LONG).show();
                    break;
                case 1315:
                    ppCount++;
                    //thetr.setText("Setting 100ms 共:"+ppCount+"次");
                    //Toast.makeText(BleConnectActivity.this, "连接成功:"+data+" ", Toast.LENGTH_LONG).show();
                    break;
                case 12:
                    if(Tools.mBleService != null){
                        Tools.mBleService.stopscanBle(mLeScanCallback);
                        Tools.mBleService.setOnWriteOverCallback(mOnWriteOverCallback);
                    }
                    Toast.makeText(BleDataActivityCopy.this, "连接成功", Toast.LENGTH_LONG).show();
                    Intent intent2 = new Intent("com.qixiang.blesuccess");
                    sendBroadcast(intent2);

                    Intent intent = new Intent(BleDataActivityCopy.this,MainActivity.class);
                    startActivity(intent);
                    break;
                case 122:
                    if(Tools.mBleService != null){
                        Tools.mBleService.stopscanBle(mLeScanCallback);
                    }
                    break;
                case 13:
                    new MyConnectedThread().start();
                    break;
                case 14:
                    Bundle bundle=msg.getData();
                    String string =(String) bundle.get("Key");
                    Tools.setLog("log1", "msg.what 14:"+string);
                    Tools.mBleService.characterWrite1.setValue(hexToBytes(string));
                    Tools.mBleService.mBluetoothGatt.writeCharacteristic(Tools.mBleService.characterWrite1);
                    break;
                default:
                    break;
            }
        };
    };
    Animation myAnimation;
    private SendBle mSendBle;
    TextView  theReveieTimes,theReveieData,tv_id,mTextStatus;
    EditText et_SysID,et_SysID2,et_data;
    ScrollView mScrollView;
    Button btn_once,btn_more,btn_stop;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bledata);

        checkBluetoothPermission();
        bindService(new Intent(this,BLEService.class), connection, Context.BIND_AUTO_CREATE);

        //收到广播的次数统计
        theReveieTimes  =  (TextView) findViewById(R.id.tv_times22);
        //收到广播的数据部分
        theReveieData  =  (TextView) findViewById(R.id.tv_receive_data);

        scrollToBottom();

        //校验位数值
        tv_id =  (TextView) findViewById(R.id.tv_id);
        //接收部系统值
        et_SysID = (EditText) findViewById(R.id.et_systemid);

        //发送部系统值和数据
        et_SysID2 = (EditText) findViewById(R.id.et_systemid2);
        et_data = (EditText) findViewById(R.id.et_data);

        findViewById(R.id.btn_once).setOnClickListener(this);
        findViewById(R.id.btn_more).setOnClickListener(this);
        findViewById(R.id.btn_stop).setOnClickListener(this);

        findViewById(R.id.btn_once).setEnabled(false);

        findViewById(R.id.btn_reveive).setOnClickListener(this);
        findViewById(R.id.btn_stop_reveive).setOnClickListener(this);

        reveiveFlag = false;

        mSendBle = new SendBle(this);


        /*thetr = (TextView) findViewById(R.id.thrtittle);
        myAnimation = AnimationUtils.loadAnimation(this, R.anim.rotation1);
        findViewById(R.id.imageView111).startAnimation(myAnimation);

       ;*/
    }
    private void scrollToBottom()
    {
        mScrollView.post(new Runnable()
        {
            public void run()
            {
                mScrollView.smoothScrollTo(0, mTextStatus.getBottom());
            }
        });
    }
public void backtomain(View view){
        Intent intent = new Intent(BleDataActivityCopy.this,MainActivity.class);
        startActivity(intent);
        //BleConnectActivity.this.finish();
}
    //校验系统ID
    public boolean identifySysID(byte[] data){
        byte[] byteData = toBytes("0201050319C1030716");

        for(int i=0;i<byteData.length;i++){
            if(!(byteData[i] == data[i]))
                return false;
        }
        return true;
    }
    public boolean identifyRanData(byte[] data){

        if(data.length<15){
            Toast.makeText(BleDataActivityCopy.this, "返回参数异常了", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(data[10] != theRandowData)
             return false;
        else
            return true;
    }
    public static String bytesToHexFun3(byte[] bytes) {
        byte[] iData = new byte[6];
        for (int i= 0;i<6;i++){
            iData[i] = bytes[i+9];
        }

        StringBuilder buf = new StringBuilder(iData.length * 2);
        for(byte b : iData) { // 使用String的format方法进行转换
            buf.append(String.format("%02x", new Integer(b & 0xff)));
        }

        return buf.toString();
    }

private void InitBle(){
    BluetoothManager bManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
    BluetoothAdapter bAdapter = bManager.getAdapter();
    if(bAdapter == null){
        Toast.makeText(BleDataActivityCopy.this, "not support", Toast.LENGTH_SHORT).show();
        finish();
    }
    if (bAdapter.isEnabled()) {
        bindService(new Intent(this,BleDataActivityCopy.class), connection, Context.BIND_AUTO_CREATE);
    }else {
        bAdapter.enable();
    }
    setBroadcastReveiver();
}

    /**
     * 将16进制字符串转换为byte[]
     *
     * @param str
     * @return
     */
    public  byte[] toBytes(String str) {
        if(str == null || str.trim().equals("")) {
            return new byte[0];
        }

        byte[] bytes = new byte[str.length() / 2];
        for(int i = 0; i < str.length() / 2; i++) {
            String subStr = str.substring(i * 2, i * 2 + 2);
            bytes[i] = (byte) Integer.parseInt(subStr, 16);
        }

        return bytes;
    }

    ArrayList<String> unPermissionList;
    private void checkBluetoothPermission() {
        if (Build.VERSION.SDK_INT >= 23) {

            String[] mPermission = new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.BLUETOOTH,
                    Manifest.permission.BLUETOOTH_ADMIN
            };
            unPermissionList = new ArrayList<String>();
            try {
                for (int i = 0; i < mPermission.length; i++) {
                    if (ContextCompat.checkSelfPermission(BleDataActivityCopy.this, mPermission[i]) != PackageManager.PERMISSION_GRANTED) {
                        unPermissionList.add(mPermission[i]);
                    }
                }
                for (int i = 0; i < mPermission.length; i++) {
                    if (ContextCompat.checkSelfPermission(BleDataActivityCopy.this, "android.permission.ACCESS_COARSE_LOCATION") == PackageManager.PERMISSION_GRANTED) {
                        if (isLocationEnabled()) {

                        }else {
                            Toast.makeText(BleDataActivityCopy.this, "Please open the location switch.", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivityForResult(intent, 0);
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                Toast.makeText(BleDataActivityCopy.this, "Exception0:"+e.toString(), Toast.LENGTH_LONG).show();
            }

        }else {
            InitBle();
            Toast.makeText(BleDataActivityCopy.this, "Init.............:", Toast.LENGTH_LONG).show();
            return;
        }
        if (unPermissionList.isEmpty()) {
            if (isLocationEnabled())
                InitBle();
                //Toast.makeText(BleConnectActivity.this, "Init.............:", Toast.LENGTH_LONG).show();
            //都授权了。。。
        }else {

            try {
                String[] permissionStrings = unPermissionList.toArray(new String[unPermissionList.size()]);
                ActivityCompat.requestPermissions(this, permissionStrings, 1);
            } catch (Exception e) {
                Toast.makeText(BleDataActivityCopy.this, "Exception11:"+e.toString(), Toast.LENGTH_LONG).show();
            }
        }

    }
    /**
     * 判断定位服务是否开启
     *
     * @param
     * @return true 表示开启
     */

    public boolean isLocationEnabled() {
        int locationMode = 0;
        String locationProviders;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(getContentResolver(), Settings.Secure.LOCATION_MODE);
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }
            return locationMode != Settings.Secure.LOCATION_MODE_OFF;
        } else {
            locationProviders = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }

    @SuppressLint("NewApi")
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        // TODO Auto-generated method stub
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        //Toast.makeText(TestAct.this, "requestCode:"+requestCode, Toast.LENGTH_LONG).show();
        for (int i = 0; i < grantResults.length; i++) {
            //Toast.makeText(ScanActivity.this, permissions[i]+"     "+grantResults[i], Toast.LENGTH_LONG).show();

            if (permissions[i].equals("android.permission.ACCESS_COARSE_LOCATION")) {
                if ((grantResults[i] ==0)) {
                    if(!isLocationEnabled()){
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(intent, 0);
                    }else {
                        InitBle();
                        Toast.makeText(BleDataActivityCopy.this, "Init.............:", Toast.LENGTH_LONG).show();
                    }
                }else {
                    finish();
                }

            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 0){
            if (isLocationEnabled()) {
                checkBluetoothPermission();
            }else {
                Toast.makeText(BleDataActivityCopy.this, "Please open the location switch.", Toast.LENGTH_LONG).show();
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(intent, 0);
                    }
                }, 1000);
            }
        }
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        Log.e("sunlei","FirstActivity---onPause");
    }
    //设置广播接收器
    private void setBroadcastReveiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BLEService.ACTION_STATE_CONNECTED);
        intentFilter.addAction(BLEService.ACTION_STATE_DISCONNECTED);
        intentFilter.addAction(BLEService.ACTION_WRITE_DESCRIPTOR_OVER);
        //intentFilter.addAction(BLEService.ACTION_CHARACTER_CHANGE);
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);

        bluetoothReceiver = new BluetoothReceiver();
        registerReceiver(bluetoothReceiver, intentFilter);
    }

    private BluetoothReceiver bluetoothReceiver = null;




public MyListener listener = new MyListener() {
    @Override
    public void readData(byte[] var1, String var2) {

    }

    @Override
    public void onSendStatus(Boolean var1) {
        Tools.setLog("log1", "..........................onSendStatus......");
        if (var1)
            Toast.makeText(BleDataActivityCopy.this, "...........成功...............", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(BleDataActivityCopy.this, "...........失败...............", Toast.LENGTH_SHORT).show();
    }
};
    public class BluetoothReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context arg0, Intent intent) {
            // TODO Auto-generated method stub
            String action = intent.getAction();

            if(BLEService.ACTION_CHARACTER_CHANGE.equals(action)){

                Log.e(Utils.TAG,"心跳。。。。。。。。。。。。。。");
                ((Button)findViewById(R.id.btn_ble_jump)).setText("hahhaha");
                //tmp_byte = characteristic.getValue();
                byte[] tmp_byte = intent.getByteArrayExtra("value");
                tmp = "";
                for (int i = 0; i < tmp_byte.length; i++) {
                    hex = Integer.toHexString(tmp_byte[i] & 0xFF);
                    if (hex.length() == 1) {
                        hex = '0' + hex;
                    }
                    tmp = tmp + hex;
                }
            }else if(BLEService.ACTION_STATE_CONNECTED.equals(action)){

            }else if(BLEService.ACTION_STATE_DISCONNECTED.equals(action)){
                connected_flag = false;
                myHandler.sendEmptyMessage(11);
            }else if (BLEService.ACTION_WRITE_DESCRIPTOR_OVER.equals(action)) {
               connected_flag = true;
                theDevice = null;
                myHandler.sendEmptyMessage(12);
            }else if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                int blueState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);
                switch(blueState){
                    case BluetoothAdapter.STATE_ON:
                        //开始扫描
                        bindService(new Intent(BleDataActivityCopy.this,BleDataActivityCopy.class), connection, Context.BIND_AUTO_CREATE);
                        break;
                }

            }
        }
    }

        @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        exit_activity = true;
        //unbindService(connection);
    }

     public byte[] hexToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] bytes = new byte[length];
        String hexDigits = "0123456789ABCDEF";
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            int h = hexDigits.indexOf(hexChars[pos]) << 4;
            int l = hexDigits.indexOf(hexChars[pos + 1]);
            if (h == -1 || l == -1) {
                return null;
            }
            bytes[i] = (byte) (h | l);
        }
        return bytes;
    }

        public class MyConnectedThread extends Thread{

        @Override
        public void run() {
            // TODO Auto-generated method stub
            super.run();

            try {
                while (true) {
                    connected_flag = false;
                    if (exit_activity) return;
                    Tools.setLog("log1", "connectBle..........");
                    Tools.mBleService.connectBle(theDevice);

                    for(int j=0;j<50;j++){

                        if (connected_flag) {
                            break;
                        }
                        sleep(100);
                    }

                    if(connected_flag)
                        break;
                }
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
    }

    private String sendstr="";
    private String sss1="";
    private byte[] command_ = new byte[16];
    int data1 = 0;
    int data2 = 0;
    int data3 = 0;
    int data4 = 0;
    int data5 = 0;
    int data6 = 0;
    int data7 = 0;
    int data8 = 0;
    int data9 = 0;
    int data10 = 0;
    int data11 = 0;
    int data12 = 0;
    int data13 = 0;
    int data14 = 0;
    int data15 = 0;
    int data16 = 0;
    /**
     * 初始化操作指令
     */
    private void initData() {
        command_[0] = (byte) data16;
        command_[1] = (byte) data15;
        command_[2] = (byte) data14;
        command_[3] = (byte) data13;
        command_[4] = (byte) data12;
        command_[5] = (byte) data11;
        command_[6] = (byte) data10;
        command_[7] = (byte) data9;
        command_[8] = (byte) data8;
        command_[9] = (byte) data7;
        command_[10] = (byte) data6;
        command_[11] = (byte) data5;
        command_[12] = (byte) data4;
        command_[13] = (byte) data3;
        command_[14] = (byte) data2;
        command_[15] = (byte) data1;
        sss1 = bytesToHexString(command_);//需要发送的字节需反向拼接
        sendstr= sss1.substring(0,8)+"-"+sss1.substring(8,12)+"-"+sss1.substring(12,16)+"-"+sss1.substring(16,20)+"-"+sss1.substring(20,32);//"01020304-0506-0708-1112-131415161718"
    }
    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_once:

                initData();
                boolean flag = mSendBle.startSendOnce(command_, 3,"dd",myHandler);
                break;
            case R.id.btn_more:
                //initData();
                //getIDValue();
                //String data = et_data.getText().toString();
                //data="0d2be7f7-e6d6-4f48-a4bc-e521f9fd8eff";

                maxSendData(true);
                break;
            case R.id.btn_stop:
                stopSendData();
                break;
            case R.id.btn_reveive:
                reveiveFlag = true;
                break;
            case R.id.btn_stop_reveive:
                break;

        }
    }

    private void stopSendData() {
        findViewById(R.id.btn_stop).setEnabled(false);
        findViewById(R.id.btn_more).setEnabled(true);
        mSendBle.stopSend();
    }

    public void maxSendData(boolean theFlag){
        findViewById(R.id.btn_more).setEnabled(false);
        findViewById(R.id.btn_stop).setEnabled(true);
        String inString = "19FF100100000000";
        //String data = et_data.getText().toString();
            boolean flag1 = mSendBle.startSendMore(getIDValue(inString,theFlag), 3,myHandler);
            mSendBle.setListener(listener);

    }
    //得到校验结果（存在输入风险 未筛选非法字符）
    private String getIDValue(String data,boolean flag) {

        //String s="123bf";
        String regex="^[A-Fa-f0-9]+$";
        if(!data.matches(regex) || (data.length()%2 !=0)){
            Toast.makeText(BleDataActivityCopy.this, "字符串不是十六进制字符串", Toast.LENGTH_SHORT).show();
            return "";
        }

        if(data.length() != 16){
            Toast.makeText(BleDataActivityCopy.this, "数据长度异常..", Toast.LENGTH_SHORT).show();
            return "";
        }
        byte[] theByte = toBytes(data);

        if(flag){//代表第一发送
            theRandowData = getRandomData();
            theByte[3] = theRandowData;
        }else {
            theByte[2] = 0x12;
            theByte[3] = theTwoByte[0];
            theByte[4] = theTwoByte[1];
        }

        //将随机数整合到数组中，并显示在view中
        String theString = byteArrayToHexStr(theByte);

        SpannableStringBuilder style=new SpannableStringBuilder(theString);
        //str代表要显示的全部字符串
        style.setSpan(new ForegroundColorSpan(Color.RED),6,8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //３代表从第几个字符开始变颜色，注意第一个字符序号是０．
        //８代表变色到第几个字符．
        et_data.setText(style);

        //颠倒数组顺序
        for (int i=0;i<theByte.length/2;i++){
            byte temp = theByte[i];
            theByte[i] = theByte[7-i];
            theByte[7-i] = temp;
        }
        byte theResult = 0;
        //依次"亦或"九个byte
        for (int i=0;i<theByte.length;i++){
            theResult = (byte)(theByte[i]^theResult);
        }
        theResult = (byte)(theResult^0x5A);
        tv_id.setText("校验值：\n"+theResult);
        //组织新的数据 附加校验位
        byte[] newByte = new byte[theByte.length+1];
        for (int i=0;i<newByte.length;i++){
            if(i == 0)
                newByte[i] = theResult;
            else
                newByte[i] = theByte[i-1];
        }

        String stringData = bytesToHexString(newByte);
        return formData(stringData);
    }

    public byte getRandomData(){
        int n = (int)(1+Math.random()*(253));

        byte[] b = new byte[4];
        b[0] = (byte)(n & 0xff);

        return b[0];
    }
   /* public String getRandomData(){
        int n = (int)(1+Math.random()*(253));
        String[] hexArray = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

        if (n < 0) {
            n = n + 256;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return hexArray[d1] + hexArray[d2];
    }*/
    public String formData(String stringData){
        if(stringData.length() != 18)
            Toast.makeText(BleDataActivityCopy.this, "数据长度异常2..", Toast.LENGTH_SHORT).show();

        return stringData.substring(0,8)+"-"+stringData.substring(8,12)+"-"+stringData.substring(12,16)+"-"+stringData.substring(16);
    }


}
