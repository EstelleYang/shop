/*
 * Copyright by Deppon and the original author or authors.
 *
 * This document only allow internal use ,Any of your behaviors using the file
 * not internal will pay legal responsibility.
 *
 * You may learn more information about Deppon from
 *
 *      http://www.deppon.com
 *
 */
package com.o2o.Util.util.excel;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

/**
 *<pre>
 *功能:Excel导出工具类
 *作者：405859
 *日期：2017年2月20日下午2:20:34
 *</pre>
 */
public final class ExportExcelNew {
	/**
	 *<pre>
	 * 方法体说明：(创建字体样式)
	 * 作者：405859
	 * 日期： 2017年3月1日 下午10:22:44
	 * @param wb
	 * @return
	 *</pre>
	 */
	public static CellStyle createTextStyle(SXSSFWorkbook wb) {
		//Represents a Font
		Font font = wb.createFont();
		font.setFontName("微软雅黑");//微软雅黑
		font.setCharSet(Font.DEFAULT_CHARSET);//设置中文字体，那必须还要再对单元格进行编码设置
		font.setFontHeightInPoints((short) 10);//10号 自动修正快捷键 Ctrl+1
		font.setColor(Font.COLOR_NORMAL);
		font.setBoldweight(Font.BOLDWEIGHT_NORMAL);//非粗体
		CellStyle style = wb.createCellStyle();
		style.setFont(font);
		style.setAlignment(CellStyle.ALIGN_LEFT);
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		style.setWrapText(false);
		return style;
	}
	/**  
	* @Title: createHeadMoneyStyle  
	* @Description: TODO(第一行金额字段：微软雅黑 12号，黑色，加粗，行高 14，靠左，居中，无网格线)  
	* @param @param wb
	* @param @return    参数  
	* @return CellStyle    返回类型  
	* @throws  
	*/
	public static CellStyle createHeadMoneyStyle(SXSSFWorkbook wb) {
		//Represents a Font
		Font font = wb.createFont();
		font.setFontName("微软雅黑");//微软雅黑
		font.setCharSet(Font.DEFAULT_CHARSET);//设置中文字体，那必须还要再对单元格进行编码设置
		font.setFontHeightInPoints((short) 12);//12号 自动修正快捷键 Ctrl+1
		font.setColor(Font.COLOR_NORMAL);
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);//粗体
		CellStyle style = wb.createCellStyle();
		style.setFont(font);
		style.setAlignment(CellStyle.ALIGN_LEFT);
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		style.setWrapText(false);
		return style;
	}
	/**  
	* @Title: createHeadStyle  
	* @Description: TODO(第二行表头字段：微软雅黑 10号，居中，白色，不加粗， 底色 德邦蓝 55 66 100，行高 12 ，有网格线)  
	* @param @param wb
	* @param @return    参数  
	* @return CellStyle    返回类型  
	* @throws  
	*/
	public static CellStyle createHeadStyle(SXSSFWorkbook wb) {
		Font font = wb.createFont();
		font.setFontName("微软雅黑");//微软雅黑
		font.setCharSet(Font.DEFAULT_CHARSET);//设置中文字体，那必须还要再对单元格进行编码设置
		font.setFontHeightInPoints((short) 10);//10号 自动修正快捷键 Ctrl+1
		font.setColor(HSSFColor.WHITE.index);//白色
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);//粗体
		XSSFCellStyle style = (XSSFCellStyle)wb.createCellStyle();
		style.setFont(font);
		style.setAlignment(CellStyle.ALIGN_LEFT);//横向具左对齐
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);//单元格垂直居中
		style.setWrapText(false);//换行
		
       //设置背景色
		style.setFillBackgroundColor(HSSFColor.RED.index);
		style.setFillForegroundColor(HSSFColor.LIME.index);
		style.setFillForegroundColor(new XSSFColor(new java.awt.Color((byte) 55,(byte) 60,(byte) 100)));
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
			
		//使用自定义颜色 HSSFPalette
//		HSSFPalette customPalette = wb.getCustomPalette();  
//		customPalette.setColorAtIndex(HSSFColor.ORANGE.index, (byte) 55, (byte) 60, (byte) 100);
        //背景颜色
