package com.bilibiliCover.generator.entity;

import com.bilibiliCover.generator.constant.PrintTypeEnum;

import lombok.Data;

/**
 * 基础画图生成BO(business object) 业务对象
 * @author kayden
 * @version 0.0.1
 * @createTime 2019年1月30日
 * @package com.bilibiliCover.generator.entity
 */
@Data
public class BaseCoverBean {

	private int step; // 步数
	
	private String beanType; // 继承类类型
	
	public PrintTypeEnum getBeanType(){
		return PrintTypeEnum.tran(beanType);
	}
}
