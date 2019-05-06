package com.sto.customerapp.excel.employee.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.dangdang.ddframe.job.api.simple.SimpleJob;


public interface OutputToFileJob extends SimpleJob {
    //导出excel线程池
    public ExecutorService outputToExcelJobService=Executors.newCachedThreadPool();
    
}
