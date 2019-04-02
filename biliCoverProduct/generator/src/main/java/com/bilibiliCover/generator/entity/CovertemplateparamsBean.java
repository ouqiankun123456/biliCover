package com.bilibiliCover.generator.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 模板参数表
 * @author kayden
 * @version 0.0.1
 * @createTime 2019年2月7日
 * @package com.bilibiliCover.generator.entity
 */
@Accessors(chain = true)
@Table(name = "coverTemplateparams")
@Entity
@Data
public class CovertemplateparamsBean {
	/** 唯一id*/
	@Id
	private Integer id;
	
	/** 唯一kid*/
	@Column(name = "kid")
	private String kid;
	
	/** 创建时间*/
	@Column(name = "createTime")
	private Timestamp createTime;
	
	/** 模板kid*/
	@Column(name = "templateKid")
	private String templateKid;
	
	/** 参数名字*/
	@Column(name = "paramName")
	private String paramName;
	
	/** 参数的输入类型*/
	@Column(name = "paramType")
	private String paramType;
	
	/** 参数的默认值*/
	@Column(name = "defaultValue")
	private String defaultValue;
}
