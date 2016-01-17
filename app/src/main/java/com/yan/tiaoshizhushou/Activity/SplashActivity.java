package com.yan.tiaoshizhushou.Activity;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.yan.tiaoshizhushou.Bean.PersonInfoBean;
import com.yan.tiaoshizhushou.Bean.UpdateBean;
import com.yan.tiaoshizhushou.R;
import com.yan.tiaoshizhushou.Utils.MyCallBack;
import com.yan.tiaoshizhushou.Utils.XUtil;

import org.xutils.common.Callback;
import org.xutils.x;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class SplashActivity extends AppCompatActivity {

    static final String UPDATEURL = "http://qingxinchengming.cn/appweb/tiaoshizhushou/update.json";
    static final String DOWNLOADURL = "http://qingxinchengming.cn/appweb/tiaoshizhushou/update.apk";
    private UpdateBean updateBean;
    private PackageInfo packageInfo;
    private int versionCode;
    private String versionName;
    private TextView version;
    private String descriptionFromInternet;
    private ProgressBar downloadProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.Ext.init(getApplication());  //初始化 xUtils
        x.Ext.setDebug(true); // 是否输出debug日志
        x.view().inject(this);
        //初始化View
        initView();




    }

    /**
     * 为什么把检查更新放在这里？
     * http://stackoverflow.com/questions/21947675/android-4-4-2-java-lang-runtimeexception-performing-stop-of-activity-that-is
     * 生命周期
     * onCreate
     * onStart
     * onResume
     *
     * onPause
     * onStop
     * onDestroy
     */
    @Override
    protected void onResume() {
        super.onResume();
        //检查更新
        update();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    /**
     * 检查更新消息
     */
    private void update() {

        //获取版本号等信息
        PackageManager packageManager = getPackageManager();  //得到包的信息
        try {
            packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            versionCode = packageInfo.versionCode;
            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        version.setText("版本号：" + versionName);

        updateBean = new UpdateBean();

        Map<String, String> map = new HashMap<>();
        XUtil.Get(UPDATEURL, map, new MyCallBack<UpdateBean>() {

            @Override
            public void onSuccess(UpdateBean result) {
                super.onSuccess(result);
                updateBean = result;
                descriptionFromInternet = updateBean.getDescription();
                if (Integer.parseInt(updateBean.getVersionCode()) > versionCode) {

                    showUpdateDialog();
                } else {
                    enterHomeActivity();
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                Log.e("result", "getErr");


            }

        });
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void initView() {
        setContentView(R.layout.activity_splash_activity);
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        version = (TextView) findViewById(R.id.text_version);
        downloadProgress = (ProgressBar) findViewById(R.id.progressBarDownload);
    }

    /**
     * 显示提示框
     */
    private void showUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("发现更新");
        builder.setMessage(descriptionFromInternet);
        builder.setNegativeButton("立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "正在下载更新", Toast.LENGTH_SHORT).show();
                downloadProgress.setVisibility(View.VISIBLE);
                downloadUpdate();
            }
        });
        builder.setPositiveButton("下次再说", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                enterHomeActivity();  //跳转到主界面
            }
        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                enterHomeActivity();
            }
        });
        builder.show();
    }

    /**
     * 跳转到主界面
     */
    private void enterHomeActivity() {

        startActivity(new Intent(this,MainActivity.class));
    }

    /**
     * 下载更新软件
     */
    private void downloadUpdate() {
        String path = Environment.getExternalStorageDirectory() + "/TiaoShiZhuShou/update.apk"; //存储地址
        XUtil.DownLoadFile(DOWNLOADURL, path, new Callback.ProgressCallback<File>() {
            @Override
            public void onWaiting() {

            }

            @Override
            public void onStarted() {

            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                downloadProgress.setProgress((int) (current * 100 / total));

            }

            @Override
            public void onSuccess(File result) {
                //跳转到下载页面
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setDataAndType(Uri.fromFile(result),
                        "application/vnd.android.package-archive");
                startActivityForResult(intent, 0);   //如果取消安装，会返回结果，调用 onActivityResult方法
                Log.e("MainActivity", "下载成功");

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });


    }
}
