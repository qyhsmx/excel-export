/**
 * @author 由丕林
 * @date   2018-12-12
 * @p      此接口定义了需要执行导出的接口方法，不管采用何种导出方式，不管导出成何种文件格式，必须实现该接口
 * 
 */
package com.sto.customerapp.excel.employee.concurrent;

import com.sto.customerapp.excel.employee.entity.DownExcel;

public interface STOOutPutToFiles {
	
	public void init();
	
	public void execute(DownExcel de);
	
	public void exit();
	
}
