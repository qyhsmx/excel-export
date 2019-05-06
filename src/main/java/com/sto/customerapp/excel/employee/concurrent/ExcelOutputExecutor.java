package com.sto.customerapp.excel.employee.concurrent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sto.customerapp.excel.employee.entity.DownExcel;
import com.sto.customerapp.excel.employee.facade.IDownExcel;

@SuppressWarnings("unused")
public class ExcelOutputExecutor extends Thread {
	Log log = LogFactory.getLog(ExcelOutputExecutor.class);
	STOOutPutToFiles output ;
	DownExcel de ;
	public ExcelOutputExecutor(STOOutPutToFiles output,DownExcel de){
	    this.output=output;
	    this.de = de;
	}
	public void run(){
		log.debug("ExcelOutputExecutor中的deid为 ："+de.getInfoId());
		output.init();
		output.execute(de);
		output.exit();
	}
}
