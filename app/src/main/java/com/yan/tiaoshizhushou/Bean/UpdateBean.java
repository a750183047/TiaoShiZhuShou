package com.yan.tiaoshizhushou.Bean;

import com.yan.tiaoshizhushou.Utils.JsonResponseParser;

import org.xutils.http.annotation.HttpResponse;

/**
 * Created by a7501 on 2016/1/17.
 */
@HttpResponse(parser = JsonResponseParser.class)
public class UpdateBean  {
    private String versionName;
    private String versionCode;
    private String description;
    private String downloadUrl;

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    @Override
    public String toString() {
        return "UpdateBean{" +
                "versionName='" + versionName + '\'' +
                ", versionCode='" + versionCode + '\'' +
                ", description='" + description + '\'' +
                ", downloadUrl='" + downloadUrl + '\'' +
                '}';
    }
}
