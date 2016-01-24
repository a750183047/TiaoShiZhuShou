package com.yan.tiaoshizhushou.Utils;

/**
 * Created by a7501 on 2016/1/24.
 */
public class StringToWhat {

    private static float value;

    public static float stringToFloat(String s){
        float lastValue = 0;
        try {
            value = Float.valueOf(s);
            lastValue = value;
        }catch (Exception e){
            return lastValue;
        }

        return value;
    }
}
