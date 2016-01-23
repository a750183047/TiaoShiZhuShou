package com.yan.tiaoshizhushou.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.yan.tiaoshizhushou.Fragment.FirstStopFragment;
import com.yan.tiaoshizhushou.Fragment.FragmentIndex;
import com.yan.tiaoshizhushou.R;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;
import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;

public class MainActivity extends MaterialNavigationDrawer {

    public BluetoothSPP bluetoothSPP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bluetoothSPP = new BluetoothSPP(MainActivity.this);
        bluetoothSPP.setupService();
        bluetoothSPP.startService(BluetoothState.DEVICE_OTHER);
        Intent intent = new Intent(MainActivity.this, DeviceList.class);
        startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
    }

    @Override
    public void init(Bundle savedInstanceState) {
        setDrawerHeaderImage(R.drawable.title);


        // create sections
        this.addSection(newSection("一键停车", R.drawable.apps_home, new FirstStopFragment()));

        // create bottom section
        this.addBottomSection(newSection("设置中心", R.drawable.ic_settings_black_24dp, new FragmentIndex(bluetoothSPP)));
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) {
            Log.e("main", "1");
            if(resultCode == Activity.RESULT_OK){
                Log.e("main", "1.1");
                if (data !=null){
                    bluetoothSPP.connect(data);
                }
            }else {
                showBluetoothDialog();
            }
        } else if(requestCode == BluetoothState.REQUEST_ENABLE_BT) {
            Log.e("main","2");
            if(resultCode == Activity.RESULT_OK) {
                bluetoothSPP.setupService();
                bluetoothSPP.startService(BluetoothState.DEVICE_OTHER);
            } else {
                Log.e("main","3");
                showBluetoothDialog();
            }
        }
    }

    /**
     * 弹出蓝牙提示框
     */
    private void showBluetoothDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("蓝牙错误");
        builder.setMessage("为什么不选择一个蓝牙设备呢？！");
        builder.setNegativeButton("再给次机会吧", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getApplicationContext(), DeviceList.class);
                startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
            }
        });
        builder.setPositiveButton("就是任性", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                finish();
            }
        });
        builder.show();
    }

}
