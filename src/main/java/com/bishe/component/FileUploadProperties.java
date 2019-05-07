package com.bishe.component;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by ZYL on 2019/1/17.
 */
/*
从项目外的文件夹中取出图片配置
 */
@Component
@ConfigurationProperties(prefix = "file")
public class FileUploadProperties {


    // /静态资源对外暴露的访问路径
    private String staticAccessPath;

    //文件上传目录
    private String uploadFolder ;

    public  String getStaticAccessPath() {
        return staticAccessPath;
    }

    public  void setStaticAccessPath(String staticAccessPath) {
        this.staticAccessPath = staticAccessPath;
    }

    public   String getUploadFolder() {
        return uploadFolder;
    }

    public void setUploadFolder(String uploadFolder) {
        this.uploadFolder = uploadFolder;
    }
}


