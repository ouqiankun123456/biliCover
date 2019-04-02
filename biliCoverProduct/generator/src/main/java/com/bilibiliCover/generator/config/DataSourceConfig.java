package com.bilibiliCover.generator.config;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.druid.pool.DruidDataSource;

import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "spring.datasource")
@Data
public class DataSourceConfig {
 
	private String url;
	private String username;
	private String password;
 
	@Bean
	public DataSource getDataSource() {
		DruidDataSource dataSource = new DruidDataSource();
		dataSource.setUrl(url);
		dataSource.setUsername(username);// 用户名
		dataSource.setPassword(password);// 密码
		dataSource.setMaxWait(50000);
		dataSource.setMaxActive(30);
		dataSource.setTestOnBorrow(true);
		return dataSource;
	}
}
