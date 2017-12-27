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

import com.alibaba.fastjson.JSONObject;
import com.deppon.cubc.bill.client.model.bill.BillExportListDO;
import com.deppon.cubc.bill.client.utils.TemplateException;
import com.deppon.cubc.web.common.util.JSONUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

/**
 *<pre>
 *功能:适用于多个sheet的导出
 *作者：405859
 *日期：2017年2月19日下午3:21:57
 *</pre>
 */
@SuppressWarnings({ "rawtypes","unchecked"})
public class ExportBaseMoreSheetNew<T> {
	public  String filePath = "";
    /**
     * 自定义列集合
     * key:     属性名
     * value    header列名
     */
    protected Map<String,Object> customMap;
    /**
     * 总览
     */
    protected Collection<BillExportListDO> collection;
    
    protected String money ="";
    protected String discountMoney="";
    /**
     * 导出集合
     */
    protected List<T> list;
    /**
     * 类属性集合
     */
    protected List<String> fieldList = new ArrayList<String>();
    /**
     * key:属性名
     * value:属性类型
     * 类属性集合
     */
    protected Map<String,Class<?>> fieldMap = new HashMap<String,Class<?>>();
    /**
     * wb
     */
    protected  SXSSFWorkbook wb;
    /**
     * sheet
     */
    protected Sheet sheet;
	/**
	 * centerStyle
	 */
    protected CellStyle centerStyle;
    /**
     * leftStyle
     */
    protected CellStyle leftStyle;
    /**
     * boldStyle
     */
    protected CellStyle boldStyle;
    /**
     * request
     */
    protected HttpServletRequest request;
    /**
     * response
     */
    protected HttpServletResponse response;
    //日志
    private final static Logger logger = LoggerFactory.getLogger(ExportBaseMoreSheetNew.class);
    /**
     * 标题行号
     */
    protected int headerNum = 0;
    /**
     * rowNum
     */
    protected int rowNum = 2;
    /**
     * sequenceNum
     */
    private int sequenceNum  = 1;
    /**
     * Excel列宽设置常量
     */
    private static final int ROW_ONE = 0;
    /**
     * Excel列宽设置常量
     */
    private static final int SIZE_SMALL = 10;
    /**
     * Excel列宽设置常量
     */
    private static final int SIZE_BIG = 25;
    /**
     * Excel列宽设置常量
     */
    private static final float SIZE_ADD = 0.72f;
    /**
     * Excel列宽设置常量
     */
    private static final int LENGTH= 256;
    
    List<String> sheetList = new ArrayList<String>();
    /**
     *  Excel导出
     * @param customMapJson JSON串
     * @param fileName      Excel名称
     * @param list          导出集合
     * @param response      HttpServletResponse
     */
	public ExportBaseMoreSheetNew(String fileName,List <Map<String, Object>> customMapJsons, 
			List <String> sheetNames,List<String> moneys,List<String> discountMoneys,List <Collection<T>> lists ,
			Collection<BillExportListDO> collection,HttpServletResponse response,HttpServletRequest request){
		//创建EXCEL
		this.wb = new SXSSFWorkbook();
        this.request = request;
        this.response = response;
		try {
			//总览
			createMainWorkbook(collection);
			//填充EXCEL
			creatWorkbookBase(customMapJsons, sheetNames, moneys, discountMoneys);
		} catch (Exception e) {
			logger.error("生成excel异常",e);
		}
    }
	public   SXSSFWorkbook getWb(){
		return wb;
	}
    /**
     * <p>创建一个新的实例 ExportBaseMoreSheetUtil.TODO</p>
     * @author 405859
     * @date 2017年3月2日 上午10:48:38
     */
    protected ExportBaseMoreSheetNew(){}
    
