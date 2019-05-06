package com.sto.customerapp.excel.employee.job;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.cxytiandi.elasticjob.annotation.ElasticJobConf;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.sto.customerapp.excel.employee.common.CheckUtils;
import com.sto.customerapp.excel.employee.common.CheckUtilsDefault;
import com.sto.customerapp.excel.employee.concurrent.ExcelOutputExecutor;
import com.sto.customerapp.excel.employee.concurrent.OutputToFileJobFacade;
import com.sto.customerapp.excel.employee.concurrent.STOOutPutToFiles;
import com.sto.customerapp.excel.employee.entity.DownExcel;
import com.sto.customerapp.excel.employee.facade.IDownExcel;

@SuppressWarnings("all")

@ElasticJobConf(name = "ExportExcelJob", cron = "0 0/2 * * * ?", shardingItemParameters = "0=0,1=1", description = "ExportExcelJob")
public class ExportExcelJob extends OutputToFileJobFacade implements SimpleJob {  
	Log log = LogFactory.getLog(ExportExcelJob.class);

	List<STOOutPutToFiles> outputs = new ArrayList<STOOutPutToFiles>();
	
    @Value("${excelpath}")
    private String excelpath;

    @Value("${seviceIp}")
    private String seviceIp;
    
    @Autowired
    private IDownExcel downExcleService;

	Map paramsMap = new HashMap();
    
	public void initOutputFilesThread() {            
        paramsMap.put("seviceIp", seviceIp);
        List<DownExcel> des = downExcleService.queryDownInfoAll(paramsMap);

        //通过校验方法得到符合规范的downexcel对象集合
        List<DownExcel> validdes = getValidDownExcels(des);       
        log.info("downExcel 导出失败和并未导出的记录共有["+des.size()+" ] 条,"
        		+ "符合导出数据的记录有  [ " + validdes.size()+" ]条");
        
        //将符合初步筛选规范的数据封装成des列表，回传给父类创建线程
        this.setDownExcels(validdes);
        
        //声明一个具体的导出实现，赋值，并上传给父类创建线程
        STOOutPutExcelImpl outputtofiles = new STOOutPutExcelImpl();
        outputtofiles.setDownExcleService(downExcleService);
        outputtofiles.setExcelpath(excelpath);
        
        this.setOutPutToFiles(outputtofiles);
	}

	private List<DownExcel> getValidDownExcels(List<DownExcel> des) {
		List<DownExcel> validdes = new ArrayList<DownExcel>();
        for (DownExcel de :des){
        	if (this.getCheck().isValidJson(de.getQueryJson()))
        		validdes.add(de);
        }
		return validdes;
	}


	public void exitOutputFilesThread() {
		paramsMap.clear();
		outputs.clear();
	}

	/**
	 * 冗余方法，因为@ElasticJobConf必须指定为simplejob实现类，所以，该方法必须定义，重载父类方法即可
	 */
	@Override
	public void execute(ShardingContext shardingContext) {
		super.execute(shardingContext);
	}
}
