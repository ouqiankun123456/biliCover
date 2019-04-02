package com.bilibiliCover.generator.utils;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.lang.StringUtils;

import com.bilibiliCover.generator.entity.TextBean;


/**
 * 图片工具类
 * @author kayden
 * @version 0.0.1
 * @createTime 2019年1月27日
 * @package com.bilibiliCover.generator.utils
 */
public class ImageUtils {
	
	/**
	*
	* 1、 输出图片到本地目录
	* @param buffImg 图片
	* @param savePath 本地目录的路径
	*/
   public static void  saveImageToLocalDir(BufferedImage buffImg, String savePath) {
       try {
    	   File file = new File(savePath); 
    	   if(!file.exists()){
    		   file.mkdirs();
           }
           ImageIO.write(buffImg, "jpg", file);
       } catch (IOException e1) {
           e1.printStackTrace();
       }
   }
   
   /**
    * 竖排文字
    * @author kayden
    * @version 0.0.1
    * @createTime 2019年1月28日
    * @return void
    * @param x X坐标
    * @param y y坐标
    * @param str 文字
    * @param g 实例
    * @param font 字体
    * @param textColor 文字颜色
    * @param borderColor 描边颜色
    */
   public static void addVerticalText(double x,double y,String str,Graphics2D g, Font font,Color textColor, Color borderColor){
	   int strlength = str.length();
	   //获取字体宽度
//	   int t = g.getFontMetrics().stringWidth("测试");
	   int t = font.getSize();
	   FontRenderContext frc = g.getFontRenderContext();

	   //竖排文字
	   for(int i=0;i<strlength;i++){
		   TextLayout tl = new TextLayout(String.valueOf(str.charAt(i)), font, frc); 
		   Shape shape = tl.getOutline(AffineTransform.getTranslateInstance(x, y));
		   g.setColor(borderColor);
		   g.draw(shape);
		   g.setColor(textColor);
		   g.fill(shape);
		   y +=t;
	   }
   }
   
   /**
    * 抗锯齿设置
    * @author kayden
    * @version 0.0.1
    * @createTime 2019年1月28日
    */
   public static void antialasing(Graphics2D g) {
	   // 抗锯齿
       g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);

       g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

