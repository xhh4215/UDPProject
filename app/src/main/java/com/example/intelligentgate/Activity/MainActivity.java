package com.example.intelligentgate.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.intelligentgate.Client.UDPClient;
import com.example.intelligentgate.R;
import com.example.intelligentgate.UDPConfig.UDPResource;
import com.example.intelligentgate.Utils.StringToByte;
import com.example.intelligentgate.Utils.ThreadUtil;
import com.example.intelligentgate.bean.MessageEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static String TAG = "MainActivity";
    private Button open_door;
    private Button close_door;
    private Button stop_door;
    private Button all_open_door;
    private Button all_close_door;
    private Button all_stop_door;
    private Button up_lift;
    private Button stop_lift;
    private Button down_lift;
    private Button all_up_lift;
    private Button all_down_lift;
    private Button all_stop_lift;
    private UDPClient udpClient;
    private ImageView textView_door_one;
    private ImageView textView_door_two;
    private ImageView textView_door_three;
    private ImageView textView_lift_one;
    private ImageView textView_lift_two;
    private ImageView textView_lift_three;
    private boolean isOpen = false;
    private static final String switch_close = "4d7950617261000001004c52434e494f31363034436f6e66696744617400ca0388131a000100000000000000000000000000000000000000000000000000000000000000000000000000000000000000";
    private static final String switch_open = "4d7950617261000001004c52434e494f31363034436f6e66696744617400ca0388131a000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_two);
        udpClient = new UDPClient(this);
        ThreadUtil.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                udpClient.receiveUDPData(8705);
            }
        });
        textView_door_one = findViewById(R.id.textView_one_door);
        textView_door_two = findViewById(R.id.textView_two_door);
        textView_door_three = findViewById(R.id.textView_three_door);
        textView_lift_one = findViewById(R.id.textView_one_lift);
        textView_lift_two = findViewById(R.id.textView_two_lift);
        textView_lift_three = findViewById(R.id.textView_three_lift);
        open_door = findViewById(R.id.open_door);
        open_door.setOnClickListener(this);
        close_door = findViewById(R.id.close_door);
        close_door.setOnClickListener(this);
        stop_door = findViewById(R.id.stop_door);
        stop_door.setOnClickListener(this);
        all_open_door = findViewById(R.id.all_open_door);
        all_open_door.setOnClickListener(this);
        all_close_door = findViewById(R.id.all_close_door);
        all_close_door.setOnClickListener(this);
        all_stop_door = findViewById(R.id.all_stop_door);
        all_stop_door.setOnClickListener(this);
        up_lift = findViewById(R.id.up_lift);
        up_lift.setOnClickListener(this);
        down_lift = findViewById(R.id.down_lift);
        down_lift.setOnClickListener(this);
        stop_lift = findViewById(R.id.stop_lift);
        stop_lift.setOnClickListener(this);
        all_up_lift = findViewById(R.id.all_up_lift);
        all_up_lift.setOnClickListener(this);
        all_down_lift = findViewById(R.id.all_down_lift);
        all_down_lift.setOnClickListener(this);
        all_stop_lift = findViewById(R.id.all_stop_lift);
        all_stop_lift.setOnClickListener(this);


    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);

    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        Log.d(TAG, "我已经接收到数据");
        switch (event.getMessage()) {
            case (switch_open):
                // todo 点击第一个串口的处理
//                Toast.makeText(MainActivity.this, "门以开", Toast.LENGTH_LONG).show();
                isOpen = true;
                close_door.setEnabled(true);
                open_door.setEnabled(false);
                textView_door_one.setBackground(getDrawable(R.mipmap.open));
                break;
            case (switch_close):
//                Toast.makeText(MainActivity.this, "门以关", Toast.LENGTH_LONG).show();
                isOpen = false;
                close_door.setEnabled(false);
                open_door.setEnabled(true);
                textView_door_one.setBackground(getDrawable(R.mipmap.close));

                break;
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.open_door:
                ThreadUtil.getInstance().execute(new Runnable() {
                    @Override
                    public void run() {
                        udpClient.sendUDPData(UDPResource.open_door_one, UDPResource.port, UDPResource.ip_door);
                    }
                });
                textView_door_one.setBackground(getDrawable(R.mipmap.open));
                isOpen = true;
                close_door.setEnabled(true);
                open_door.setEnabled(false);
                break;
            case R.id.close_door:
                ThreadUtil.getInstance().execute(new Runnable() {
                    @Override
                    public void run() {
                        udpClient.sendUDPData(UDPResource.close_door_one, UDPResource.port, UDPResource.ip_door);

                    }
                });
                textView_door_one.setBackground(getDrawable(R.mipmap.close));
                isOpen = false;
                close_door.setEnabled(false);
                open_door.setEnabled(true);
                break;
            case R.id.stop_door:
                ThreadUtil.getInstance().execute(new Runnable() {
                    @Override
                    public void run() {
                        udpClient.sendUDPData(UDPResource.stop_door_one, UDPResource.port, UDPResource.ip_door);

                    }
                });
                if (isOpen) {
                    textView_door_one.setBackground(getDrawable(R.mipmap.open));


                } else {
                    textView_door_one.setBackground(getDrawable(R.mipmap.close));


                }
                break;
            case R.id.all_open_door:

                textView_door_one.setBackground(getDrawable(R.mipmap.open));
                textView_door_two.setBackground(getDrawable(R.mipmap.open));
                textView_door_three.setBackground(getDrawable(R.mipmap.open));

                break;
            case R.id.all_close_door:
                textView_door_one.setBackground(getDrawable(R.mipmap.close));
                textView_door_two.setBackground(getDrawable(R.mipmap.close));
                textView_door_three.setBackground(getDrawable(R.mipmap.close));


                break;
            case R.id.all_stop_door:
                if (isOpen) {
                    textView_door_one.setBackground(getDrawable(R.mipmap.open));
                    textView_door_two.setBackground(getDrawable(R.mipmap.open));
                    textView_door_three.setBackground(getDrawable(R.mipmap.open));

                } else {
                    textView_door_one.setBackground(getDrawable(R.mipmap.close));
                    textView_door_two.setBackground(getDrawable(R.mipmap.close));
                    textView_door_three.setBackground(getDrawable(R.mipmap.close));
                }
                break;
            case R.id.up_lift:
                ThreadUtil.getInstance().execute(new Runnable() {
                    @Override
                    public void run() {
                        udpClient.sendUDPData(UDPResource.open_door_one, UDPResource.port, UDPResource.ip_lift);

                    }
                });
//                Toast.makeText(this, "上升", Toast.LENGTH_LONG).show();
                textView_lift_one.setBackground(getDrawable(R.mipmap.open));
                isOpen = true;
                close_door.setEnabled(true);
                open_door.setEnabled(false);


                break;
            case R.id.down_lift:
                ThreadUtil.getInstance().execute(new Runnable() {
                    @Override
                    public void run() {
                        udpClient.sendUDPData(UDPResource.close_door_one, UDPResource.port, UDPResource.ip_lift);

                    }
                });
                textView_lift_one.setBackground(getDrawable(R.mipmap.close));
                isOpen = false;
                close_door.setEnabled(false);
                open_door.setEnabled(true);


                break;
            case R.id.stop_lift:
                ThreadUtil.getInstance().execute(new Runnable() {
                    @Override
                    public void run() {
                        udpClient.sendUDPData(UDPResource.stop_door_one, UDPResource.port, UDPResource.ip_lift);

                    }
                });
                if (isOpen) {
                    textView_lift_one.setBackground(getDrawable(R.mipmap.open));

                } else {
                    textView_lift_one.setBackground(getDrawable(R.mipmap.close));

                }
                break;
            case R.id.all_up_lift:
                textView_lift_one.setBackground(getDrawable(R.mipmap.open));
                textView_lift_two.setBackground(getDrawable(R.mipmap.open));
                textView_lift_three.setBackground(getDrawable(R.mipmap.open));

                break;
            case R.id.all_down_lift:
                textView_lift_one.setBackground(getDrawable(R.mipmap.close));
                textView_lift_two.setBackground(getDrawable(R.mipmap.close));
                textView_lift_three.setBackground(getDrawable(R.mipmap.close));

                break;
            case R.id.all_stop_lift:
                if (isOpen) {
                    textView_door_one.setBackground(getDrawable(R.mipmap.open));
                    textView_door_two.setBackground(getDrawable(R.mipmap.open));
                    textView_door_three.setBackground(getDrawable(R.mipmap.open));

                } else {
                    textView_door_one.setBackground(getDrawable(R.mipmap.close));
                    textView_door_two.setBackground(getDrawable(R.mipmap.close));
                    textView_door_three.setBackground(getDrawable(R.mipmap.close));
                }
                break;
            default:
                break;
        }
    }

}
