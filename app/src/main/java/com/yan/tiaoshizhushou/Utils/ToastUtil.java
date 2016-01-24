package com.yan.tiaoshizhushou.Utils;

import android.app.Activity;
import android.widget.Toast;

/**
 * Created by a7501 on 2016/1/23.
 */
public class ToastUtil {

    /**
     * 弹出提示
     * Created by a7501 on 2015/11/24.
     */

    public static void showToast(final Activity context, final String string) {
        if ("main".equals(Thread.currentThread().getName())) { //判断是否为主线程
            Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
        } else {
            context.runOnUiThread(new Runnable() { //如果不是，就用该方法使其在ui线程中运行
                @Override
                public void run() {
                    Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

}
