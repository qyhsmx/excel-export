package com.sto.customerapp.excel.employee.config;

import javax.sql.DataSource;

import org.apache.ibatis.io.VFS;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;

@Configuration
@MapperScan(basePackages = DataSourceConfig.MASTERMAPPERPACKAGE, sqlSessionFactoryRef="masterSqlSessionFactory")
public class DataSourceConfig {
	
	static final String MASTERMAPPERPACKAGE = "com.sto.customerapp.employee.dao";
	static final String MASTERMAPPERLOCATION = "classpath:com/sto/customerapp/excel/employee/dao/*.xml";
	static final String MASTERALIASESPACKAGE = "com.sto.customerapp.excel.employee.entity";

	@Bean(name = "dataSourceOne")
	@ConfigurationProperties("spring.datasource.druid.one")
	@Primary
	public DataSource dataSource() {
		return DruidDataSourceBuilder.create().build();
	}

	@Bean(name = "masterTransactionManager")
	@Primary
	public DataSourceTransactionManager masterTransactionManager(@Qualifier("dataSourceOne") DataSource dataSourceOne) {
		return new DataSourceTransactionManager(dataSourceOne);
	}

	@Bean(name = "masterSqlSessionFactory")
	@Primary
	public SqlSessionFactory masterSqlSessionFactory(@Qualifier("dataSourceOne") DataSource dataSourceOne)
			throws Exception {
		
		//解决myBatis下 不能嵌套jar文件的问题
		VFS.addImplClass(SpringBootVFS.class);
		
		final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
		sessionFactory.setDataSource(dataSourceOne);
		sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(MASTERMAPPERLOCATION));
		sessionFactory.setTypeAliasesPackage(MASTERALIASESPACKAGE);
		return sessionFactory.getObject();
	}
}
