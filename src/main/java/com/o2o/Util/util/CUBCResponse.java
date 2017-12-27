/** 
 * Project Name:CUBC-Web-1030 
 * File Name:CUBCResponse.java 
 * Package Name:com.deppon.cubc.web.common.util 
 * Date:2016年11月19日下午8:56:03 
 * Copyright (c) 2016, chenzhou1025@126.com All Rights Reserved. 
 * 
*/  
  
package com.o2o.Util.util;

import com.deppon.cubc.commons.dao.Query;
import com.deppon.cubc.commons.dao.Result;
import com.deppon.cubc.commons.dao.ResultList;
import com.deppon.cubc.web.common.constant.NumberConstants;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/** 
 * ClassName:CUBCResponse <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Reason:   TODO ADD REASON. <br/> 
 * Date:     2016年11月19日 下午8:56:03 <br/> 
 * @author   380853 
 * @version   
 * @since    JDK 1.6 
 * @see       
 */
public class CUBCResponse<T> implements Serializable{

	
	public static final String SUCCESS="1";
	public static final String FAILURE="-1";
	private static final long serialVersionUID = 7094384198859289182L;
	private String status;
	private String msg;
	
	
	public static  void responseFailure(CUBCResponse<?> page,String msg){
		page.setStatus(CUBCResponse.FAILURE);
		page.setMsg(msg);
	}
	public static void responseSuccess(CUBCResponse<?> page,String msg){
		page.setStatus(CUBCResponse.SUCCESS);
		page.setMsg(msg);
	}
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	/**
	 * 页码
	 */
	@SuppressWarnings("unused")
	private Integer page = 1;
	
	/**
	 * 显示起始数
	 */
	private Integer offset = 0;
	
	/**
	 * 显示多少页
	 */
	private Integer limit = NumberConstants.NUMBER_10;
	
	/**
	 * 总数量
	 */
	private Long total;

	/**
	 * 排序名称
	 */
	private String sort;

	/**
	 * 倒序：desc
	 * 顺序：asc
	 */
	private String order;
	
	/**
	 * 请求JSON数据(第一种格式)
	 */
	private String requestData;
	
	/**
	 * 请求实体数据(第二种格式)
	 */
	private T requestEntity;
	
	/**
	 * 请求Map数据(第三种格式)
	 */
	private Map<String, Object> requestMap = new HashMap<String, Object>();
	
	/**
	 * 响应結果集
	 */
	private Collection<T> resultList;
	
	private T result;
	

	public T getResult() {
		return result;
	}
	public void setResult(T result) {
		this.result = result;
	}

	private static final String PAGE_TOTAL = "total";
	private static final String PAGE_ROWS = "rows";
	private static final String ROW = "result";

	
	
	
	public Collection<T> getResultList() {
		return resultList;
	}
	public void setResultList(Collection<T> resultList) {
		this.resultList = resultList;
	}
	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		if (limit <= 0)
			limit = 1;
		this.limit = limit;
	}

	public String getRequestData() {
		return requestData;
	}

	public void setRequestData(String requestData) {
		this.requestData = requestData;
	}

	

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		if (this.total!=null&&this.total .equals( total) )
			this.total = 1L;
		this.total = total;
	}

	public Integer getPage() {
		int currentPage = this.offset / this.limit + 1;
		if (currentPage <= 0)
			currentPage = 1;
		return currentPage;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	

	public T getRequestEntity() {
		return requestEntity;
	}

	public void setRequestEntity(T requestEntity) {
		this.requestEntity = requestEntity;
	}

	public Map<String, Object> getRequestMap() {
		return requestMap;
	}

	public void setRequestMap(Map<String, Object> requestMap) {
		this.requestMap = requestMap;
	}

	/**
	 * 封装中台调用Query
	 * @param page
	 * @return
     */
	public Query<T> encapsulationQuery(PageBase<T> page){
		Query<T> query = new Query<T>();
		if(ObjectUtils.notEqual(null,this.requestEntity)){
			query.setData(getRequestEntity());
		}
		if(StringUtils.isNotBlank(this.sort) && StringUtils.isNotBlank(this.order)){
			// oraderBy = sort+" "+order
			StringBuffer string = new StringBuffer(getSort());
			string.append(" ");
			string.append(getOrder());
			query.setOrderBy(string.toString());
		}
		query.setStart(getOffset());
		query.setPageSize(getLimit());
		return query;
	}

	/**
	 * Controller 封装中台返回值
	 * 分页查询结果
	 */
	public Object resultListJSON(ResultList<T> resultList) {
		if(ObjectUtils.notEqual(null,resultList)){
			if(resultList.getTotal()>0){
				Map<String, Object> map = new HashMap<String, Object>();
				map.put(PAGE_TOTAL, resultList.getTotal());
				map.put(PAGE_ROWS, resultList.getDatalist());
				return map;
			}
		}
		return null;
	}
	/**
	 * 非分页查询结果
	 * @param result
	 * @return
	 */
	public Object resultJSON(Result<T> result) {
		if(ObjectUtils.notEqual(null,result)){
				Map<String, Object> map = new HashMap<String, Object>();
				map.put(ROW, result.getData());
				return map;
			}
		return null;
	}

	/**
	 * Controller 返回类型
	 */
	public Object toJSON() {
		if((CollectionUtils.isEmpty(this.resultList) || null == total)&&result==null){
			return null;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(PAGE_TOTAL, this.total);
		map.put(PAGE_ROWS, this.resultList);
		map.put(ROW, this.result);
		return map;
	}


}
  