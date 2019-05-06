package com.sto.customerapp.excel.employee.concurrent;

import java.util.List;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.sto.customerapp.excel.employee.common.CheckUtils;
import com.sto.customerapp.excel.employee.common.CheckUtilsDefault;
import com.sto.customerapp.excel.employee.entity.DownExcel;


public abstract class OutputToFileJobFacade implements OutputToFileJob {	
	public List<DownExcel> des;		
	private STOOutPutToFiles outPutToFiles;

    private CheckUtils check;
    
    public CheckUtils getCheck() {
		return check;
	}

	public void setCheck(CheckUtils check) {
		this.check = check;
	}
	
	@Override
	public void execute(ShardingContext shardingContext) {
		//默认为CheckUtilsDefault校验规则
		this.setCheck(new CheckUtilsDefault());
		initOutputFilesThread();
		//为每一个符合导出条件的记录创建线程，放到线程池中
		for (DownExcel de : des){	
			outputToExcelJobService.submit(new ExcelOutputExecutor(outPutToFiles,de));
		}
		exitOutputFilesThread();
	}

	public abstract void initOutputFilesThread();
	
	public abstract void exitOutputFilesThread();

	public void setDownExcels(List<DownExcel> des) {
		this.des = des;
	}

	public STOOutPutToFiles getOutPutToFiles() {
		return outPutToFiles;
	}

	public void setOutPutToFiles(STOOutPutToFiles outPutToFiles) {
		this.outPutToFiles = outPutToFiles;
	}

}
