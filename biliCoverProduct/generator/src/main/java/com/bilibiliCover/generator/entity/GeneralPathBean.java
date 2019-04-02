package com.bilibiliCover.generator.entity;

import lombok.Data;

/**
 * 特殊形状遮罩操作BO(business object) 业务对象
 * （此为原子对象，一般以一个List<GeneralPathBean>进行生成遮罩）
 * @author kayden
 * @version 0.0.1
 * @createTime 2019年2月5日
 * @package com.bilibiliCover.generator.entity
 */
@Data
public class GeneralPathBean {
	
	private String type; // 根据操作类型来进行不同的方法 
	//moveTo:移动到某点 lineTo:连线到某点 quadTo: 二次曲线片段 curveTo：三次曲线片段 closePath：闭合遮罩 reset: 重置遮罩，恢复整个页面
	
	private Integer pointX; // 点的X坐标
	
	private Integer pointY; // 点的Y坐标
	
	private Integer bezierX1 ; // 二次曲线片段扭曲点X
	
	private Integer bezierY1; // 二次曲线片段扭曲点Y
	
	private Integer bezierX2 ; // 三次曲线片段扭曲点X
	
	private Integer bezierY2; // 三次曲线片段扭曲点Y
}