       g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
   }
   
   /**
    * 绘制图片
    * @author kayden
    * @version 0.0.1
    * @createTime 2019年1月28日
    * @return void
    * @param g Graphics2D实例
    * @param imagePath 需要加入的图片路径
    * @param x X轴坐标
    * @param y y轴坐标
    * @param width 图片宽度
    * @param height 图片高度
    */
   public static void printPicture(Graphics2D g, String imagePath, int x, int y, int width, int height) {
	   try {
		   if(StringUtils.isBlank(imagePath)) {
			   g.setBackground(Color.WHITE);
			   g.clearRect(0, 0, width, height);
			   return;
		   }
			BufferedImage i = ImageIO.read(new File(imagePath));		
			// 开始绘制图片
		    g.drawImage(i, x, y, width, height, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
   }
   
   @SuppressWarnings("unused")
   private static boolean ObjectIsAllNull(Object... o) {
	   boolean flag = true;
	   for(Object a : o) {
		   if(a != null)
			   flag = false;
	   }
	   return flag;
   }
   
   private static boolean ObjectIsAnyNull(Object... o) {
	   boolean flag = false;
	   for(Object a : o) {
		   if(a == null)
			   flag = true;
	   }
	   return flag;
   }
   
   /**
    * 根据格式rgba(19, 206, 102, 0.8)转换成Color对象
    * @author kayden
    * @version 0.0.1
    * @createTime 2019年1月29日
    * @return Color
    * @param textColor
    * @return
    */
   public static Color transColor(String textColor) {
	   if(StringUtils.isBlank(textColor))
		   return null;
	   textColor = StringUtils.substringAfter(textColor, "(");
	   textColor = StringUtils.substringBeforeLast(textColor, ")");
	   String[] rgba = textColor.split(",");
	   return new Color(Integer.parseInt(rgba[0].trim()),
			   Integer.parseInt(rgba[1].trim()),
			   Integer.parseInt(rgba[2].trim()),
			   rgba.length == 3 ? 255 : (int)(Float.parseFloat(rgba[3].trim())*255));
   }
   
   /**
    * 文字输出
    * @author kayden
    * @version 0.0.1
    * @createTime 2019年1月28日
    * @return void
    * @param g Graphics2D实例
    * @param isVerticalText 是否垂直文本
    * @param fontName 字体，若为null则为宋体
    * @param fontStyle 样式
    * @param fontSize 字体大小
    * @param textColor 字体颜色
    * @param borderColor 描边颜色
    * @param borderWidth 描边宽度
    * @param positionX 文本框X坐标
    * @param positionY 文本框Y坐标
    * @param textAreaWidth 文本框宽度，用于计算对齐
    * @param textAreaHeight 文本框高度，用于计算对齐
    * @param text 文字
    * @param GradientPaintX1 渐变点1X坐标，如果不需要渐变则为null
    * @param GradientPaintY1 渐变点1Y坐标，如果不需要渐变则为null
    * @param GradientPaintColor1 渐变点1颜色，如果不需要渐变则为null
    * @param GradientPaintX2 渐变点2X坐标，如果不需要渐变则为null
    * @param GradientPaintY2 渐变点2Y坐标，如果不需要渐变则为null
    * @param GradientPaintColor2 渐变点2颜色，如果不需要渐变则为null
    * @param cyclic 暂不知道是什么 传true
    * 
    * 渐变原理：GradientPaint是Java2D中专门用来控制渐变的类，
    * 它提供了使用线性颜色渐变模式填充 Shape 的方法。
    * 其构造函数GradientPaint(float x1, float y1, Color color1, float x2, float y2, Color color2)，
    * 充分说明了它的作用，即从点(x1,y1)到点(x2,y2)进行渐变。
    * 如果在用户空间指定了 Point P1 的 Color 为 C1，
    * Point P2 的 Color 为 C2，
    * 则 P1、P2 连接线上的 Color 是逐渐地从 C1 变化到 C2 的。
    * 任何不在 P1、P2 延长线上的点 P 都具有点 P' 的颜色，
    * P' 是 P1、P2 延长线上点 P 的垂直投影。P1、P2 段以外的延长线上的点可以按以下两种方式之一进行着色。 
    */
   public static void printText(Graphics2D g, TextBean bean) {
	   
	   Rectangle rect = new Rectangle((int)bean.getPositionX(), (int)bean.getPositionY(), bean.getTextAreaWidth(), bean.getTextAreaHeight());	   
	   // 定义字体样式
	   Font textFont = new Font(StringUtils.isBlank(bean.getFontName()) ? "宋体" : bean.getFontName(), bean.getFontStyle(), bean.getFontSize());
	   
	   Integer positionX = textHorizontalAlign(g, bean.getText(), rect, textFont, bean.getHorizontalAlign());
	   Integer positionY = textVerticalAlign(g, bean.getText(), rect, textFont, bean.getHorizontalAlign());
	   // 定义描边
	   // 字描边宽度
       g.setStroke(new BasicStroke(bean.getBorderWidth()));
       if(!ObjectIsAnyNull(bean.getGradientPaintX1(), bean.getGradientPaintY1(), ImageUtils.transColor(bean.getGradientPaintColor1()),
    		   bean.getGradientPaintX2(), bean.getGradientPaintY2(),  ImageUtils.transColor(bean.getGradientPaintColor2()))) {
    	   // 渐变颜色
    	   // 创建循环渐变的GraphientPaint对象
    	   GradientPaint paint = new GradientPaint(bean.getGradientPaintX1(), bean.getGradientPaintY1(), ImageUtils.transColor(bean.getGradientPaintColor1()),
    			   bean.getGradientPaintX2(), bean.getGradientPaintY2(),  ImageUtils.transColor(bean.getGradientPaintColor2()), true);
    	   g.setPaint(paint);// 设置渐变    	   
       }
       // 垂直还是水平输出
       if(bean.isVerticalText()) {
    	   addVerticalText(positionX, positionY, bean.getText(), g, textFont,
    			   ImageUtils.transColor(bean.getTextColor()), ImageUtils.transColor(bean.getBorderColor()));  
       } else {
    	   FontRenderContext frc = g.getFontRenderContext();
    	   TextLayout tl = new TextLayout(bean.getText(), textFont, frc);     
           Shape shape = tl.getOutline(AffineTransform.getTranslateInstance(positionX, positionY));
           g.setColor(ImageUtils.transColor(bean.getBorderColor()));
           g.draw(shape);
           g.setColor(ImageUtils.transColor(bean.getTextColor()));
           g.fill(shape); 
       }
   }
   
   /**
    * 文字计算水平对齐X坐标
    * @author kayden
    * @version 0.0.1
    * @createTime 2019年2月3日
    * @return Integer
    * @param g
    * @param text
    * @param rect 传入的文本框
    * @param font 
    * @param tag left:左对齐 right:右对齐 center: 居中
    * @return
    */
   public static Integer textHorizontalAlign(Graphics g, String text, Rectangle rect, Font font, String tag) {
	   // Get the FontMetrics
       FontMetrics metrics = g.getFontMetrics(font);
       switch (StringUtils.lowerCase(tag)) {
		case "left":
			return rect.x;
		case "right":
			return rect.x + (rect.width - metrics.stringWidth(text));
		default:
			return rect.x + (rect.width - metrics.stringWidth(text)) / 2;
		}
   }
   
   /**
    * 文字计算水平对齐Y坐标
    * @author kayden
    * @version 0.0.1
    * @createTime 2019年2月3日
    * @return Integer
    * @param g
    * @param text
    * @param rect 传入的文本框
    * @param font 
    * @param tag top:上对齐 bottom:下对齐 center: 居中
    * @return
    */
   public static Integer textVerticalAlign(Graphics g, String text, Rectangle rect, Font font, String tag) {
	   // Get the FontMetrics
       FontMetrics metrics = g.getFontMetrics(font);
       switch (StringUtils.lowerCase(tag)) {
		case "top":
			return rect.y + metrics.getAscent();
		case "bottom":
			return rect.y + rect.height - metrics.getHeight() + metrics.getAscent();
		default:
			return rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
		}
   }

   
   /**
    * 旋转 最后两个参数表示的是围绕那个坐标
    * @author kayden
    * @version 0.0.1
    * @createTime 2019年1月28日
    * @return void
    * @param g
    * @param theta 角度,可负数
    * @param x
    * @param y
    */
   public static void rotate(Graphics2D g, double theta, double x, double y) {
	   g.rotate(Math.toRadians(theta), x, x);
   }
   
   /*** 
    * 功能 :调整图片大小 开发：
	* @author kayden
    * @param srcImgPath 原图片路径 
    * @param distImgPath  转换大小后图片路径 
    * @param width   转换后图片宽度 
    * @param height  转换后图片高度 
 * @throws IOException 
    */  
   public static void resizeImage(String srcImgPath, String distImgPath,  
           int width, int height) throws IOException {  
 
       File srcFile = new File(srcImgPath);  
       Image srcImg = ImageIO.read(srcFile);  
       BufferedImage buffImg = null;  
       buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);  
       buffImg.getGraphics().drawImage(  
               srcImg.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0,  
               0, null);  
 
       ImageIO.write(buffImg, "png", new File(distImgPath));  
 
   }  
   
//   public static void main(String[] args) {
//	   
//	   String path = "C:\\Users\\kayden\\Pictures\\00100.jpg";
//	   GeneralPath pathpp = new GeneralPath();
//	   pathpp.moveTo(0, 0);
//	   pathpp.curveTo(900, 250, 250, 900, 1000, 1000);
//	   pathpp.lineTo(0, 1000);
//	   pathpp.closePath();
//
//	   BufferedImage i;
//		try {
//			i = ImageIO.read(new File(path));
//		
//		
//		   // 3、将二维码放入缓冲流
//	       BufferedImage image = new BufferedImage(1000, 1000, BufferedImage.TYPE_INT_RGB);
//	       
//	       //在原来基础上，再添加一个图片
//	       Graphics2D g = image.createGraphics();
//	       // 抗锯齿
//	       g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
//
//	       g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//
//	       g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
//	       
//	       //设定在图片中间
//	       int x = (image.getWidth() - 100) / 2;
//	       int y = (image.getHeight() - 100) / 2;
//	       
//	       // 剪切图片
//	       g.setClip(pathpp);
//	       
//	       // 开始绘制图片
//	       g.drawImage(i, 0, 0, 1000, 1000, null);
//	       
//	       // 还原尺寸
//	       g.setClip(0,0,1000,1000);
//	
//	       //绘制圆角矩形
//	       g.drawRoundRect(x, y, 250, 250, 15, 15);
//	
//	       //边框宽度
//	       g.setStroke(new BasicStroke(2));
//	
//	       //边框颜色
//	       g.setColor(Color.WHITE);
//	       g.drawRect(x, y, 400, 400);
//	       
//	       // 添加字
//           g.setColor(Color.WHITE);
//           // 字描边宽度
//           g.setStroke(new BasicStroke(10));
//           
//           Font font = new Font("微软雅黑",Font.BOLD,100);
//
//           FontRenderContext frc = g.getFontRenderContext();
//
//           TextLayout tl = new TextLayout("测试", font, frc);
//           
//           Rectangle r2d = new Rectangle(0, 0, 1000, 1000);
//           
//           Integer nx = textHorizontalAlign(g, "测试", r2d, font, "center");
//           Integer ny = textVerticalAlign(g, "测试", r2d, font, "center");
//           
//           Shape shape = tl.getOutline(AffineTransform.getTranslateInstance(nx, ny));
//           
//           // 剪文字
//           pathpp.reset();
//           pathpp.moveTo(0, 500);
//           pathpp.lineTo(1000, 500);
//           pathpp.lineTo(1000, 1000);
//           pathpp.lineTo(0, 1000);
//           pathpp.closePath();
//           g.setClip(pathpp);
//           // 打印“测试”下半部分文字
//           g.draw(shape);
//           g.setColor(Color.BLACK);
//           g.fill(shape);
//           
//           tl = new TextLayout("英豪", font, frc);
//           shape = tl.getOutline(AffineTransform.getTranslateInstance(nx, ny - 20));
//           // 剪文字
//           pathpp.reset();
//           pathpp.moveTo(0, 495);
//           pathpp.lineTo(1000, 495);
//           pathpp.lineTo(1000, 0);
//           pathpp.lineTo(0, 0);
//           pathpp.closePath();
//           g.setClip(pathpp);
//           // 打印“英豪”上半部分文字
//           g.draw(shape);
//           g.setColor(Color.WHITE);
//           g.fill(shape);
//           // 还原
//           g.setClip(0, 0, 1000, 1000);
//           
////           g.drawString("测试", 100, 200);
//           
//           //字体样式
//           int FONT_STYLE = Font.CENTER_BASELINE;
//           //字体大小
//           int FONT_SIZE = 20;
//
//           //字体颜色
//           String FONT_NAME = "宋体";
//           
//           g.setFont(new Font(FONT_NAME, FONT_STYLE, FONT_SIZE));
//           g.setColor(Color.YELLOW);
//           
//           //设定旋转 ， 后面两个参数表示的是围绕那个坐标
//           g.rotate(Math.toRadians(-30), i.getWidth()/2, i.getHeight()/2);
//           
//           g.drawString("测试2", 200, 100);
//           
//           // addVerticalText
//           g.setStroke(new BasicStroke(10));
//           g.setColor(Color.BLUE);
//           g.setFont(new Font("Lucida Handwriting", Font.ITALIC, 50));
//           g.rotate(Math.toRadians(30), 0, 0);
////           addVerticalText(700, -10, "vertical text", g);
//           
//           // 渐变颜色
//           // 创建循环渐变的GraphientPaint对象
//           GradientPaint paint = new GradientPaint(50, 50, Color.DARK_GRAY, 100,100, Color.CYAN, true);
//           g.setPaint(paint);// 设置渐变
//           g.setFont(new Font("华文琥珀", Font.BOLD, 60));
//           g.drawString("渐变字", x, y); // 绘制文本  
//           
//	       g.dispose();
//	       i.flush();
//	       image.flush();
//	       
//	       // 输出水印图片
//	       String saveFilePath = "C:\\Users\\kayden\\Desktop\\new.png";
//	       saveImageToLocalDir(image, saveFilePath);
//	       System.out.println("生成成功");
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}

}
