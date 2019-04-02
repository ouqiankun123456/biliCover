package com.bilibiliCover.generator.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 图片操作BO(business object) 业务对象
 * @author kayden
 * @version 0.0.1
 * @createTime 2019年1月29日
 * @package com.bilibiliCover.generator.entity
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class ImageBean extends BaseCoverBean{
	private String imagePath; // 图片路径
	
	private int x; // 坐标x
	
	private int y; // 坐标y
	
	private int width; // 宽度
	
	private int height; // 高度
}
