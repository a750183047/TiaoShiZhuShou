package com.yan.tiaoshizhushou.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.yan.tiaoshizhushou.Activity.MainActivity;
import com.yan.tiaoshizhushou.R;
import com.yan.tiaoshizhushou.Utils.ToastUtil;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;

/**
 * 一键停车
 * Created by a7501 on 2016/1/21.
 */
public class FirstStopFragment extends Fragment {

    private View view;
    private Button stopButton;
    private BluetoothSPP bluetoothSPP;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_first_stop_fragment, container, false);
        stopButton = (Button) view.findViewById(R.id.button_stop);

        return view;
    }


    /***
     * 1.Fragment中通过getActivity()然后进行强制转化，调用Activity中的公有方法
     *
     *    ((XXXXActivity)getActivity()).fun();
     */
    @Override
    public void onResume() {
        super.onResume();
        bluetoothSPP = ((MainActivity)getActivity()).bluetoothSPP;
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stopButton.getText().toString().equals("停车")) {
                    stopButton.setBackgroundResource(R.drawable.button_selector_green);
                    stopButton.setText("启动");

                    //发送停车指令
                    bluetoothSPP.send("go", true);

                } else if (stopButton.getText().toString().equals("启动")) {
                    stopButton.setBackgroundResource(R.drawable.button_selector_red);
                    stopButton.setText("停车");
                    bluetoothSPP.send(new byte[]{'s','s','w','\r','\n'}, false);

                    //发送启动指令

                }
            }
        });

    }



}
