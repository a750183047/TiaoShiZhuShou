package com.yan.tiaoshizhushou.Utils;

import com.yan.tiaoshizhushou.Bean.StringNum;

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

    public static StringNum stringToFloats(String s){
        StringNum stringNum = new StringNum();

        try {
            String num[] = s.split(":");
            stringNum.setNum1(stringToFloat(num[0]));
            stringNum.setNum2(stringToFloat(num[1]));
            stringNum.setNum3(stringToFloat(num[2]));
        }catch (Exception e){
            stringNum.setNum1(0);
            stringNum.setNum2(0);
            stringNum.setNum3(0);
        }

        return stringNum;
    }
}