    protected void creatWorkbookBase(List <Map<String, Object>> customMapJsons, 
    		List <String> sheetNames,List<String> moneys,List<String> discountMoneys){
	    	for (int i = 0; i < sheetNames.size(); i++) {
	    		//创建sheet
				this.sheet = wb.createSheet(sheetNames.get(i));
	    	}
    }
    /**
     * 创建WorkBook
     * @param fileName
     * @throws IOException 
     * @throws TemplateException 
     */
    public  void createWorkbook(Collection<T> list,String sheetName,
    		Map<String, Object> customMapJson,String money,String discountMoney) throws TemplateException, IOException{
    	    if(!sheetList.contains(sheetName)){
    	    	//创建sheet表头
				LinkedHashMap customMap = (LinkedHashMap) customMapJson;
				this.customMap = customMap;
    	    	this.sheet = wb.getSheet(sheetName);
    	    	this.headerNum = 0;
				this.createColumns(money,discountMoney);
    			//填充表头对应的数据
    		    this.list = (List<T>) list;
    		    this.fieldsAsList(this.list.get(0).getClass());
    		    //避免下一个sheet 导出 行数和列数错误
                this.sequenceNum = 1;
                this.rowNum = 2;
                sheetList.add(sheetName);
    	    }
		    getReport();
    }
	public  void saveWorkBook2007(SXSSFWorkbook workbook2007, String dstFile) {
		FileOutputStream os = null;    
		try {
			logger.error("保存文件路径"+dstFile);
			os = new FileOutputStream(dstFile);
			workbook2007.write(os);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					System.out.println(e);
				}
			}
		}
	}
    public void closeWb(String fileName) throws IOException{
    	export(fileName);
    }
    /**
    * @Title: createFirstSheet
    * @Description: (总览)
    * @param @param wb
    * @param @param sheetname
    * @param @param list    参数
    * @return void    返回类型
    * @throws
    * 405859
    */
    public void createFirstSheet(SXSSFWorkbook wb, String sheetname) {
        //创建sheet
        Sheet sheet = wb.createSheet(sheetname);
        //去除所有边框
        sheet.setDisplayGridlines(false);
        //合并单元格
        sheet.addMergedRegion(new CellRangeAddress(2, 3, 1, 18));
        //设置列头的字体
        Font headFont = wb.createFont();
        //设置字体
        headFont.setFontName("华文细黑");
        //设置大小
        headFont.setFontHeightInPoints((short) 14);
        //设置白体
        headFont.setColor(HSSFColor.WHITE.index);
        //设置列头的样式
        XSSFCellStyle headStyle = (XSSFCellStyle) wb.createCellStyle();
        //左右居中
        headStyle.setAlignment(CellStyle.ALIGN_CENTER);
        //上下居中
        headStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        //锁定
        headStyle.setLocked(true);
        //设置背景色
        headStyle.setFillForegroundColor(HSSFColor.LIME.index);
        //设置背景色
        headStyle.setFillBackgroundColor(HSSFColor.RED.index);
        headStyle.setFillForegroundColor(HSSFColor.LIME.index);
        //填充
        headStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color((byte) 55, (byte) 60, (byte) 100)));
        headStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        //设置字体
        headStyle.setFont(headFont);
        //创建单元格
        Row headRow = sheet.createRow(2);
        //创建cell
        Cell headCell = headRow.createCell(1);
        //设置值
        headCell.setCellValue("德邦物流股份有限公司客户对账单");
        //设置样式
        headCell.setCellStyle(headStyle);
        //设置右边样式
        for (int i = 0; i <= 12; i++) {//右侧边框一共有12行
            //创建单元格
            Row row = sheet.createRow(i + 4);
            //添加右边边框的样式
            CellStyle rightStyle = wb.createCellStyle();
            //右边边框
            rightStyle.setBorderRight(CellStyle.BORDER_THIN);
            //右边边框颜色
            rightStyle.setRightBorderColor(HSSFColor.BLACK.index);
            //获取最后一列
            Cell rightCell = row.createCell(18);
            //第一个
            Cell leftCell = row.createCell(0);
            //设置样式
            rightCell.setCellStyle(rightStyle);
            //设置样式
            leftCell.setCellStyle(rightStyle);
        }
        //设置底部的样式
        //创建单元格
        Row bottomRow = sheet.getRow(16);//excel底框所在行数
        for (int i = 0; i < 18; i++) {
            //添加右边边框的样式
            CellStyle bottomStyle = wb.createCellStyle();
            //底部边框
            bottomStyle.setBorderBottom(CellStyle.BORDER_THIN);
            //底部边框颜色
            bottomStyle.setBottomBorderColor(HSSFColor.BLACK.index);
            //创建cell
            Cell cell = bottomRow.getCell(i + 1);
            if (cell != null) {
                //右边边框
                bottomStyle.setBorderRight(CellStyle.BORDER_THIN);
                //右边边框颜色
                bottomStyle.setRightBorderColor(HSSFColor.BLACK.index);
            } else {
                cell = bottomRow.createCell(i + 1);
            }
            //设置底部样式
            cell.setCellStyle(bottomStyle);
        }
        //锁定位置
        for (BillExportListDO billExportDO : collection) {
            //设置字体
            Font font = wb.createFont();
            //设置字体
            font.setFontName("华文细黑");
            //设置大小
            font.setFontHeightInPoints((short) 12);
            if ("应还总金额：".equals(billExportDO.getCellName())) {
                //设置粗体
                font.setBoldweight(Font.BOLDWEIGHT_BOLD);
            }
            if ("温馨提示，请将款项汇至我司指定账户，勿将款项转至他人；如有异常请在对账单确认截止时间前与我们联系".equals(billExportDO.getCellName())) {
                //设置颜色
                font.setColor(HSSFColor.RED.index);
            }
            //设置样式
            CellStyle style = wb.createCellStyle();
            //设置字体
            style.setFont(font);
            //创建行
            Row row = sheet.getRow(billExportDO.getRow());
            if (row == null) {
                row = sheet.createRow(billExportDO.getRow());
            }
            //创建cell名称
            Cell cellName = row.createCell(billExportDO.getCol());
            //设置样式
            cellName.setCellStyle(style);
            //设置值
            cellName.setCellValue(billExportDO.getCellName() + "" + billExportDO.getCellValue());
        }

    }
    /**
     *<pre>
     * 方法体说明：(创建Header宽度)
     * 作者：405859
     * 日期： 2017年2月24日 下午2:54:21
     *</pre>
     */
    protected void createColumns(String money,String discountMoney){
    	List<String> list  = new ArrayList<String>();
    	list.add("businessDate");
    	list.add("businessOverDate");
    	list.add("waybillNo");
    	list.add("destination");
    	list.add("customerName");
    	list.add("deliveryContact");
    	list.add("consignee");
    	list.add("goodsName");
        int i =0;
        Iterator<Entry<String, Object>> iterator = customMap.entrySet().iterator();
        while (iterator.hasNext()) {
        	Entry<String, Object> next = iterator.next();
        	String key = next.getKey();
        	if(list.contains(key))
        	{
                BigDecimal smallSize = BigDecimal.valueOf(SIZE_SMALL).add(BigDecimal.valueOf(SIZE_ADD)).multiply(BigDecimal.valueOf(LENGTH));
                sheet.setColumnWidth(((short)i), smallSize.intValue());

        	}else {
        		Object value = next.getValue();
        		int length2 = value.toString().getBytes().length;
        		((SXSSFSheet) sheet).trackAllColumnsForAutoSizing();
        		sheet.autoSizeColumn((short)i);
			}
        	i++;
		}
		//金额计算
		if(!"".equals(money)&&!"0.00".equals(money)
				&&!"0".equals(money)&&!"0.0".equals(money)){
			this.money = money;
		}else{
			this.money="";
		}
		if(!"".equals(discountMoney)&&!"0.00".equals(discountMoney)
				&&!"0".equals(discountMoney)&&!"0.0".equals(discountMoney)){
			this.discountMoney = discountMoney;
		}else{
			this.discountMoney="";
		}
        //标题金额
        this.createHeadMoney();
        //标题列
        this.createHead();
    }
    /**
    * @Title: createHeadMoneyStyle
    * @Description: TODO(创建填充样式)
    * @param     参数
    * @return void    返回类型
    * @throws
    */
    protected void createHeadMoneyStyle(){
    	this.centerStyle = createLeftStyle();
    	CellStyle boldStyle = ExportExcelNew.createHeadMoneyStyle(wb);
    	this.boldStyle = boldStyle;
    }
    /**
    * @Title: createHeadStyle
    * @Description: TODO(创建填充样式)
    * @param     参数
    * @return void    返回类型
    * @throws
    */
    protected void createHeadStyle(){
    	this.centerStyle = createCenterStyle();
    	CellStyle boldStyle = ExportExcelNew.createHeadStyle(wb);
    	this.boldStyle = boldStyle;
    }
    /**
     *<pre>
     * 方法体说明：(创建填充样式)
     * 作者：405859
     * 日期： 2017年2月24日 下午2:54:41
     *</pre>
     */
    protected void createStyle(){
        this.centerStyle = createCenterStyle();
//        this.boldStyle = ExportExcelNew.createBoldStyle(wb);
        //前景颜色
        boldStyle.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
        //填充方式，前色填充
        boldStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        boldStyle.setBorderTop(CellStyle.SOLID_FOREGROUND);
        boldStyle.setBorderLeft(CellStyle.SOLID_FOREGROUND);
        boldStyle.setBorderBottom(CellStyle.SOLID_FOREGROUND);
        boldStyle.setBorderRight(CellStyle.SOLID_FOREGROUND);
        boldStyle.setTopBorderColor(HSSFColor.BLACK.index);
        boldStyle.setLeftBorderColor(HSSFColor.BLACK.index);
        boldStyle.setRightBorderColor(HSSFColor.BLACK.index);
        boldStyle.setBottomBorderColor(HSSFColor.BLACK.index);
        boldStyle.setWrapText(true);//换行
    }
    /**
     * 创建WorkBook
     * @param fileName
     * @throws UnsupportedEncodingException
     * 总览
     */
    protected void createMainWorkbook(Collection<BillExportListDO> collection) {
        this.collection = collection;
        this.createFirstSheet(wb, "总览");;
    }
    /**
    * @Title: createHeadMoney
    * @Description: TODO(创建标题金额列)
    * @param     参数
    * @return void    返回类型
    * 作废  可做合并单元格的参考
    * @throws
    * 第一行金额字段：微软雅黑 12号，黑色，加粗，行高 14，靠左，居中，无网格线
    */
    protected void createMainMergeList(){
    	this.createHeadMoneyStyle();
//    	//空白行
//    	CellRangeAddress cra0=new CellRangeAddress(0, 1, 0, 16);
//		this.mergeCell(cra0,0, "",0);
		//头
        Row headRow0 = sheet.createRow(2);
		CellRangeAddress cra1=new CellRangeAddress(2, 3, 1, 16);
		this.mergeCell(cra1,headRow0, "德邦物流股份有限公司客户对账单",1);

		Row headRow1 = sheet.createRow(4);
		CellRangeAddress cra3=new CellRangeAddress(4, 4, 1, 6);
		this.mergeCell(cra3,headRow1, "客户名称",1);
		CellRangeAddress cra4=new CellRangeAddress(4, 4, 7, 12);
		this.mergeCell(cra4,headRow1, "所属部门",7);
		CellRangeAddress cra5=new CellRangeAddress(4, 4, 13, 18);
		this.mergeCell(cra5,headRow1, "账单日",13);
		Row headRow2 = sheet.createRow(5);
		CellRangeAddress cra6=new CellRangeAddress(5, 5, 1, 6);
		this.mergeCell(cra6,headRow2, "对账单号",1);
		//其它行
		CellRangeAddress cra7=new CellRangeAddress(15, 15, 1, 12);
		this.mergeCell(cra7,headRow1, "温馨提示，请将款项汇至我司指定账户，勿将款项转至他人；如有异常请在对账单确认截止时间前与我们联系",1);
//		int startRow =
//    	for (int i = 2; i < 10; i++) {
//    		CellRangeAddress cra=new CellRangeAddress(0, 1, 0, 16);
//    		this.mergeCell(cra,i, "aaaa");
//		}
    }
    /**
    * @Title: mergeCell  
    * @Description: TODO(合并单元格)  
    * @param     参数  
    * @return void    返回类型  
    * @throws  
    */
    protected void mergeCell(CellRangeAddress cra, Row row, String value, int cellIndex){
    	 /* 
         * 设定合并单元格区域范围 
         *  firstRow  0-based 
         *  lastRow   0-based 
         *  firstCol  0-based 
         *  lastCol   0-based 
         */  
        //在sheet里增加合并单元格  
        sheet.addMergedRegion(cra);
        Cell cell = row.createCell((short)cellIndex);
        cell.setCellValue(value); 
    }
    /**  
    * @Title: createHeadMoney  
    * @Description: TODO(创建标题金额列)  
    * @param     参数  
    * @return void    返回类型  
    * @throws  
    * 第一行金额字段：微软雅黑 12号，黑色，加粗，行高 14，靠左，居中，无网格线
    */
    protected void createHeadMoney(){
    	this.createHeadMoneyStyle();
        /* 
         * 设定合并单元格区域范围 
         *  firstRow  0-based 
         *  lastRow   0-based 
         *  firstCol  0-based 
         *  lastCol   0-based 
         */  
        CellRangeAddress cra=new CellRangeAddress(0, 0, 0, customMap.size());
        //在sheet里增加合并单元格  
        sheet.addMergedRegion(cra);
        Row headRow = sheet.createRow(0);
        //行高
        headRow.setHeight((short) (15.625*35));
        Cell cell = headRow.createCell((short)0);
        cell.setCellStyle(boldStyle);
        if(!"".equals(money)&&!"".equals(discountMoney)){
        	cell.setCellValue("应还金额："+money+"  事后折："+discountMoney); 
        }else if(!"".equals(money)){
        	cell.setCellValue("应还金额："+money); 
        }else{
        	cell.setCellValue(""); 
        }
    }
    /**
     *<pre>
     * 方法体说明：(创建标题列)
     * 作者：405859
     * 日期： 2017年3月2日 上午10:50:08
     *</pre>
     */
    protected void createHead(){
        //填充样式
        this.createHeadStyle();
        Row headRow = sheet.createRow(1);
        //行高
        headRow.setHeight((short) (15.625*30));
        int cellType = Cell.CELL_TYPE_STRING;
        Iterator iter = customMap.entrySet().iterator();
        while (iter.hasNext()){
            Entry entry = (Entry) iter.next();
            Object value = entry.getValue();
            //第二行表头字段：微软雅黑 10号，居中，白色，不加粗， 底色 德邦蓝 55 66 100，行高 12 ，有网格线
            ExportExcelNew.createCell(headRow,headerNum++, boldStyle,cellType,value.toString());
        }
    }
	/**
	 *<pre>
	 * 方法体说明：(填充Excel)
	 * 作者：405859
	 * 日期： 2017年3月2日 上午10:50:18
	 * @throws IOException
	 * @throws Exception
	 *</pre>
	 */
	protected void getReport() throws TemplateException, IOException {
        logger.debug("导出Excel总计{}条数据",list.size());
        logger.debug("导出Excel填充数据开始.......");
        for(int i = 0;i < list.size();i++){
            int cellType = Cell.CELL_TYPE_STRING;
            Row row = sheet.createRow(rowNum);
            row.setHeight((short) (15.625*20));//行高
            JSONObject jsonObject = JSONUtils.converEntityToJSONObject(list.get(i));
            int rowsCount = 0;
            Iterator itor = customMap.entrySet().iterator();
            while(itor.hasNext()){
                Entry entry = (Entry) itor.next();
                String key = entry.getKey().toString();
                if(!fieldList.contains(key) && !key.contains(".")) {
                    ExportExcelNew.createCell(row, rowsCount++, centerStyle, cellType, "");
                    continue;
                }
                //实体类中如果存在其它实体，则循环调用取值
                Object value = null;
                if (null != jsonObject) {
                    if (key.contains(".")) {
                        String[] keys = StringUtils.split(key, ".");
                        JSONObject jsonAttr = JSONUtils.converEntityToJSONObject(jsonObject.get(keys[0]));
                        for (int k = 1; k < keys.length - 1; k++) {
                            if (null != jsonAttr) {
                                jsonAttr = JSONUtils.converEntityToJSONObject(jsonObject.get(keys[k]));
                            }
                        }
                        if (null != jsonAttr) {
                            value = jsonAttr.get(keys[keys.length - 1]);
                        }
                    } else {
                        value = jsonObject.get(key);
                    }
                }
                if (null == value) {
                    ExportExcelNew.createCell(row, rowsCount++, centerStyle, cellType, "");
                    continue;
                }
                if(fieldMap.get(key) == Date.class && value.getClass().getName().equals("java.lang.Long")){
//                    value = DateUtil.formatDate(new Date((Long)value),"yyyy-MM-dd");
                      value = formatDate(new Date((Long)value),"yyyy-MM-dd");
                }
                ExportExcelNew.createCell(row, rowsCount++, centerStyle, cellType, value);
            }
            if(rowNum%1000==0){
                ((SXSSFSheet)sheet).flushRows();
            }
            rowNum++;
        }
    }
	/**
	 * 时间转换 改成中国时区，默认是US
	 * @param date
	 * @param pattern
	 * @return
	 */
	public  String formatDate(Date date, String pattern) {
        if (date == null) throw new IllegalArgumentException("date is null");
        if (pattern == null) throw new IllegalArgumentException("pattern is null");
        
        SimpleDateFormat formatter = new SimpleDateFormat(pattern, Locale.CHINA);
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return formatter.format(date);
    } 
    /**
     *<pre>
     * 方法体说明：(导出)
     * 作者：405859
     * 日期： 2017年3月2日 上午10:50:30
     * @param fileName
     * @throws IOException
     *</pre>
     */
    protected void export(String fileName) throws IOException {
        ExportExcelNew.setBaseMoreHeader(response,request,fileName);
        ExportExcelNew.releaseBase(response,fileName);
//        ExportExcelNew.close(wb);
    }
    /**
     *<pre>
     * 方法体说明：(获取行对象)
     * 作者：405859
     * 日期： 2017年3月2日 上午10:50:38
     * @param rownum
     * @return
     *</pre>
     */
    protected Row getRow(int rownum){
        return this.sheet.getRow(rownum);
    }
    /**
     *<pre>
     * 方法体说明：(获取最后一个列号)
     * 作者：405859
     * 日期： 2017年3月2日 上午10:50:46
     * @return
     *</pre>
     */
    public int getLastCellNum(){
        return this.getRow(headerNum).getLastCellNum();
    }
    /**
     *<pre>
     * 方法体说明：(反射)
     * 作者：405859
     * 日期： 2017年3月2日 上午10:49:49
     * @param cls
     *</pre>
     */
    protected void fieldsAsList(Class cls){
        Field[] fields = cls.getDeclaredFields();
        for(Field field:fields){
            if(StringUtils.isNotBlank(field.getName())){
                if(!field.getType().toString().startsWith("class java") &&
                        field.getType().toString().startsWith("class com")){
                    String className = field.getType().toString();
                    try {
                        int index = className.indexOf(" ");
                        className = className.substring(index+1);
                        Class classs = Class.forName(className);
                        for(Field fieldAttr:classs.getDeclaredFields()){
                            fieldList.add(fieldAttr.getName());
                            fieldMap.put(fieldAttr.getName(),field.getType());
                        }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                        logger.error("ClassNotFoundException 没有找到类 {}",className);
                    }
                }
                fieldList.add(field.getName());
                fieldMap.put(field.getName(),field.getType());
            }
        }
        Field[] superFields = cls.getSuperclass().getDeclaredFields();
        if(superFields.length > 0){
            fieldsAsList(cls.getSuperclass());
        }
    }
    /**
     *<pre>
     * 方法体说明：(创建中间样式)
     * 作者：405859
     * 日期： 2017年3月2日 上午10:50:59
     * @return
     *</pre>
     */
    protected CellStyle createCenterStyle(){
        CellStyle centerStyle = ExportExcelNew.createTextStyle(wb);
        centerStyle.setBorderTop(CellStyle.SOLID_FOREGROUND);
        centerStyle.setBorderLeft(CellStyle.SOLID_FOREGROUND);
        centerStyle.setBorderBottom(CellStyle.SOLID_FOREGROUND);
        centerStyle.setBorderRight(CellStyle.SOLID_FOREGROUND);
        centerStyle.setTopBorderColor(HSSFColor.BLACK.index);
        centerStyle.setLeftBorderColor(HSSFColor.BLACK.index);
        centerStyle.setRightBorderColor(HSSFColor.BLACK.index);
        centerStyle.setBottomBorderColor(HSSFColor.BLACK.index);
        centerStyle.setAlignment(CellStyle.ALIGN_CENTER);
        return centerStyle;
    }
    /**
     *<pre>
     * 方法体说明：(创建左边样式)
     * 作者：405859
     * 日期： 2017年3月2日 上午10:51:06
     * @return
     *</pre>
     */
    protected CellStyle createLeftStyle(){
        CellStyle leftStyle = ExportExcelNew.createTextStyle(wb);
        leftStyle.setBorderTop(CellStyle.SOLID_FOREGROUND);
        leftStyle.setBorderLeft(CellStyle.SOLID_FOREGROUND);
        leftStyle.setBorderBottom(CellStyle.SOLID_FOREGROUND);
        leftStyle.setBorderRight(CellStyle.SOLID_FOREGROUND);
        leftStyle.setTopBorderColor(HSSFColor.BLACK.index);
        leftStyle.setLeftBorderColor(HSSFColor.BLACK.index);
        leftStyle.setRightBorderColor(HSSFColor.BLACK.index);
        leftStyle.setBottomBorderColor(HSSFColor.BLACK.index);
        leftStyle.setAlignment(CellStyle.ALIGN_LEFT);
        leftStyle.setWrapText(true);
        return leftStyle;
    }
