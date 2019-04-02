package com.bilibiliCover.generator.command.receiver;

import java.awt.Font;
import java.awt.Graphics2D;

import com.bilibiliCover.generator.command.BaseReceiver;
import com.bilibiliCover.generator.entity.TextBean;
import com.bilibiliCover.generator.utils.ImageUtils;

/**
 * 文字执行者
 * @author kayden
 * @version 0.0.1
 * @createTime 2019年1月29日
 * @package com.bilibiliCover.generator.command.receiver
 */
public class TextReceiver implements BaseReceiver {
	
	private TextBean bean;
	private Graphics2D g;
	
	public TextReceiver(TextBean bean, Graphics2D g) {
		this.bean = bean;
		this.g = g;
	}

	@Override
	public void doDraw() {
		ImageUtils.printText(g, bean);
	}

}
