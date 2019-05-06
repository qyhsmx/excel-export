package com.sto.customerapp.excel.employee.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.sto.customerapp.excel.employee.entity.DownExcel;

@Mapper
@SuppressWarnings("rawtypes")
public interface DownExcelDao {

	public void lockRecordBySeqId(Map paramsMap);

	public int updateDownInfo(DownExcel de);

	public List<DownExcel> queryDownInfo(Map params);
	
	public List<DownExcel> queryDownInfoAll(Map params);
		
	public List<Map> select(String sql);
}
