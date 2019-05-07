package com.bishe.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import com.bishe.interceptor.UserLoginInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

public class ImageUtil {


	//提供要存储的图片和存储的相对地址，返回由虚拟路径组成的项目可以访问的图片地址，但要删除图片此返回路径需要去掉"/upload"
	public static String myStoreStudentImage(MultipartFile imageFile,String realtivePath){
        //图片存储的绝对路径的文件夹=图片存储的根目录(根据系统不同而不同)+图片存储的相对路径
        String previewFilePath = FileUtil.getImgBasePath() + realtivePath;
        //创建图片存储的绝对路径的文件夹
        File fileTruePath = FileUtil.makeDirPath(previewFilePath);
        //图片新名字=随机数(当前时间+五位数字=随机数)+图像的后缀
        String newName = FileUtil.getRandomFileName() + FileUtil.getFileExtension(imageFile);

        // 将图片存储到图片服务器的绝对路径的文件夹下
        //但是如何将这些存储的图片删除掉？？？
        try {
            File file = new File(fileTruePath, newName);
            imageFile.transferTo(file);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //取出图片的路径即：根目录虚拟映射的路径+相对路径+文件名
        String virtualPath="/upload"+FileUtil.getStudentImageRelativePath()+newName;
       return virtualPath;
    }


    //提供由虚拟路径映射的地址，去掉"/upload"变成存储的地址，然后进行删除
    public static void myDeleteImage(String virtualPath) {
	    System.out.print("传来的数据库路径virtualPath"+virtualPath);

	    String storePath=virtualPath.replace("/upload","");
        System.out.print("storePath"+storePath);
        File file = new File(FileUtil.getImgBasePath() + storePath);
        System.out.print("file的决定路径"+file.getAbsolutePath());
        if (file.exists()) {
            if (file.isDirectory()) {
                File files[] = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    files[i].delete();
                }
            }
            file.delete();
        }
    }


    public static String myStoreActivityThemeImage(MultipartFile imageFile,String realtivePath){
        //图片存储的绝对路径的文件夹=图片存储的根目录(根据系统不同而不同)+图片存储的相对路径
        String previewFilePath = FileUtil.getImgBasePath() + realtivePath;
        //创建图片存储的绝对路径的文件夹
        File fileTruePath = FileUtil.makeDirPath(previewFilePath);
        //图片新名字=随机数(当前时间+五位数字=随机数)+图像的后缀
        String newName = FileUtil.getRandomFileName() + FileUtil.getFileExtension(imageFile);

        // 将图片存储到图片服务器的绝对路径的文件夹下
        //但是如何将这些存储的图片删除掉？？？
        try {
            File file = new File(fileTruePath, newName);
            imageFile.transferTo(file);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //取出图片的路径即：根目录虚拟映射的路径+相对路径+文件名
        String virtualPath="/upload"+FileUtil.getThemeImageRelativePath()+newName;
        return virtualPath;
    }


   }
