package com.bilibiliCover.generator.command.receiver;

import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.bilibiliCover.generator.command.BaseReceiver;
import com.bilibiliCover.generator.entity.GeneralPathBean;
import com.bilibiliCover.generator.service.impl.CoverServiceImpl;

/**
 * 特许形状遮罩执行者
 * @author kayden
 * @version 0.0.1
 * @createTime 2019年2月5日
 * @package com.bilibiliCover.generator.command.receiver
 */
public class GeneralPathReceiver implements BaseReceiver {
	
	private List<GeneralPathBean> list;
	
	private Graphics2D g;
	
	public GeneralPathReceiver(List<GeneralPathBean> list, Graphics2D g) {
		this.list = list;
		this.g = g;
	}

	@Override
	public void doDraw() {
		GeneralPath pathpp = new GeneralPath();
		for(GeneralPathBean bean : list) {
			if(StringUtils.isBlank(bean.getType()))
				throw new RuntimeException("对于特殊形状遮罩请输入正确的执行类型");
			switch (bean.getType()) {
			case "moveTo":
				pathpp.moveTo(bean.getPointX(), bean.getPointY());
				break;
			case "lineTo":
				pathpp.lineTo(bean.getPointX(), bean.getPointY());
				break;
			case "curveTo":
				pathpp.curveTo(bean.getBezierX1(), bean.getBezierY1(),
						bean.getBezierX2(), bean.getBezierY2(),
						bean.getPointX(), bean.getPointY());
				break;
			case "quadTo":
				pathpp.quadTo(bean.getBezierX1(), bean.getBezierY1(),
						bean.getPointX(), bean.getPointY());
				break;
			case "closePath":
				pathpp.closePath();
				break;
			case "reset":
				g.setClip(0, 0, CoverServiceImpl.WIDTH, CoverServiceImpl.HEIGHT);
				return;
			default:
				break;
			}
		}
		g.setClip(pathpp);
	}

}
