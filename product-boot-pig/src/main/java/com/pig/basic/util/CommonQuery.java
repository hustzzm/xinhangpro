package com.pig.basic.util;

import org.springframework.util.StringUtils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 查询参数
 */
public class CommonQuery extends LinkedHashMap<String, Object> {
	// 当前页码
	private int current = 1;
	// 每页条数
	private int size = 20;
	// 排序列
	public String sortName;
	// 排序方式
	public String order;


	public CommonQuery() {
	}

	public CommonQuery(Map<String, Object> params) {
		if (params.get("current") != null) {
			this.current = Integer.parseInt(params.get("current").toString());
		}
		if (params.get("size") != null) {
			this.size = Integer.parseInt(params.get("size").toString());
		}
		params.forEach((k,v)->{
			if(!StringUtils.isEmpty(v)){
				this.put(k,v);
			}
		});
//		this.putAll(params);
		this.remove("current");
		this.remove("size");
	}

	public int getCurrent() {
		return current;
	}

	public void setCurrent(int current) {
		this.current = current;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String getSortName() {
		return sortName;
	}

	public void setSortName(String sortName) {
		this.sortName = sortName;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}
}
