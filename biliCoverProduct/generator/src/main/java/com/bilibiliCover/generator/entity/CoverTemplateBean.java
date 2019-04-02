package com.bilibiliCover.generator.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 封面模板表
 * @author kayden
 * @version 0.0.1
 * @createTime 2019年2月7日
 * @package com.bilibiliCover.generator.entity
 */
@Accessors(chain = true)
@Table(name = "coverTemplate")
@Entity
@Data
public class CoverTemplateBean {
	
	/** 唯一id*/
	@Id
	private Integer id;
	
	/** 唯一kid*/
	@Column(name = "kid")
	private String kid;
	
	/** 模板代码*/
	@Column(name = "templateCode")
	private String templateCode;
	
	/** 代码名字*/
	@Column(name = "templateName")
	private String templateName;
	
	/** 代码类型*/
	@Column(name = "templateType")
	private String templateType;
	
	/** 是否删除*/
	@Column(name = "isdel")
	private Boolean isdel;
	
	/** 创建时间*/
	@Column(name = "createTime")
	private Timestamp createTime;
	
	/** 删除时间*/
	@Column(name = "delTime")
	private Timestamp delTime;
	
	/** 模板简介250字内*/
	@Column(name = "introduction")
	private String introduction;
	
	/** 模板预览图kid，对应uploadFile表*/
	@Column(name = "previewImage")
	private String previewImage;
}
