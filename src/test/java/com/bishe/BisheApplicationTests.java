package com.bishe;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import com.bishe.component.FileUploadProperties;
import com.bishe.dao.StudPublishDao;
import com.bishe.pojo.Activity;
import com.bishe.pojo.Student;
import com.bishe.utils.FileUtil;
import com.bishe.utils.ImageUtil;
import com.bishe.utils.MyBeanUtil;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BisheApplicationTests {

	@Autowired FileUploadProperties  fileUploadProperties;
	@Autowired
	StudPublishDao studPublishDao;
	Logger log= LoggerFactory.getLogger(FileUploadProperties.class);
	@Test
	public void contextLoads() {
		log.info("静态资源"+fileUploadProperties.getStaticAccessPath()+"路径"+fileUploadProperties.getUploadFolder());
		log.info(("资源完整路径"+fileUploadProperties.getUploadFolder()+ FileUtil.getThemeImageRelativePath()));
	}
	@Test
	public  void testStudPublishActivity(){
		Activity activity = new Activity();
		activity.setActiId("adhhfjfjkwkfj");
		studPublishDao.studPublishActivity(activity);
	}

	//亮点
	@Test
	public void  testMyBeanUtil(){
		Student sourceModel = new Student(); // 第一个对象
		Student targetModel = new Student(); // 第二个model对象

		sourceModel.setStudId("学生Id585366");
		sourceModel.setStudClassID(12345688);

		targetModel.setStudPhone("电话理论12345");
		targetModel.setStudNumber("学号1988855");

		Student my = (Student) MyBeanUtil.combineSydwCore(sourceModel, targetModel);
		System.out.print(my);
	}
	//测试使用自己写的工具类删除图片




}

