package com.bilibiliCover.generator.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 文字操作BO(business object) 业务对象
 * @author kayden
 * @version 0.0.1
 * @createTime 2019年1月29日
 * @package com.bilibiliCover.generator.entity
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class TextBean extends BaseCoverBean {
	
	private boolean verticalText; // 是否垂直文字
	
	private String fontName; // 字体名
	
	private int fontStyle; // 字体样式
	
	private int fontSize; // 字体尺寸
	
	private String textColor; // 字体颜色，格式例子rgba(19, 206, 102, 0.8)
	
	private String borderColor; // 描边颜色，格式例子rgba(19, 206, 102, 0.8)
	
	private float borderWidth; // 描边宽度
	
	private double positionX; // 文本框x坐标
	
	private double positionY; // 文本框y坐标
	
	private int textAreaWidth; // 文本框宽度，用于计算对齐
	
	private int textAreaHeight; // 文本框高度，用于计算对齐
	
	private String horizontalAlign; // 水平对齐，left:左对齐 right:右对齐 center: 居中
	
	private String verticalAlign; // 垂直对齐, top:上对齐 bottom:下对齐 center: 居中
	
	private String text; // 文字内容
	
	private Float gradientPaintX1; // 渐变点1X坐标，如果不需要渐变则为null
	
	private Float gradientPaintY1; // 渐变点1Y坐标，如果不需要渐变则为null

	private String gradientPaintColor1; //渐变点1颜色，如果不需要渐变则为null
	
	private Float gradientPaintX2; // 渐变点2X坐标，如果不需要渐变则为null
	
	private Float gradientPaintY2; // 渐变点2Y坐标，如果不需要渐变则为null
	
	private String gradientPaintColor2; //渐变点2颜色，如果不需要渐变则为null
}
