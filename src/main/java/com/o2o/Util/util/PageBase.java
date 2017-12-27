package com.o2o.Util.util;

import com.deppon.cubc.commons.dao.Query;
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
 * 
 * com.deppon.cubc.web.common.util
 * 
 * @author tangyuhan 2016年11月1日
 *
 */
@SuppressWarnings("unused")
public class PageBase<T> implements Serializable {

	private static final long serialVersionUID = 7094384198859289182L;

	/**
	 * 页码
	 */
	private Integer page = NumberConstants.NUMBER_1;
	
	/**
	 * 显示起始数
	 */
	private Integer offset = NumberConstants.NUMERAL_ZORE;
	
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
	private Collection<T> result;

	private static final String PAGE_TOTAL = "total";
	private static final String PAGE_ROWS = "rows";

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
		if (limit <= NumberConstants.NUMERAL_ZORE)
			limit = NumberConstants.NUMBER_1;
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
		if (this.total != total) 
			this.total = NumberConstants.LONG_1;
		this.total = total;
	}

	public Integer getPage() {
		int currentPage = this.offset / this.limit + NumberConstants.NUMBER_1;
		if (currentPage <= NumberConstants.NUMERAL_ZORE)
			currentPage = NumberConstants.NUMBER_1;
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

	public Collection<T> getResult() {
		return result;
	}

	public void setResult(Collection<T> result) {
		this.result = result;
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
	 */
	public Object resultJSON(ResultList<T> resultList) {
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
	 * Controller 返回类型
	 */
	public Object toJSON() {
		if(CollectionUtils.isEmpty(this.result) || null == total){
			return null;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(PAGE_TOTAL, this.total);
		map.put(PAGE_ROWS, this.result);
		return map;
	}

}