//		style.setFillForegroundColor(HSSFColor.ORANGE.index);
        //填充方式，前色填充
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		style.setBorderTop(CellStyle.SOLID_FOREGROUND);
		style.setBorderLeft(CellStyle.SOLID_FOREGROUND);
		style.setBorderBottom(CellStyle.SOLID_FOREGROUND);
		style.setBorderRight(CellStyle.SOLID_FOREGROUND);
		style.setTopBorderColor(HSSFColor.BLACK.index);
		style.setLeftBorderColor(HSSFColor.BLACK.index);
		style.setRightBorderColor(HSSFColor.BLACK.index);
		style.setBottomBorderColor(HSSFColor.BLACK.index);
		style.setAlignment(CellStyle.ALIGN_CENTER);
		return style;
	}
	/**
	 * @param wb
	 * @return
	 * 明细行字段：微软雅黑 10号，居中，黑色，不加粗，行高 10，有网格线
	 */
	public static CellStyle createDetailStyle(SXSSFWorkbook wb) {
		//Represents a Font
		Font font = wb.createFont();
		font.setFontName("Verdana");
		//normal type of black color.
		font.setColor(Font.COLOR_NORMAL);
		//粗体
		font.setBoldweight(Font.BOLDWEIGHT_NORMAL);
		//Create a new Cell style and add it to the workbook's style table
		CellStyle style = wb.createCellStyle();
		style.setFont(font);
		style.setAlignment(CellStyle.ALIGN_LEFT);
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		style.setWrapText(false);
		return style;
	}
	/**
	 *<pre>
	 * 方法体说明：(粗体样式)
	 * 作者：405859
	 * 日期： 2017年3月1日 下午10:22:55
	 * @param wb
	 * @return
	 *</pre>
	 */
	public static CellStyle createBoldStyle(SXSSFWorkbook wb) {
		//create a new Font and add it to the workbook's font table
		Font boldFont = wb.createFont();
		//粗体
		boldFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		//颜色
		boldFont.setColor(Font.COLOR_NORMAL);
		//Create a new Cell style and add it to the workbook's style table
		CellStyle style = wb.createCellStyle();
		//居中
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setFont(boldFont);
		return style;
	}
	/**
	 * 405859
	 * 设置响应头
	 * 兼容火狐
	 * @param response
	 * @param request
	 * @param fileName
	 * @throws UnsupportedEncodingException
	 */
	public static void setBaseMoreHeader(HttpServletResponse response,HttpServletRequest request, String fileName) throws UnsupportedEncodingException {
		String agent = request.getHeader("USER-AGENT").toLowerCase();
		response.setContentType("application/vnd.ms-excel");
		fileName = fileName.substring(fileName.lastIndexOf("/")+1);
		String codedFileName = URLEncoder.encode(fileName, "UTF-8");
		if (agent.contains("firefox")) {
			response.setCharacterEncoding("utf-8");
			response.setHeader("content-disposition", "attachment;filename=" + new String(fileName.getBytes(), "ISO8859-1"));
		} else {
			response.setHeader("content-disposition", "attachment;filename=" + codedFileName);
		}
	}
	/**
	 *<pre>
	 * 方法体说明：(设置响应头)
	 * 作者：405859
	 * 日期： 2017年3月1日 下午10:23:10
	 * @param response
	 * @param fileName
	 * @throws UnsupportedEncodingException
	 *</pre>
	 */
	public static void setHeader(HttpServletResponse response, String fileName) throws UnsupportedEncodingException {
		fileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
		fileName = new String(fileName.getBytes("GBK"), "UTF-8");
		response.setContentType("application/msexcel");
		response.setHeader("Pragma", "public");
		response.setHeader("Cache-Control", "max-age=0");
		//Content-Disposition为属性名 
		//attachment 以附件方式下载 
		//filename 默认保存时的文件名 
		response.setHeader("Content-disposition", "attachment; filename="+ fileName+".xls");
	}
	/**
	 *<pre>
	 * 方法体说明：(创建单元格)
	 * 作者：405859
	 * 日期： 2017年3月1日 下午10:23:21
	 * @param row
	 * @param column
	 * @param style
	 * @param cellType
	 * @param value
	 *</pre>
	 */
	@SuppressWarnings("deprecation")
	public static void createCell(Row row, int column, CellStyle style, int cellType, Object value) {
		//创建指定列号的单元格
		Cell cell = row.createCell((short) column);
		if (style != null) {
			cell.setCellStyle(style);
		}
		cell.setCellType(cellType);
		switch (cellType) {
			case Cell.CELL_TYPE_BLANK: {
				//Blank Cell type
				break;
			}
			case Cell.CELL_TYPE_STRING: {
				//String Cell type
				if (value != null){
					cell.setCellValue(value.toString());
				}
				break;
			}
			case Cell.CELL_TYPE_NUMERIC: {
				//Numeric Cell type
				try {
					if (value != null && StringUtils.isNotBlank(value.toString())) {
						cell.setCellFormula(value.toString());
					}
				} catch (Exception e) {
					cell.setCellType(Cell.CELL_TYPE_STRING);
					cell.setCellValue(value.toString());
				}
				break;
			}
			default:
				break;
		}
	}
	public  static void releaseBase(HttpServletResponse response,String realPath) throws IOException {
		InputStream in = new FileInputStream(realPath);
		int len = 0;
		//5.创建数据缓冲区
		byte[] buffer = new byte[1024];
		//6.通过response对象获取OutputStream流
		OutputStream out = response.getOutputStream();
		//7.将FileInputStream流写入到buffer缓冲区
		while ((len = in.read(buffer)) > 0) {
		//8.使用OutputStream将缓冲区的数据输出到客户端浏览器
		    out.write(buffer,0,len);
		}
		in.close();
		File file = new File(realPath);
		file.delete();
	}
	/**
	 *<pre>
	 * 方法体说明：(输出excel到客户端)
	 * 作者：405859
	 * 日期： 2017年3月1日 下午10:23:32
	 * @param response
	 * @param wb
	 * @throws IOException
	 *</pre>
	 */
	public static void release(HttpServletResponse response, SXSSFWorkbook wb) throws IOException {
		//输出流
		OutputStream op = response.getOutputStream();
		//write out this workbook to an OutputStream. 
		wb.write(op);
		//Flushes this output stream 
		op.flush();
		//Closes this output stream
		op.close();
	}
	/**
	 *<pre>
	 * 方法体说明：(清除临时文件)
	 * 作者：405859
	 * 日期： 2017年3月1日 下午10:23:44
	 * @param wb
	 * @throws IOException
	 *</pre>
	 */
	public static void close(SXSSFWorkbook wb) throws IOException {
		//关闭
//		wb.close();
	}
}
