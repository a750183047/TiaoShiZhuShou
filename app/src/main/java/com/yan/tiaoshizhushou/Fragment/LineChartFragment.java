package com.yan.tiaoshizhushou.Fragment;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.PopupWindow;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.yan.tiaoshizhushou.Activity.MainActivity;
import com.yan.tiaoshizhushou.Bean.StringNum;
import com.yan.tiaoshizhushou.R;
import com.yan.tiaoshizhushou.Utils.StringToWhat;
import com.yan.tiaoshizhushou.Utils.ToastUtil;

import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;

/**
 * 数据折线图
 * Created by a7501 on 2016/1/24.
 */
public class LineChartFragment extends Fragment {

    private LineChart mChart;
    private Button start;
    private int xR = 0;
    private BluetoothSPP bluetoothSPP;
    private boolean isStartReceive = false;
    private boolean isNum1Show = false, isNum2Show = false, isNum3Show = false;
    private LineData lineData;
    private LineDataSet set, set2, set3;
    private CheckBox cbNum1;
    private CheckBox cbNum2;
    private CheckBox cbNum3;
    private Button btnMenu;
    private View contentView;

    private String path = "/TiaoShiZhuShou/image"; //存储地址
    private View dialogView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_line_chart_fragment, container, false);
        contentView = inflater.inflate(R.layout.popup_window_linechart_fragment,container,false);


        xR = 0;
        mChart = (LineChart) view.findViewById(R.id.chart);
        start = (Button) view.findViewById(R.id.btn_start);
        cbNum1 = (CheckBox) view.findViewById(R.id.cb_num1);
        cbNum2 = (CheckBox) view.findViewById(R.id.cb_num2);
        cbNum3 = (CheckBox) view.findViewById(R.id.cb_num3);
        btnMenu = (Button) view.findViewById(R.id.btn_chart_menu);

        mChart.setDescription("");
        mChart.setNoDataTextDescription("点击开始");  //中心黄子提示

        // 点击使能
        mChart.setTouchEnabled(true);

        // 拖拽和选取使能
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setDrawGridBackground(false);
        //双指缩放
        mChart.setPinchZoom(true);

        // set an alternative background color 背景颜色
        mChart.setBackgroundColor(Color.WHITE);

        LineData data = new LineData();
        data.setValueTextColor(Color.BLACK);

        // add empty data
        mChart.setData(data);
        mChartInit();
        Log.e("fragment", "onCreateView");

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("fragment", "onResume");
        bluetoothSPP = ((MainActivity) getActivity()).bluetoothSPP;

        /**
         * 开始按钮点击事件
         */
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isStartReceive) {
                    start.setText("停止");
                    isStartReceive = true;
                } else {
                    isStartReceive = false;
                    start.setText("开始");
                }
            }
        });

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindow(v);
            }
        });

        /**
         * 蓝牙接收监听
         */
        bluetoothSPP.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() {
            @Override
            public void onDataReceived(byte[] data, String message) {
                StringNum stringNum;
                if (isStartReceive) {
                    stringNum = StringToWhat.stringToFloats(message);
                    if (isNum1Show || isNum2Show || isNum3Show) {
                        xR++;  //横坐标数值
                        lineData.addXValue(String.valueOf(xR));
                    }
                    if (isNum1Show) {
                        addEntry(stringNum.getNum1());
                    }
                    if (isNum2Show) {
                        addEntry2(stringNum.getNum2());
                    }
                    if (isNum3Show) {
                        addEntry3(stringNum.getNum3());
                    }


                }


            }
        });

        /**
         * checkBox监听
         */
        cbNum1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isNum1Show = isChecked;
            }
        });

        cbNum2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isNum2Show = isChecked;
            }
        });
        cbNum3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isNum3Show = isChecked;
            }
        });
    }

    /**
     * 弹出popupWindow
     * @param v
     */
    @TargetApi(Build.VERSION_CODES.M)
    private void showPopupWindow(View v) {
        // 一个自定义的布局，作为显示的内容

        Button saveToSD = (Button) contentView.findViewById(R.id.btn_save);
        Button saveValue = (Button) contentView.findViewById(R.id.btn_save_value);
        saveToSD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveToSd();

            }
        });

        saveValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showToast(getActivity(),"还没写出来呢，不要急");
            }
        });

        final PopupWindow popupWindow = new PopupWindow(contentView,
                AbsListView.LayoutParams.WRAP_CONTENT, AbsListView.LayoutParams.WRAP_CONTENT, true);

        popupWindow.setTouchable(true);

        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });

        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_corners_bg));

        // 设置好参数之后再show
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, location[0] - 30, location[1] - 150);



    }

    /**
     * 保存到SD卡
     */
    private void saveToSd() {
        dialogView = View.inflate(getActivity(),R.layout.dialog_line_chart_save_image,null);
        final EditText etName = (EditText) dialogView.findViewById(R.id.et_name);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = etName.getText().toString();
                if (name.equals("")){
                    Long tsLong = System.currentTimeMillis()/1000;
                    name = tsLong.toString();
                }
                if (mChart.saveToPath(name, path)) {
                    ToastUtil.showToast(getActivity(),"保存成功");
                } else {
                    ToastUtil.showToast(getActivity(), "出了点问题啊");
                }

            }
        });
        final AlertDialog dialog = builder.create();
        dialog.setTitle("起个名字吧");
        dialog.setView(dialogView);

        dialog.show();

    }

    /**
     * 表格初始化
     */
    private void mChartInit() {
        // get the legend (only possible after setting data)
        Legend l = mChart.getLegend();
        // modify the legend ...
        // l.setPosition(LegendPosition.LEFT_OF_CHART);
        l.setForm(Legend.LegendForm.LINE);
        l.setTextColor(Color.BLACK);

        XAxis xl = mChart.getXAxis();
        xl.setTextColor(Color.BLACK);
        xl.setDrawGridLines(false);
        xl.setAvoidFirstLastClipping(true);
        xl.setSpaceBetweenLabels(5);
        xl.setEnabled(true);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setTextColor(Color.BLACK);
        // leftAxis.setAxisMaxValue(100f);
        // leftAxis.setAxisMinValue(0f);
        leftAxis.setStartAtZero(false);
        leftAxis.setDrawGridLines(true);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);

        lineData = mChart.getData();

        isStartReceive = false;

        set = createSet2();
        set2 = createSet();
        set3 = createSet3();
        lineData.addDataSet(set);
        lineData.addDataSet(set2);
        lineData.addDataSet(set3);


    }


    /***
     * 添加一个数据
     *
     * @param yValue
     */
    private void addEntry(float yValue) {

        // add a new x-value first
        lineData.addEntry(new Entry(yValue, xR), 0);

        // let the chart know it's data has changed
        mChart.notifyDataSetChanged();

        // limit the number of visible entries 允许当页显示的数据数量

        mChart.setVisibleXRangeMaximum(100);
        // mChart.setVisibleYRange(30, AxisDependency.LEFT);

        // move to the latest entry
        mChart.moveViewToX(lineData.getXValCount() - 101);

        // this automatically refreshes the chart (calls invalidate())
        // mChart.moveViewTo(data.getXValCount()-7, 55f,
        // AxisDependency.LEFT);

    }

    /***
     * 添加一个数据
     *
     * @param yValue
     */
    private void addEntry2(float yValue) {


        lineData.addEntry(new Entry(yValue, xR), 1);

        // let the chart know it's data has changed
        mChart.notifyDataSetChanged();

        // limit the number of visible entries 允许当页显示的数据数量

        mChart.setVisibleXRangeMaximum(100);
        // mChart.setVisibleYRange(30, AxisDependency.LEFT);

        // move to the latest entry
        mChart.moveViewToX(lineData.getXValCount() - 101);

        // this automatically refreshes the chart (calls invalidate())
        // mChart.moveViewTo(data.getXValCount()-7, 55f,
        // AxisDependency.LEFT);

    }

    /***
     * 添加一个数据
     *
     * @param yValue
     */
    private void addEntry3(float yValue) {


        lineData.addEntry(new Entry(yValue, xR), 2);

        // let the chart know it's data has changed
        mChart.notifyDataSetChanged();

        // limit the number of visible entries 允许当页显示的数据数量

        mChart.setVisibleXRangeMaximum(100);
        // mChart.setVisibleYRange(30, AxisDependency.LEFT);

        // move to the latest entry
        mChart.moveViewToX(lineData.getXValCount() - 101);

        // this automatically refreshes the chart (calls invalidate())
        // mChart.moveViewTo(data.getXValCount()-7, 55f,
        // AxisDependency.LEFT);

    }

    /**
     * 创建一个数据的格式
     *
     * @return
     */
    private LineDataSet createSet() {

        LineDataSet set = new LineDataSet(null, "测试数据1");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(Color.RED);
        set.setCircleColor(Color.LTGRAY);
        set.setLineWidth(2f);
        set.setFillAlpha(65);
        set.setFillColor(ColorTemplate.getHoloBlue());
        set.setHighLightColor(Color.rgb(244, 117, 117));
        set.setValueTextColor(Color.RED);
        set.setValueTextSize(9f);
        set.setDrawValues(false);
        set.setDrawCircles(false);
        set.setDrawCubic(false);
        return set;
    }

    /**
     * 创建第二个数据的格式
     *
     * @return
     */
    private LineDataSet createSet2() {

        LineDataSet set = new LineDataSet(null, "测试数据2");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(ColorTemplate.getHoloBlue());
        set.setCircleColor(Color.LTGRAY);
        set.setLineWidth(2f);
        set.setFillAlpha(65);
        set.setFillColor(ColorTemplate.getHoloBlue());
        set.setHighLightColor(Color.rgb(244, 117, 117));
        set.setValueTextColor(Color.BLUE);
        set.setValueTextSize(9f);
        set.setDrawValues(false);
        set.setDrawCircles(false);
        set.setDrawCubic(false);
        return set;
    }

    /**
     * 创建第三个数据的格式
     *
     * @return
     */
    private LineDataSet createSet3() {

        LineDataSet set = new LineDataSet(null, "测试数据3");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(Color.GREEN);
        set.setCircleColor(Color.LTGRAY);
        set.setLineWidth(2f);
        set.setFillAlpha(65);
        set.setFillColor(ColorTemplate.getHoloBlue());
        set.setHighLightColor(Color.rgb(244, 117, 117));
        set.setValueTextColor(Color.GREEN);
        set.setValueTextSize(9f);
        set.setDrawValues(false);
        set.setDrawCircles(false);
        set.setDrawCubic(false);
        return set;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mChart.clearValues();
        mChart.clearAllJobs();
    }

}
