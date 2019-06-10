package com.example.blue;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * 客户端发消息
 */
public class ClentSendActivity extends BaseActivity {
    private final static String TAG = Electrocardiogram.class.getSimpleName();
    private View button1;
    public BluetoothDevice device;
    public BluetoothSocket bTSocket;
    TextView btNmae;
    public static String string;
    public static float num;
   //  TextView tv_recv;
    public static int i;





    public static String s;

    public static final int DATA=1;
    public static final int MAX=1;
    public static final int MIN=1;
    private TextView data;
    private TextView max_text;
    private TextView min_text;
    public int min=800;
    public int max=0;
    public String min_string;
    public String max_string;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     //   setContentView(R.layout.activity_clent_send);
        setContentView(R.layout.activity_clent_send);

  //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        btNmae = findViewById(R.id.bluetooth_name);

        data=findViewById(R.id.data);
        max_text=findViewById(R.id.max);
        min_text=findViewById(R.id.min);

        device = getIntent().getParcelableExtra("device");
        btNmae.setText(device.getName() + "  " + device.getAddress());
        connectDevice();

    }




    private Handler handler=new Handler() {
        public  void handleMessage(Message msg)
        {
            if(msg.what==DATA)
            {
                data.setText(s);
            }
            if(msg.what==MAX)
            {
                max_text.setText(max_string);
            }
            if(msg.what==MIN)
            {
                min_text.setText(min_string);
            }
        }
    };


    /**
     * 连接蓝牙
     */
    public void connectDevice() {
        showProgressDialog("正在连接蓝牙");
        new Thread() {
            public void run() {
                super.run();
                try {
                    bTSocket = device.createRfcommSocketToServiceRecord(UUID.fromString(MainActivity.MY_UUID)); //创建一个Socket连接：只需要服务器在注册时的UUID号
                    bTSocket.connect(); //连接
                    showToast("连接成功");
                    dismissProgressDialog();
                    Electrocardiogram electrocardiogram
                            = (Electrocardiogram) findViewById(R.id.electrocardiogram);

                    Canvas canvas=null;


//                    while(data_string)
//                    {
//                        String s = String.valueOf(num%10000/10);
//                        tv.setText(s);
//                        data_string=false;
//                    }

                    while (true) {
                        InputStream is = bTSocket.getInputStream();
                        byte[] buffer = new byte[1024 * 120];
                        int count = is.read(buffer);
                        string = new String(buffer, 0, 5, "utf-8");
                        //  tv_recv.append(string);
                        Log.e(TAG,string + "////////////////////////");
                        num = Integer.valueOf(string).intValue();
                        Log.e(TAG,num + "5555555555");
                        if((int)num/10000==1) {
                            electrocardiogram.getnum(num%10000);
                            i++;

                            s = String.valueOf((int)num%10000/10);
                            Message message =new Message();
                            message.what=DATA;
                            handler.sendMessage(message);
                            if(num%10000/10 > max)
                            {
                                Message message_max =new Message();
                                max=(int)num%10000/10;
                                max_string=String.valueOf(max);
                                message_max.what=MAX;
                                handler.sendMessage(message_max);
                            }
                            if(num%10000/10 < min)
                            {
                                Message message_min =new Message();
                                min=(int)num%10000/10;
                                min_string=String.valueOf(min);
                                message_min.what=MIN;
                                handler.sendMessage(message_min);
                            }
                        }

                    }

                } catch (IOException e) {
                    showToast("连接异常，请退出本界面再次尝试！");
                    dismissProgressDialog();
                    e.printStackTrace();
                }

            }


        }.start();
    }


 /*   public void getdata() {
        try {
            while (true) {

                float f = string;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
*/
}
