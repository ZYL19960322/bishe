package com.bishe.utils.Excel;

import com.bishe.dto.ExcelExportStudent;
import com.bishe.pojo.Activity;
import com.bishe.pojo.Admin;
import com.bishe.pojo.Student;
import com.bishe.utils.TimeUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.util.CellRangeAddress;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.List;

/**
 * Created by ZYL on 2019/3/7.
 */
public class MyExportExcelUtil {
    public static void myExportExcelMethod(List<ExcelExportStudent> exportStudentList, Activity activity, HttpServletRequest request, HttpServletResponse response, int pageCount )throws Exception {
        //每张表打印的数据条数(这里第一张sheet表最多可以打印35条数据(由于有标题)，其他的sheet1表最多可显示38条)
        // 所以需要对每页sheet显示数据条数的参数pageCount进行校验(如果pageCount为0时,则程序无响应)
        if( pageCount==0 ||pageCount>35)
        //pageCount不符合规定时，则设置为默认值35;
        { pageCount=35;}

        //创建excel表
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet();
        wb.setSheetName(0,activity.getActiTheme()+"报名表1");

        //创建excel的样式工具类
        ExcelStyleUtil styleUtil = new ExcelStyleUtil(wb);
        //这里的short型的参数值貌似没什么区别
        //(单元格)大标题样式
        CellStyle headerStyle = styleUtil.getHeaderStyle((short) 11);
        //每列(单元格)标题样式
        CellStyle titleStyle = styleUtil.getTitleStyle((short) 12);
        //(单元格)数据行样式
        CellStyle styles = styleUtil.initStyles(wb);

        //创建大标题
        HSSFRow row = sheet.createRow(0);//创建第一个单元格
        //设置单元格高度
        row.setHeight((short) (26.25 * 20));
        HSSFCell cell = row.createCell(0);//为第一行单元格设值
        cell.setCellValue(activity.getActiTheme() + "报名表");
        cell.setCellStyle(headerStyle);

        HSSFCell cell2 = row.createCell(1);
        cell2.setCellStyle(headerStyle);
        HSSFCell cell3 = row.createCell(2);
        cell3.setCellStyle(headerStyle);

        HSSFCell cell4 = row.createCell(3);
        cell4.setCellStyle(headerStyle);
        HSSFCell cell5 = row.createCell(4);
        cell5.setCellStyle(headerStyle);
        HSSFCell cell6 = row.createCell(5);
        cell6.setCellStyle(headerStyle);

        //合并单元格，将大标题的几个单元格合并成一个单元格
        CellRangeAddress rowRegion = new CellRangeAddress(0, 0, 0, 5);
        sheet.addMergedRegion(rowRegion);

        //创建每列标题
        row = sheet.createRow(1);
        row.setHeight((short) (22.50 * 20));//设置行高

        HSSFCell cell1 = row.createCell(0);
        cell1.setCellValue("序号");//为第一个单元格设值
        cell1.setCellStyle(titleStyle);//为第一个单元格设置样式

        HSSFCell cell7 = row.createCell(1);
        cell7.setCellValue("班级");//为第二个单元格设值
        cell7.setCellStyle(titleStyle);
        HSSFCell cell8 = row.createCell(2);
        cell8.setCellValue("电话");//为第三个单元格设值
        cell8.setCellStyle(titleStyle);

        HSSFCell cell9 = row.createCell(3);
        cell9.setCellValue("学号");//为第四个单元格设值
        cell9.setCellStyle(titleStyle);

        HSSFCell cell10 = row.createCell(4);
        cell10.setCellValue("姓名");//为第五个单元格设值
        cell10.setCellStyle(titleStyle);

        HSSFCell cell11 = row.createCell(5);
        cell11.setCellValue("签到");//为第六个单元格设值
        cell11.setCellStyle(titleStyle);

        //每张表打印的数据条数(这里第一张sheet表最多可以打印35条数据(由于有标题)，其他的sheet1表最多可显示38条)
      //  int pageCount=pageCount;
        int a;
        int b=0;
        HSSFSheet sheet1=null;
        for (a=0;b<exportStudentList.size();a++){
            //数据量只为一个sheet的情况
            if(a==0) {
                for (int i = 0; i < (pageCount<exportStudentList.size()?pageCount:exportStudentList.size()); i++) {
                    row = sheet.createRow(i + 2);
                    row.setHeight((short) (20 * 20));//设置行高
                    ExcelExportStudent exportStudent = exportStudentList.get(i);

                    HSSFCell cell12 = row.createCell(0);
                    cell12.setCellValue(exportStudent.getNumber());
                    cell12.setCellStyle(styles);

                    HSSFCell cell13 = row.createCell(1);
                    cell13.setCellValue(exportStudent.getStudClassInfo());
                    cell13.setCellStyle(styles);

                    HSSFCell cell14 = row.createCell(2);
                    cell14.setCellValue(exportStudent.getStudPhone());
                    cell14.setCellStyle(styles);

                    HSSFCell cell16 = row.createCell(3);
                    cell16.setCellValue(exportStudent.getStudNumber());
                    cell16.setCellStyle(styles);

                    HSSFCell cell17 = row.createCell(4);
                    cell17.setCellValue(exportStudent.getStudEnrollName());
                    cell17.setCellStyle(styles);

                    HSSFCell cell18 = row.createCell(5);
                    cell18.setCellValue("");
                    cell18.setCellStyle(styles);
                }
                b=pageCount;
            }
            else if(a*pageCount<exportStudentList.size())  {
                sheet1 = wb.createSheet();
                wb.setSheetName(a,activity.getActiTheme()+"报名表"+(a+1));
                int index=1;

                for (b=a*pageCount;b<((a+1)*pageCount < exportStudentList.size() ? (a+1)*pageCount:exportStudentList.size() )        ;b++) {
                    row = sheet1.createRow(0);
                    row.setHeight((short) (22.50 * 20));//设置行高

                    HSSFCell cell20 = row.createCell(0);
                    cell20.setCellValue("序号");//为第一个单元格设值
                    cell20.setCellStyle(titleStyle);

                    HSSFCell cell21 = row.createCell(1);
                    cell21.setCellValue("班级");//为第二个单元格设值
                    cell21.setCellStyle(titleStyle);
                    HSSFCell cell22 = row.createCell(2);
                    cell22.setCellValue("电话");//为第三个单元格设值
                    cell22.setCellStyle(titleStyle);

                    HSSFCell cell23 = row.createCell(3);
                    cell23.setCellValue("学号");//为第四个单元格设值
                    cell23.setCellStyle(titleStyle);

                    HSSFCell cell24 = row.createCell(4);
                    cell24.setCellValue("姓名");//为第五个单元格设值
                    cell24.setCellStyle(titleStyle);

                    HSSFCell cell25 = row.createCell(5);
                    cell25.setCellValue("签到");//为第六个单元格设值
                    cell25.setCellStyle(titleStyle);

                    row = sheet1.createRow(index++);
                    row.setHeight((short) (20 * 20));//设置行高

                    ExcelExportStudent exportStudent = exportStudentList.get(b);

                    HSSFCell cell12 = row.createCell(0);
                    cell12.setCellValue(exportStudent.getNumber());
                    cell12.setCellStyle(styles);

                    HSSFCell cell13 = row.createCell(1);
                    cell13.setCellValue(exportStudent.getStudClassInfo());
                    cell13.setCellStyle(styles);

                    HSSFCell cell14 = row.createCell(2);
                    cell14.setCellValue(exportStudent.getStudPhone());
                    cell14.setCellStyle(styles);

                    HSSFCell cell16 = row.createCell(3);
                    cell16.setCellValue(exportStudent.getStudNumber());
                    cell16.setCellStyle(styles);

                    HSSFCell cell17 = row.createCell(4);
                    cell17.setCellValue(exportStudent.getStudEnrollName());
                    cell17.setCellStyle(styles);

                    HSSFCell cell18 = row.createCell(5);
                    cell18.setCellValue("");
                    cell18.setCellStyle(styles);

                    //列宽自适应
                    sheet1.autoSizeColumn(0, true);
                    sheet1.autoSizeColumn(1, true);
                    sheet1.autoSizeColumn(2, true);
                    sheet1.autoSizeColumn(3, true);
                    sheet1.autoSizeColumn(4, true);
                }
            }
        }
        //判断是谁去创建结尾数据:如果数据在一张sheet就显示完就由第一张sheet创建结尾数据，否则由其他sheet1去创建
        HSSFRow row1=null;
        if(sheet1==null){
            row1=sheet.createRow(exportStudentList.size()%pageCount==0? pageCount+2:exportStudentList.size()%pageCount+2 );
        }else {
            row1 = sheet1.createRow(exportStudentList.size()%pageCount==0? pageCount+1:exportStudentList.size()%pageCount+1 );
        }
        row1.createCell(0).setCellValue("   附:");//空格是为了在单元格中居中显示
        row1.createCell(1).setCellValue("报名情况:"+activity.getActiNowEnroll()+"/"+activity.getActiMaxEnroll());
        row1.createCell(2).setCellValue("打印日期:");
        row1.createCell(3).setCellValue(TimeUtils.getStringDateShort());
        //打印人的从session中取出
        Admin admin = (Admin) request.getSession().getAttribute("admin");
        row1.createCell(4).setCellValue("打印人:");
        row1.createCell(5).setCellValue(admin.getAdminName());

        sheet.setDefaultRowHeight((short) (18 * 20));
        //列宽自适应,单元格的宽度会根据内容来自动确定
        //原始 sheet.autoSizeColumn(i);改进之后
        sheet.autoSizeColumn(0, true);
        sheet.autoSizeColumn(1, true);
        sheet.autoSizeColumn(2, true);
        sheet.autoSizeColumn(3, true);
        sheet.autoSizeColumn(4, true);

        //将excel文件输出
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        OutputStream os = response.getOutputStream();
        response.setHeader("Content-disposition", "attachment;filename=StudentInfo.xls");//默认Excel名称,不能用中文名称，故不能使用活动主题作为文件名
        wb.write(os);
        os.flush();
        os.close();

    }
}
