package com.sto.customerapp.excel.employee.job;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sto.customerapp.excel.employee.common.RandomUtils;
import com.sto.customerapp.excel.employee.concurrent.STOOutputToFileFacade;
import com.sto.customerapp.excel.employee.entity.DownExcel;
import com.sto.customerapp.excel.employee.facade.IDownExcel;

public class STOOutPutExcelImpl extends STOOutputToFileFacade {
	private final static String ISFINISH_UPDATING = "2";// 导出中。。。。。
	private final static String ISFINISH_SUCCESSED = "1";// 导出成功
	private final static String ISFINISH_FAILED = "-1";// 导出失败
	Log log = LogFactory.getLog(STOOutPutExcelImpl.class);

	// 定义锁
	private ReentrantLock lock = new ReentrantLock();

	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

	private String excelpath;

	private IDownExcel downExcleService;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void execute(DownExcel de) {

		// 创建工作表
		XSSFWorkbook workBook = new XSSFWorkbook();
		

		Gson gson = new Gson();
		Map queryjson = gson.fromJson(de.getQueryJson(),
				new TypeToken<Map<String, String>>() {
				}.getType());
		log.debug("QueryJson:" + de.getQueryJson() + " and map 解析结果为："
				+ queryjson.toString());
		Map paramsMap = new HashMap();
		paramsMap.put("seqid", de.getInfoId());
		String fileName = de.getModuleType() + "_"
				+ RandomUtils.generateOrderNo(4) + ".xlsx";
		String resultFileName = getFullPath(fileName);

		try {
			// 更新导出状态为正在导出
			paramsMap.put("flagnumber", ISFINISH_UPDATING);
			log.debug("querystr : " + queryjson.get("querystr"));
			String[] titles = ((String) queryjson.get("columnname")).split(",");
			int querycount = Integer.parseInt((String)queryjson.get("queryCount"));
			
			if (querycount<50000){
				lock.lock();
				// 根据传入json得到需要导出的数据
				List<Map> result = downExcleService.select((String) queryjson
						.get("querystr"));
				downExcleService.lockRecordBySeqId(paramsMap);
				lock.unlock();	
				// 填充excel数据
				XSSFSheet sheet = workBook.createSheet("sheet1");
				fillDataToExcelObject(sheet, titles, result);
			}else//需要建立多个sheet
			{
				int pagesize = 50000;
				int pagecount = querycount / pagesize +1;
				log.info(de.getInfoId()+" 总共有 【 " +pagecount +" 】页数据需要导出。");
				for (int i = 1 ;i<=pagecount;i++){
					String querystr = "SELECT * FROM ( SELECT T.*, ROWNUM rn FROM (" + 
						queryjson.get("querystr")+" ) T  WHERE  ROWNUM <=  "+pagesize * i+ ")"+
							"WHERE rn >= " + (i - 1) * pagesize;
					log.info(de.getInfoId()+" 拼接成的sql为 【 " +querystr +" 】");
					lock.lock();
					// 根据传入json得到需要导出的数据
					List<Map> result = downExcleService.select((String) queryjson.get("querystr"));
					log.info("数据库查询出的记录数为："+result.size()+" 条");
					downExcleService.lockRecordBySeqId(paramsMap);
					lock.unlock();	
					// 填充excel数据
					XSSFSheet sheet = workBook.createSheet("sheet"+i);
					log.info("第"+i+"页数据导入Excel开始于："+new Date());
					fillDataToExcelObject(sheet, titles, result);
					log.info("第"+i+"页数据导入Excel结束于："+new Date());
					result.clear();
				}
			}
			isChartPathExist();
			FileOutputStream outputStream = new FileOutputStream(new File(resultFileName));
			workBook.write(outputStream);
			log.info("文件[" + fileName + "]导出成功，本次导出数据为：[" + querycount  + "]条， 存放地址为：" + resultFileName);
			
			
			// 更新导出状态为导出成功
			de.setFinishOrNot(ISFINISH_SUCCESSED);
			de.setDownUrl(resultFileName);
			lock.lock();
			downExcleService.updateDownInfo(de);
			lock.unlock();
		} catch (Exception e) {
			paramsMap.put("flagnumber", ISFINISH_FAILED);
			lock.lock();
			// 更新导出状态为导出失败
			downExcleService.lockRecordBySeqId(paramsMap);
			lock.unlock();
			log.error("文件[" + fileName + "]导出失败，具体失败原因为：", e);
			e.printStackTrace();
		} finally {
			log.debug(de.getInfoId() + "释放");
			try {
				workBook.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void isChartPathExist() {
		File file = new File(getExcelpath() + File.separator + "downExcel"
				+ File.separator + sdf.format(new Date()));
		if (!file.exists())
			file.mkdirs();
	}

	private String getFullPath(String fileName) {
		String resultFileName = getExcelpath() + File.separator + "downExcel"
				+ File.separator + sdf.format(new Date()) + File.separator
				+ fileName;
		log.debug("resultFileName:" + resultFileName);
		return resultFileName;
	}

	/**
	 * 填充excel数据
	 * 
	 * @param sheet
	 *            填充的sheet对象
	 * @param titles
	 *            标题
	 * @param result
	 *            需要填充的数据对象集合
	 */
	@SuppressWarnings("rawtypes")
	private void fillDataToExcelObject(XSSFSheet sheet, String[] titles,
			List<Map> result) {
		Row rowtitle = sheet.createRow(0);// 创建表头
		for (int i = 0; i < titles.length; i++) {
			Cell cell = rowtitle.createCell(i);
			cell.setCellValue(titles[i]);
		}
		for (int i = 1; i <= result.size(); i++) {
			XSSFRow rowdetail = sheet.createRow(i);
			Map map = result.get(i - 1);
			log.debug("map info :" + map);
			for (int j = 0; j < titles.length; j++) {
				rowdetail.createCell(j).setCellValue(
						(String) map.get(titles[j]));
			}
		}
	}

	public String getExcelpath() {
		return excelpath;
	}

	public void setExcelpath(String excelpath) {
		this.excelpath = excelpath;
	}

	public IDownExcel getDownExcleService() {
		return downExcleService;
	}

	public void setDownExcleService(IDownExcel downExcleService) {
		this.downExcleService = downExcleService;
	}
	
	public static void  main(String[] args){
		int i = 405000 / 50000;
		System.out.println("i:"+i);
		
	}
}
