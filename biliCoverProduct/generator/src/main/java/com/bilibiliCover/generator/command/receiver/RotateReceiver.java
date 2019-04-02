package com.bilibiliCover.generator.command.receiver;

import java.awt.Graphics2D;

import com.bilibiliCover.generator.command.BaseReceiver;
import com.bilibiliCover.generator.entity.RotateBean;
import com.bilibiliCover.generator.utils.ImageUtils;

/**
 * 旋转执行者
 * @author kayden
 * @version 0.0.1
 * @createTime 2019年1月30日
 * @package com.bilibiliCover.generator.command.receiver
 */
public class RotateReceiver implements BaseReceiver {

	private RotateBean bean;
	
	private Graphics2D g;
	
	public RotateReceiver(RotateBean bean, Graphics2D g) {
		this.bean = bean;
		this.g = g;
	}
	
	@Override
	public void doDraw() {
		ImageUtils.rotate(g, bean.getTheta(), bean.getX(), bean.getY());
	}

}
