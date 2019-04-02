package com.bilibiliCover.generator.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 上传文件管理表
 * @author kayden
 * @version 0.0.1
 * @createTime 2019年2月11日
 * @package com.bilibiliCover.generator.entity
 */
@Accessors(chain = true)
@Table(name = "uploadFile")
@Data
@Entity
public class UploadFileBean {

	/** 唯一id*/
	@Id
	private Integer id;
	
	/** 唯一kid*/
	@Column(name = "kid")
	private String kid;
	
	/** 保存路径*/
	@Column(name = "savePath")
	private String savePath;
	
	/** 后缀*/
	@Column(name = "ext")
	private String ext;
	
	/** 是否删除*/
	@Column(name = "isdel")
	private Boolean isdel;
	
	/** 创建时间*/
	@Column(name = "createTime")
	private Timestamp createTime;
	
	/** 删除时间*/
	@Column(name = "delTime")
	private Timestamp delTime;
	 
}
