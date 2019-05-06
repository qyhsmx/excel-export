/**
 * @author 由丕林
 * @date   2018-12-12
 * @p      一个导出接口的excel实现
 */
package com.sto.customerapp.excel.employee.concurrent;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sto.customerapp.excel.employee.entity.DownExcel;

public abstract class STOOutputToFileFacade implements STOOutPutToFiles {

	Log log = LogFactory.getLog(STOOutputToFileFacade.class);
	
	private Date startdate;
	private Date enddate;
	@Override
	public void init() {
		startdate = new Date();
		log.debug("initmethod excuted   timestart : "+startdate);
	}

	@Override
	public abstract void execute(DownExcel de) ;

	@Override
	public void exit() {
		enddate = new Date();
		long diff = enddate.getTime() - startdate.getTime();//这样得到的差值是毫秒级别  
	    long days = diff / (1000 * 60 * 60 * 24);  	   
	    long hours = (diff-days*(1000 * 60 * 60 * 24))/(1000* 60 * 60);  
	    long minutes = (diff-days*(1000 * 60 * 60 * 24)-hours*(1000* 60 * 60))/(1000* 60);  
	    long seconds = (diff-days*(1000 * 60 * 60 * 24)-hours*(1000* 60 * 60)-minutes*(1000*60))/1000; 
		log.debug("exitmethod excuted   timestart : "+enddate);
		log.info("导出excel花费的时间为["+days+"天"+hours+"小时"+minutes+"分"+seconds+"秒]");
	}
	
}
