package com.bilibiliCover.generator.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 旋转操作BO(business object) 业务对象
 * @author kayden
 * @version 0.0.1
 * @createTime 2019年1月30日
 * @package com.bilibiliCover.generator.entity
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class RotateBean extends BaseCoverBean {
	
	private double theta; // 旋转角度，可负数
	 
	private double x; // 围绕x坐标旋转
	 
	private double y; // 围绕y坐标旋转

}
