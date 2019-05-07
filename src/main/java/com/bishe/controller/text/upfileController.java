package com.bishe.controller.text;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;

/**
 * Created by ZYL on 2019/1/14.
 */
@Controller
@RequestMapping("/file")
public class upfileController {

    @RequestMapping(value = "/upfile")
    @ResponseBody
    public String upfile(MultipartFile upfile, HttpSession session, HttpServletRequest  request, HttpServletResponse response) {




        /*
		 * 测试信息
		 */
        System.out.println("upfile执行了...");

        System.out.println(upfile.getContentType());

        System.out.println(upfile.getOriginalFilename());

		/*
		 * 在MultipartFile接口中定义了如下很多有用的方法。
		 *
		 * 使用getSize()方法获得文件长度，以此决定允许上传的文件大小。
		 *
		 * 使用isEmpty()方法判断上传文件是否为空文件，以此决定是否拒绝空文件。
		 *
		 * 使用getInputStream()方法将文件读取为java.io.InputStream流对象。
		 *
		 * 使用getContentType()方法获得文件类型，以此决定允许上传的文件类型。
		 *
		 * 使用transferTo（）方法将上传文件写到服务器上指定的文件。
		 */

        // 获取上传的文件名

        String fileName = upfile.getOriginalFilename();
        System.out.println("发布路径："+request.getSession().getServletContext().getRealPath("/cache"));
        String root_path = session.getServletContext().getRealPath("cache");// 获取自定义缓存文件夹路径

        File file = new File(root_path);
        // 如果文件夹不存在，则创建
        if (!file.exists()) {
            file.mkdirs();
        }
        // 构建图片url路径，为显示图片做准备
        String url_root = session.getServletContext().getContextPath();

        String file_url = url_root + "/cache/" + fileName;

        try {
            File file2 = new File(file, upfile.getOriginalFilename());
            upfile.transferTo(file2);// 将上传的文件移动到指定文件夹
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //重新给session赋值,提交修改信息表单后才提交
		/*User seUser=(User)request.getSession().getAttribute("userObj");
		seUser.setPicUrl("cache/" + fileName);
		session= request.getSession();
		session.setAttribute("userObj", seUser);*/
        System.out.println(file_url);
        return file_url;// 返回图片url给ajax
    }

}
