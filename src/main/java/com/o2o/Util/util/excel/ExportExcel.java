package com.o2o.Util.util.excel;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Excel导出工具类
 *
 * @author     tangyuhan
 * @version    v1.0
 * 2016-11-17 14:33:41
 */
public final class ExportExcel {

	/**
	 * 创建字体样式
	 */
	public static HSSFCellStyle createTextStyle(HSSFWorkbook wb) {
		HSSFFont font = wb.createFont();
		font.setFontName("Verdana");
		font.setColor(HSSFFont.COLOR_NORMAL);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		HSSFCellStyle style = wb.createCellStyle();
		style.setFont(font);
		style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style.setWrapText(false);
		return style;
	}

	/**
	 * 粗体样式
	 */
	public static HSSFCellStyle createBoldStyle(HSSFWorkbook wb) {
		HSSFFont boldFont = wb.createFont();
		boldFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		boldFont.setColor(HSSFFont.COLOR_NORMAL);
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); //居中
		style.setFont(boldFont);
		return style;
	}

	/**
	 * 设置响应头
	 * @param response
	 * @param fileName
	 * @throws UnsupportedEncodingException
	 */
	public static void setHeader(HttpServletResponse response, String fileName) throws UnsupportedEncodingException {
//		fileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
//		fileName = new String(fileName.getBytes("GBK"), "UTF-8");
		response.setContentType("application/msexcel");
		response.setHeader("Pragma", "public");
		response.setHeader("Cache-Control", "max-age=0");
		response.setHeader("Content-Disposition", "attachment; filename=\"" + new String(fileName.getBytes("UTF-8"), "ISO8859-1") + ".xls\"");
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
		String codedFileName = URLEncoder.encode(fileName, "UTF-8");
		if (agent.contains("firefox")) {
			response.setCharacterEncoding("utf-8");
			response.setHeader("content-disposition", "attachment;filename=" + new String(fileName.getBytes(), "ISO8859-1") + ".xls");
		} else {
			response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xls");
		}
	}
	/**
	 * 创建单元格
	 */
	@SuppressWarnings("deprecation")
	public static void createCell(HSSFRow row, int column, HSSFCellStyle style, int cellType, Object value) {
		HSSFCell cell = row.createCell((short) column);
		if (style != null) {
			cell.setCellStyle(style);
		}
		cell.setCellType(cellType);
		switch (cellType) {
			case HSSFCell.CELL_TYPE_BLANK: {
				break;
			}
			case HSSFCell.CELL_TYPE_STRING: {
				if (value != null){
					cell.setCellValue(value.toString());
				}
				break;
			}
			case HSSFCell.CELL_TYPE_NUMERIC: {
				try {
					if (value != null && StringUtils.isNotBlank(value.toString())) {
						cell.setCellFormula(value.toString());
					}
				} catch (Exception e) {
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellValue(value.toString());
				}
				break;
			}
			default:
				break;
		}
	}

	/**
	 * 输出excel到客户端
	 * @param response
	 * @param wb
	 * @throws IOException
	 */
	public static void release(HttpServletResponse response, HSSFWorkbook wb) throws IOException {
		OutputStream op = response.getOutputStream();
		wb.write(op);
		op.flush();
		op.close();
	}

	/**
	 * 清除临时文件
	 * @param wb
	 * @throws IOException
	 */
	public static void close(HSSFWorkbook wb) throws IOException {
//		wb.close();
	}

}
