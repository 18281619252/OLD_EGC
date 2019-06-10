package com.example.blue;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 作为客户端
 */
public class ClentActivity extends BaseActivity {
    ListView mLvBle;//蓝牙列表
    Button search;//搜索附近蓝牙

    private List<BluetoothDevice> mDeviceList = new ArrayList<>();//蓝牙列表
    private BleAdapter mAdapter;

    BluetoothAdapter mBluetoothAdapter;

    /**
     * 广播接收者
     */
    private BroadcastReceiver bTReceive = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Log.i("BroadcastReceiver", "BluetoothDevice.ACTION_FOUND  device = "+device.getName()+"  "+device.getAddress());
                for (BluetoothDevice d : mDeviceList) {
                    if (TextUtils.isEmpty(d.getAddress()) || TextUtils.isEmpty(device.getAddress()) || d.getAddress().equalsIgnoreCase(device.getAddress())) {
                        return;
                    }
                }
                mDeviceList.add(device);
                mAdapter.notifyDataSetChanged();
            } else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                Log.e("BroadcastReceiver", "BluetoothDevice.ACTION_BOND_STATE_CHANGED");
                BluetoothDevice blnDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                setProgressMsg("正在配对..");
                switch (blnDevice.getBondState()) {
                    case BluetoothDevice.BOND_BONDING://正在配对
                        setProgressMsg("正在配对..");
                        break;

                    case BluetoothDevice.BOND_BONDED://配对结束
                        mAdapter.notifyDataSetChanged();
                        setProgressMsg("配对完成,开始连接..");
                        startActivity(blnDevice);
                        break;

                    case BluetoothDevice.BOND_NONE://取消配对/未配对
                        showToast("配对失败!");
                        for (BluetoothDevice d : mDeviceList) {
                            if (d.getName().equalsIgnoreCase(blnDevice.getName())) {
                                mAdapter.notifyDataSetChanged();
                                return;
                            }
                        }
                        dismissProgressDialog();
                    default:
                        break;
                }
            } else if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                Log.e("BroadcastReceiver", "BluetoothAdapter.ACTION_BOND_STATE_CHANGED");
                Toast.makeText(context, "BluetoothAdapter.ACTION_BOND_STATE_CHANGED", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clent);
        mLvBle = findViewById(R.id.lv_ble);//蓝牙列表
        search = findViewById(R.id.btn_search);//搜索附近蓝牙

         mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        registerBTReceiver();// 注册蓝牙相关的各种广播

        //给蓝牙列表设置数据
        mAdapter = new BleAdapter(this);
        mAdapter.setData(mDeviceList);
        mLvBle.setAdapter(mAdapter);
        mLvBle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (mBluetoothAdapter != null)
                    mBluetoothAdapter.cancelDiscovery();
                BluetoothDevice device = mDeviceList.get(i);
                bondBT(device);
            }
        });

        search.setOnClickListener(new View.OnClickListener() {//点击搜索按钮
            @Override
            public void onClick(View view) {
                if (mBluetoothAdapter.isDiscovering()) {
                    return;
                }
                mBluetoothAdapter.startDiscovery();
            }
        });
    }

    /**
     * 注册广播
     */
    public void registerBTReceiver() {
        // 设置广播信息过滤
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);   //发现蓝牙事件
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);//蓝牙扫描结束事件
//        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);//状态改变
        intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        // 注册广播接收器，接收并处理搜索结果
        registerReceiver(bTReceive, intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mBluetoothAdapter.cancelDiscovery();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //注销广播
        unregisterReceiver(bTReceive);
    }

    /**
     * 绑定蓝牙
     */
    private void bondBT(BluetoothDevice device) {
        showProgressDialog("配对蓝牙开始");
        // 搜索蓝牙设备的过程占用资源比较多，一旦找到需要连接的设备后需要及时关闭搜索
        // 获取蓝牙设备的连接状态
        int connectState = device.getBondState();
        switch (connectState) {
            case BluetoothDevice.BOND_NONE: // 未配对
                setProgressMsg("开始配对");
                try {
                    Method createBondMethod = BluetoothDevice.class.getMethod("createBond");
                    createBondMethod.invoke(device);
                } catch (Exception e) {
                    showToast("配对失败！");
                    dismissProgressDialog();
                    e.printStackTrace();
                }
                break;

            case BluetoothDevice.BOND_BONDED: // 已配对
                //进入连接界面
                startActivity(device);
                break;
        }
    }

    //如果配对好了，就跳转到发消息界面
    private void startActivity(BluetoothDevice device) {
        dismissProgressDialog();
        Intent intent = new Intent(this, ClentSendActivity.class);
        intent.putExtra("device", device);

        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }





}
