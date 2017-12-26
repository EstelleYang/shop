package com.o2o.Util;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.HSSFRegionUtil;
import org.apache.poi.hssf.util.Region;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class ExportExecle {
    private HSSFWorkbook wb = null;
    private HSSFSheet sheet = null;

    public ExportExecle(HSSFWorkbook wb,HSSFSheet sheet){
        this.wb = wb;
        this.sheet = sheet;
    }

    /**
     *
     * @param headString 该报表的头部显示字符
     * @param colSum 该报表的列数
     */
    public void createNormalHead(String headString,int colSum){
        HSSFRow row = sheet.createRow(0);
        //设置第一行
        HSSFCell cell = row.createCell(0);
        //定义单元格为字符串类型
        cell.setCellType(HSSFCell.ENCODING_UTF_16);//中文处理
        cell.setCellValue(new HSSFRichTextString(headString));
        //指定合并区域
        /**
         * public Region(int rowFrom, short colFrom, int rowTo, short colTo)
         */
        sheet.addMergedRegion(new Region(0,(short)0,0,(short)colSum));
        //定义单元格格式，添加单元格表样式，并添加到工作簿
        HSSFCellStyle cellStyle = wb.createCellStyle();
        //设置单元格水平对齐类型
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        cellStyle.setWrapText(true);//指定单元格自动换行
        //设置单元格字体
        HSSFFont font = wb.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font.setFontName("宋体");
        font.setFontHeight((short)600);
        cellStyle.setFont(font);
        cell.setCellStyle(cellStyle);
    }

    /**
     * 创建通用报表第二行
     * @param params 统计条件数组
     * @param colSum 需要合并到的列索引
     */
    public void createNormalTwoRow(String[]params,int colSum){
        //创建第二行
        HSSFRow row1 = sheet.createRow(1);
        row1.setHeight((short)400);
        HSSFCell cell2 = row1.createCell(0);
        cell2.setCellType(HSSFCell.ENCODING_UTF_16);
        cell2.setCellValue(new HSSFRichTextString("时间："+params[0]+"至"+params[1]));
        //指定合并区域
        sheet.addMergedRegion(new Region(1,(short)0,1,(short)colSum));
        HSSFCellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        cellStyle.setWrapText(true);
        HSSFFont font = wb.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font.setFontName("宋体");
        font.setFontHeight((short)250);
        cellStyle.setFont(font);
        cell2.setCellStyle(cellStyle);
    }

    public void createColumHeader(String[]columHeader){
        //设置列头 在第三行
        HSSFRow row2 = sheet.createRow(2);
        row2.setHeight((short)600);
        HSSFCellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HSSFCellStyle.VERTICAL_CENTER);
        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        cellStyle.setWrapText(true);
        HSSFFont font = wb.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font.setFontName("宋体");
        font.setFontHeight((short)250);
        cellStyle.setFont(font);
        //设置单元格背景色
        cellStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        cellStyle.setFillForegroundColor(HSSFCellStyle.SOLID_FOREGROUND);
        HSSFCell cell3 = null;
        for (int i=0;i<columHeader.length;i++){
            cell3 = row2.createCell(i);
            cell3.setCellType(HSSFCell.ENCODING_UTF_16);
            cell3.setCellStyle(cellStyle);
            cell3.setCellValue(new HSSFRichTextString(columHeader[i]));
        }
    }

    /**
     * 创建内容单元格
     * @param wb HSSFWorlbook
     * @param row HSSFRow
     * @param col short型列索引
     * @param align 对齐方式 val:列值
     */
    public void createCell(HSSFWorkbook wb,HSSFRow row,int col,short align,String val){
        HSSFCell cell = row.createCell(col);
        cell.setCellType(HSSFCell.ENCODING_UTF_16);
        cell.setCellValue(new HSSFRichTextString(val));
        HSSFCellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(align);
        cell.setCellStyle(cellStyle);
    }

    /**
     * 输入Excel文件
     * @param fileName
     */
    public void OutputExcel(String fileName){
        FileOutputStream fileOutputStream = null;
        try{
            fileOutputStream = new FileOutputStream(new File(fileName));
            wb.write(fileOutputStream);
            fileOutputStream.close();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public HSSFSheet getSheet() {
        return sheet;
    }
    public void setSheet(HSSFSheet sheet){
        this.sheet = sheet;
    }
    public HSSFWorkbook getWb(){
        return wb;
    }
    public void setWb(HSSFWorkbook wb){
        this.wb = wb;
    }
}
