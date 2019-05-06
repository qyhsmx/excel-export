package com.sto.customerapp.excel.employee.facade;

import java.util.List;
import java.util.Map;

import com.sto.customerapp.excel.employee.entity.DownExcel;


public interface IDownExcel {
	
    void lockRecordBySeqId(@SuppressWarnings("rawtypes") Map paramsMap);

    int updateDownInfo(DownExcel de);

    List<DownExcel> queryDownInfo(@SuppressWarnings("rawtypes") Map params);
    
    List<DownExcel> queryDownInfoAll(@SuppressWarnings("rawtypes") Map params);
    
    @SuppressWarnings("rawtypes")
	List<Map> select(String sql);
}
