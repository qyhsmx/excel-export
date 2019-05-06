/*
 * @author 由丕林
 * @date   2018-12-13
 * @p      关于校验，这里做一下详细说明
 *         整个框架是基于不影响现存代码和逻辑的基础上进行的设计，也就是说，之前已经生成好的导出数据是不会进入该框架的处理逻辑的。
 *         第二点，即使是新开发的模块，如果想要使用本框架进行自动导出，传入的数据也需要满足一定的条件。
 *         当前版本为初始版本，简单粗暴，准入条件只有两个：再TAB_EXPORT_EXCEL表中的QUERYCRITERIA字段中，必须包含columnname和querystr两个字段
 *         模板模式如下
 *         "columnname":"编号,职位",
 *         "querystr":"select code as 编号 , DUTY as 职位 from T_BASE_EMPLOYEE where code like '%51002%'",
 *         如果传入的json字符串中没有这两个字段，也会被过滤出系统。
 *         
 *         
 */
package com.sto.customerapp.excel.employee.common;

import java.util.Arrays;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class CheckUtilsDefault implements CheckUtils {
	static Log log = LogFactory.getLog(CheckUtilsDefault.class);
	/**
	 * 该方法并不是检验json的有效性，而是校验json中是否有足够的满足自动导出的信息
	 * 当前版本需要的至少json中需要包含 columnname ，querystr这两个字段，
	 * 任何不包含这两个字段的json视作无效文件，不做处理
	 * @param json
	 * @return
	 */
	
	public boolean isValidJson(String json){	
		Gson gson = new Gson();
		@SuppressWarnings("rawtypes")
		Map queryjson = gson.fromJson(json,
				new TypeToken<Map<String, String>>() {
				}.getType());
		if (queryjson.get("columnname") != null
				&& queryjson.get("querystr") != null){
			String[] columnname = ((String)queryjson.get("columnname")).split(",");
			StringBuffer sb = new StringBuffer(((String)queryjson.get("querystr")).toLowerCase());
			String[] querystr = sb.substring(0, sb.indexOf("from")).split(",");
			if (columnname.length != querystr.length){
				log.info("columnname中定义的字段数量和数据库查询出的字段数量不一致，columnname中定义的字段为：【"+
						queryjson.get("columnname")+"】,而querystr中定义的查询字段为："
								+ "【"+sb.substring(0, sb.indexOf("from"))+"】");
				return false;
			}
			try {
				for (int i=0;i<querystr.length;i++){
					querystr[i] = querystr[i].substring(querystr[i].indexOf("as")+3, querystr[i].length()).trim();
				}
			} catch (Exception e) {
				log.info("sql字符串格式类似于 code as 编号 ，注意标准赋值格式，以免对应出错！");
				return false;
			}
			Arrays.sort(columnname);
			Arrays.sort(querystr);
			if (Arrays.equals(columnname, querystr))
				return true;
			else
			{
				log.info("【columnname】中定义的字段和数据库查询出的字段名称不一致！请详细核对");
				return false;
			}
		}else{
			log.info("json不包含【columnname】和【querystr】内容,json校验失败");
			return false;
		}
		
	}
}
