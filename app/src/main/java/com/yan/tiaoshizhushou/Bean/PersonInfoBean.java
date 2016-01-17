package com.yan.tiaoshizhushou.Bean;


import com.yan.tiaoshizhushou.Utils.JsonResponseParser;

import org.xutils.http.annotation.HttpResponse;

/**
 * Created by a7501 on 2015/12/23.
 */
@HttpResponse(parser = JsonResponseParser.class)
public class PersonInfoBean {

    @Override
    public String toString() {
        return "PersonInfoBean{" +
                "token='" + token + '\'' +
                ", non_field_errors='" + non_field_errors + '\'' +
                '}';
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    private String token;
    private String non_field_errors;

    public String getNon_field_errors() {
        return non_field_errors;
    }

    public void setNon_field_errors(String non_field_errors) {
        this.non_field_errors = non_field_errors;
    }
}
