package com.bishe.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class FileUtil {
	private static String seperator = System.getProperty("file.separator");
	private static final SimpleDateFormat sDateFormat = new SimpleDateFormat(
			"yyyyMMddHHmmss"); // 时间格式化的格式
	private static final Random r = new Random();

	public static String getImgBasePath() {
		String os = System.getProperty("os.name");
		String basePath = "";
		if (os.toLowerCase().startsWith("win")) {
			basePath = "D:/bishe";
		} else {
			basePath = "/home/bishe";
		}
		basePath = basePath.replace("/", seperator);
		return basePath;
	}
	//得到文件的后缀如.jpg、.png 等
	public static String getFileExtension(MultipartFile cFile) {
		String originalFileName = cFile.getOriginalFilename();
		return originalFileName.substring(originalFileName.lastIndexOf("."));
	}

	//主题图片存储的相对路径
	public static String getThemeImageRelativePath() {
		String themeImageRelativePath = "/themeImage/images/";
		themeImageRelativePath = themeImageRelativePath.replace("/", seperator);
		return themeImageRelativePath;
	}

	//创建存储图片的文件夹
	public static File makeDirPath(String fileRealAddress) {
		File dirPath = new File(fileRealAddress);
		if (!dirPath.exists()) {
			dirPath.mkdirs();
		}
		return dirPath;
	}

	public static String getRandomFileName() {
		// 生成随机文件名：当前年月日时分秒+五位随机数（为了在实际项目中防止文件同名而进行的处理）
		int rannum = (int) (r.nextDouble() * (99999 - 10000 + 1)) + 10000; // 获取随机数
		String nowTimeStr = sDateFormat.format(new Date()); // 当前时间
		return nowTimeStr + rannum;
	}

	public static void deleteFile(String storePath) {
		File file = new File(getImgBasePath() + storePath);
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


	//主题图片预览存储的相对路径
	public static String getThemeImagePreviewRelativePath() {
		String themeImageRelativePath = "/themeImage/preview/";
		themeImageRelativePath = themeImageRelativePath.replace("/", seperator);
		return themeImageRelativePath;
	}

	//学生头像存储的相对路径
	public static String getStudentImageRelativePath() {
		String studentImageRelativePath = "/studentImage/";
		studentImageRelativePath = studentImageRelativePath.replace("/", seperator);
		return studentImageRelativePath;
	}



	public static String getHeadLineImagePath() {
		String headLineImagePath = "/upload/images/item/headtitle/";
		headLineImagePath = headLineImagePath.replace("/", seperator);
		return headLineImagePath;
	}

	public static String getShopCategoryImagePath() {
		String shopCategoryImagePath = "/upload/images/item/shopcategory/";
		shopCategoryImagePath = shopCategoryImagePath.replace("/", seperator);
		return shopCategoryImagePath;
	}
	
	public static String getPersonInfoImagePath() {
		String personInfoImagePath = "/upload/images/item/personinfo/";
		personInfoImagePath = personInfoImagePath.replace("/", seperator);
		return personInfoImagePath;
	}

	public static String getShopImagePath(long shopId) {
		StringBuilder shopImagePathBuilder = new StringBuilder();
		shopImagePathBuilder.append("/upload/images/item/shop/");
		shopImagePathBuilder.append(shopId);
		shopImagePathBuilder.append("/");
		String shopImagePath = shopImagePathBuilder.toString().replace("/",
				seperator);
		return shopImagePath;
	}





}
