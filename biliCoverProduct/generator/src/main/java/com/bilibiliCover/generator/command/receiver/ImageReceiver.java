package com.bilibiliCover.generator.command.receiver;

import java.awt.Graphics2D;

import com.bilibiliCover.generator.command.BaseReceiver;
import com.bilibiliCover.generator.entity.ImageBean;
import com.bilibiliCover.generator.utils.ImageUtils;

/**
 * 图片执行者
 * @author kayden
 * @version 0.0.1
 * @createTime 2019年1月29日
 * @package com.bilibiliCover.generator.command.receiver
 */
public class ImageReceiver implements BaseReceiver {
	
	private ImageBean bean;
	
	private Graphics2D g;
	
	public ImageReceiver(ImageBean bean, Graphics2D g) {
		this.bean = bean;
		this.g = g;
	}

	@Override
	public void doDraw() {
		ImageUtils.printPicture(g, bean.getImagePath(),
				bean.getX(), bean.getY(), bean.getWidth(),
				bean.getHeight());
	}

}