//    public static void main(String[] args) {
//        Class cls = new TradeOrderEsDO().getClass();
//        Field[] fields = cls.getDeclaredFields();
//        for(Field field:fields){
//            if(StringUtils.isNotBlank(field.getName())){
//                System.out.println(field.getType());
//                if(!field.getType().toString().startsWith("class java") &&
//                        field.getType().toString().startsWith("class com")){
//                    try {
//                        System.out.println("进来了");
//                        String className = field.getType().toString();
//                        int index = className.indexOf(" ");
//                        className = className.substring(index+1);
//                        System.out.println(className);
//                        Class classs = Class.forName(className);
//                        System.out.println(JSONObject.toJSONString(classs.getDeclaredFields()));
//                    } catch (ClassNotFoundException e) {
//                        System.out.println("错误");
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }
//    }
    /**
     *<pre>
     * 方法体说明：(获取输入流)
     * 作者：405859
     * 日期： 2017年3月2日 上午10:51:27
     * @return
     *</pre>
     */
//    public InputStream getInputStream(){
//        InputStream excelStream = null;
//        try {
//            ByteArrayOutputStream out = new ByteArrayOutputStream();
//            this.wb.write(out);
//            excelStream = new ByteArrayInputStream(out.toByteArray());
//        } catch (IOException e) {
//        	//日志
//            logger.error(ExceptionUtils.getStackTrace(e));
//        } finally {
//            try {
//                wb.close();
//            } catch (IOException e) {
//            	//日志
//                logger.error(ExceptionUtils.getStackTrace(e));
//            }
//        }
//        //返回
//        return excelStream;
//    }
}
