package com.yan.tiaoshizhushou.Utils;

import org.xutils.common.Callback;

/**
 * callBack类
 * Created by a7501 on 2015/12/23.
 */
public class MyCallBack<ResultType> implements Callback.CommonCallback<ResultType> {

    @Override
    public void onSuccess(ResultType result) {

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

}
