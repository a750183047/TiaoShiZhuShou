package com.yan.tiaoshizhushou.Utils;

import com.google.gson.Gson;

import org.xutils.http.app.ResponseParser;
import org.xutils.http.request.UriRequest;

import java.lang.reflect.Type;

/**Json数据转换
 * Created by a7501 on 2015/12/23.
 */
public class JsonResponseParser  implements ResponseParser {
    //检查服务器返回的响应头信息
    @Override
    public void checkResponse(UriRequest request) throws Throwable {
    }

    /**
     * 转换result为resultType类型的对象
     *
     * @param resultType  返回值类型(可能带有泛型信息)
     * @param resultClass 返回值类型
     * @param result      字符串数据
     * @return
     * @throws Throwable
     */
    @Override
    public Object parse(Type resultType, Class<?> resultClass, String result) throws Throwable {

        return new Gson().fromJson(result, resultClass);

    }
}
