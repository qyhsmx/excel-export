package com.sto.customerapp.excel.employee.entity;

import java.io.Serializable;

public class DownExcel implements Serializable{
	private static final long serialVersionUID = 1L;
	private String infoId;//记录编号
	private String downSite;//下载Excel的网点名称
	private String downTime;//导出时间
	private String queryJson;//查询条件
	private String finishOrNot;//是否完成
	private String moduleType;//模块名称
	private String exceltype;//导出类型
	private String downUrl;//导出Excel地址
	private String lockSign;//锁定标识
	private String lockTime;//锁定时间
	private String serviceIp;//服务器IP
	private String finishDownLoad;//是否下载
	
	public String getInfoId() {
		return infoId;
	}
	public void setInfoId(String infoId) {
		this.infoId = infoId;
	}
	public String getDownSite() {
		return downSite;
	}
	public void setDownSite(String downSite) {
		this.downSite = downSite;
	}
	public String getDownTime() {
		return downTime;
	}
	public void setDownTime(String downTime) {
		this.downTime = downTime;
	}
	public String getQueryJson() {
		return queryJson;
	}
	public void setQueryJson(String queryJson) {
		this.queryJson = queryJson;
	}
	public String getFinishOrNot() {
		return finishOrNot;
	}
	public void setFinishOrNot(String finishOrNot) {
		this.finishOrNot = finishOrNot;
	}
	public String getModuleType() {
		return moduleType;
	}
	public void setModuleType(String moduleType) {
		this.moduleType = moduleType;
	}
	public String getExceltype() {
		return exceltype;
	}
	public void setExceltype(String exceltype) {
		this.exceltype = exceltype;
	}
	public String getDownUrl() {
		return downUrl;
	}
	public void setDownUrl(String downUrl) {
		this.downUrl = downUrl;
	}
	public String getLockSign() {
		return lockSign;
	}
	public void setLockSign(String lockSign) {
		this.lockSign = lockSign;
	}
	public String getLockTime() {
		return lockTime;
	}
	public void setLockTime(String lockTime) {
		this.lockTime = lockTime;
	}
	public String getServiceIp() {
		return serviceIp;
	}
	public void setServiceIp(String serviceIP) {
		this.serviceIp = serviceIP;
	}
	public String getFinishDownLoad() {
		return finishDownLoad;
	}
	public void setFinishDownLoad(String finishDownLoad) {
		this.finishDownLoad = finishDownLoad;
	}
	
}
