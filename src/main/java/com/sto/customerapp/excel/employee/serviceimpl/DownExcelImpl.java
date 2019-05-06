package com.sto.customerapp.excel.employee.serviceimpl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sto.customerapp.excel.employee.dao.DownExcelDao;
import com.sto.customerapp.excel.employee.entity.DownExcel;
import com.sto.customerapp.excel.employee.facade.IDownExcel;

@Service
@SuppressWarnings("rawtypes")
public class DownExcelImpl implements IDownExcel {

	@Autowired
	private DownExcelDao downExcelDao;

	
	@Override
	public void lockRecordBySeqId(Map paramsMap) {
		downExcelDao.lockRecordBySeqId(paramsMap);
	}

	@Override
	public int updateDownInfo(DownExcel de) {
		return downExcelDao.updateDownInfo(de);
	}

	@Override
	public List<DownExcel> queryDownInfo(Map params) {
		return downExcelDao.queryDownInfo(params);
	}
	
	@Override
	public List<DownExcel> queryDownInfoAll(Map params) {
		return downExcelDao.queryDownInfoAll(params);
	}

	@Override
	public List<Map> select(String sql) {
		return downExcelDao.select(sql);
	}

}
