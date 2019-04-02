package com.bilibiliCover.generator.service.impl;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bilibiliCover.generator.command.command.DrawCommand;
import com.bilibiliCover.generator.command.receiver.GeneralPathReceiver;
import com.bilibiliCover.generator.command.receiver.ImageReceiver;
import com.bilibiliCover.generator.command.receiver.RotateReceiver;
import com.bilibiliCover.generator.command.receiver.TextReceiver;
import com.bilibiliCover.generator.constant.PrintSizeEnum;
import com.bilibiliCover.generator.constant.PrintTypeEnum;
import com.bilibiliCover.generator.entity.CoverTemplateBean;
import com.bilibiliCover.generator.entity.CovertemplateparamsBean;
import com.bilibiliCover.generator.entity.GeneralPathBean;
import com.bilibiliCover.generator.entity.ImageBean;
import com.bilibiliCover.generator.entity.RotateBean;
import com.bilibiliCover.generator.entity.TextBean;
import com.bilibiliCover.generator.entity.UploadFileBean;
import com.bilibiliCover.generator.mapper.CoverTemplateMapper;
import com.bilibiliCover.generator.mapper.CoverTemplateParamsMapper;
import com.bilibiliCover.generator.mapper.UploadFileMapper;
import com.bilibiliCover.generator.service.CoverService;
import com.bilibiliCover.generator.utils.GuidUtils;
import com.bilibiliCover.generator.utils.ImageUtils;
import com.bilibiliCover.generator.utils.ParseParams;
import com.bilibiliCover.generator.utils.WebpUtils;

import lombok.extern.slf4j.Slf4j;
import tk.mybatis.mapper.entity.Example;

/**
 * 封面接口实现类
 * @author kayden
 * @version 0.0.1
 * @createTime 2019年2月1日
 * @package com.bilibiliCover.generator.service.impl
 */
@Service("coverService")
@Slf4j
public class CoverServiceImpl implements CoverService {
	
	private static final String TYPE = "type";
	
	private static final String DATA = "data";
	
	public static final Integer WIDTH = 1600;
	
	public static final Integer HEIGHT = 1000;
	
	@Value("${rootPath}")
	private String ROOTPATH; // 根目录
	
	@Value("${temporaryPath}")
	private String TEMPLATEPATH; // 临时存放路径
	
	@Value("${temporaryRelativePath}")
	private String TEMPORARY_RELATIVE_PATH; // 临时相对路径
	
	@Value("${previewCoverPath}")
	private String PREVIEW_COVER_PATH; // 预览图路径
	
	@Value("${previewRelativePath}")
	private String PREVIEW_RELATIVE_PATH; // 预览图相对路径
	
	@Autowired
	private CoverTemplateMapper coverTemplateMapper;
	
	@Autowired
	private CoverTemplateParamsMapper coverTemplateParamsMapper;
	
	@Autowired
	private UploadFileMapper uploadFileMapper;

	@Override
	public String generateCover(String templateKid, HashMap<String, Object> dataMap, String printSize) {
		
		// 根据templateKid查出模板
		Example example = new Example(CoverTemplateBean.class);
		example.createCriteria().andEqualTo("kid", templateKid).andEqualTo("isdel", false);
		CoverTemplateBean curTemplate = coverTemplateMapper.selectOneByExample(example);
		if(curTemplate == null)
			throw new RuntimeException("没有找到该模板或者模板已被删除");
		// 替换变量
		String templateCode = ParseParams.parseParams(curTemplate.getTemplateCode(), dataMap);
		List<String> json = JSONArray.parseArray(ParseParams.escapeExprSpecialWord(templateCode, "\\"), String.class);
		
		// 生成封面
		BufferedImage image = generateCoverCore(json);
       
        // 输出图片
		String saveFileName = GuidUtils.getGUID(36) + ".png";
        String saveFilePath = TEMPLATEPATH + File.separator;
        ImageUtils.saveImageToLocalDir(image, saveFilePath + saveFileName);
        // 按封面类型转换尺寸
        PrintSizeEnum pse = PrintSizeEnum.tran(printSize);
        String saveTransName = GuidUtils.getGUID(36) + ".png";
        try {
			ImageUtils.resizeImage(saveFilePath + saveFileName, saveFilePath + saveTransName, pse.getWidth(), pse.getHeight());
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("转换图片尺寸出错");
		}
        log.info("生成成功 地址:" + saveFilePath + saveTransName);
        return TEMPORARY_RELATIVE_PATH + File.separator + saveTransName;
	}
	
	/**
	 * 生成封面核心代码
	 * @author kayden
	 * @version 0.0.1
	 * @createTime 2019年2月9日
	 * @return BufferedImage
	 * @param json
	 * @return
	 */
	private BufferedImage generateCoverCore(List<String> json) {
		// 打开一个图片流
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
       
        // 创建Graphics2D对象
        Graphics2D g = image.createGraphics();
        
        json.stream().forEachOrdered(e -> {
        	// 提取其中的type属性，与printTypeEnum枚举类匹配操作类型
        	JSONObject jsonObject = null;
			jsonObject = JSONObject.parseObject(e);
        	String type = jsonObject.getString(TYPE);
        	DrawCommand dc = null;
        	switch (PrintTypeEnum.tran(type)) {
			case IMAGE:
				ImageBean imageBean = JSON.parseObject(ParseParams.escapeExprSpecialWord(jsonObject.getString(DATA), "\\"), ImageBean.class);
				// 通过uploadFile表查到对应的savePath
				Example example = new Example(UploadFileBean.class);
				if(imageBean.getImagePath().length() > 36) {
					// 大于36说明是临时文件，从临时文件夹拿
					File file = new File(ROOTPATH + imageBean.getImagePath());
					if(!file.exists())
						throw new RuntimeException("没有找到图片，请重新上传");
					else
						imageBean.setImagePath(file.getAbsolutePath());
				}else {
					example.createCriteria().andEqualTo("kid", imageBean.getImagePath());
					UploadFileBean b = uploadFileMapper.selectOneByExample(example);
					imageBean.setImagePath(Optional.ofNullable(b).orElse(new UploadFileBean()).getSavePath());					
				}
				dc = new DrawCommand(new ImageReceiver(imageBean, g));
				break;
			case TEXT:
				TextBean textBean = JSON.parseObject(jsonObject.getString(DATA), TextBean.class);
				dc = new DrawCommand(new TextReceiver(textBean, g));
				break;
			case ROTATE:
				RotateBean rotateBean = JSON.parseObject(jsonObject.getString(DATA), RotateBean.class);
				dc = new DrawCommand(new RotateReceiver(rotateBean, g));
				break;
			case GENERALPATH:
				List<GeneralPathBean> generalPathBeans = JSONArray.parseArray(jsonObject.getString(DATA), GeneralPathBean.class);
				dc = new DrawCommand(new GeneralPathReceiver(generalPathBeans, g));
				break;
			default:
				throw new RuntimeException("请输入正确的封面类型参数");
			}
        	// 运行
        	dc.Execute();
        });
        g.dispose();
        image.flush();
        return image;
	}

	@Override
	public List<CoverTemplateBean> selectAll(String templateName, String templateType) {
		Example example = new Example(CoverTemplateBean.class);
		if(StringUtils.isNotBlank(templateName))
			example.and().andLike("templateName", "%" + templateName + "%");
		if(StringUtils.isNotBlank(templateType))
			example.and().andEqualTo("templateType", templateType);
		example.and().andEqualTo("isdel", false);
		example.excludeProperties("templateCode");
		return coverTemplateMapper.selectByExample(example);
	}

	@Transactional
	@Override
	public void add(CoverTemplateBean coverTemplate, List<CovertemplateparamsBean> coverTemplateParams) {
		// 生成uuid
		String CoverTemplateKid = GuidUtils.getGUID(36);
		// 保存进模板表
		coverTemplate.setKid(CoverTemplateKid).setId(null);
		coverTemplateMapper.insertSelective(coverTemplate);
		// 保存进模板参数表	
		Optional.ofNullable(coverTemplateParams).orElse(new ArrayList<>()).forEach(e -> {
			e.setTemplateKid(CoverTemplateKid).setKid(GuidUtils.getGUID(36));
			coverTemplateParamsMapper.insertSelective(e);
		});
	}

	@Transactional
	@Override
	public void delete(String... id) {
		for(String i : id) {
			// 找到模板的kid
			CoverTemplateBean ctb = coverTemplateMapper.selectByPrimaryKey(i);
			if(ctb.getIsdel())
				throw new RuntimeException("该模板已被删除");
			ctb.setIsdel(true);
			ctb.setDelTime(Timestamp.valueOf(LocalDateTime.now()));
			// 删除模板
			coverTemplateMapper.updateByPrimaryKey(ctb);
			// 删除模板参数
			Example example = new Example(CovertemplateparamsBean.class);
			example.createCriteria().andEqualTo("templateKid", ctb.getKid());
			coverTemplateParamsMapper.deleteByExample(example);
		}
	}

	@Override
	public String getUpdateData(String kid) {
		CoverTemplateBean bean = coverTemplateMapper.selectByPrimaryKey(kid);
		// 根据templateKid查询所有参数
		Example example = new Example(CovertemplateparamsBean.class);
		example.createCriteria().andEqualTo("templateKid", kid);
		List<CovertemplateparamsBean> list = coverTemplateParamsMapper.selectByExample(example);
		Map<String, String> result = new HashMap<>();
		String coverTemplate = JSON.toJSONString(bean);
		String coverTemplateParams = JSONArray.toJSONString(list);
		result.put("coverTemplate", coverTemplate);
		result.put("coverTemplateParams", coverTemplateParams);
		return JSON.toJSONString(result);
	}

	@Transactional
	@Override
	public void update(CoverTemplateBean coverTemplate, List<CovertemplateparamsBean> coverTemplateParams) {
		// 删除旧的模板参数
		Example example = new Example(CovertemplateparamsBean.class);
		example.createCriteria().andEqualTo("templateKid", coverTemplate.getKid());
		coverTemplateParamsMapper.deleteByExample(example);
		// 重新加入新的模板 覆盖原记录
		// 重新启用模板
		coverTemplate.setIsdel(false).setCreateTime(Timestamp.valueOf(LocalDateTime.now()));
		Example ctbExample = new Example(CoverTemplateBean.class);
		ctbExample.createCriteria().andEqualTo("kid", coverTemplate.getKid());
		int i = coverTemplateMapper.updateByExampleSelective(coverTemplate, ctbExample);
		if(i != 1)
			throw new RuntimeException("修改失败，没有修改对应的模板或者没有匹配到对应模板");
		// 保存进模板参数表	
		Optional.ofNullable(coverTemplateParams).orElse(new ArrayList<>()).forEach(e -> {
			e.setTemplateKid(coverTemplate.getKid()).setKid(GuidUtils.getGUID(36));
			coverTemplateParamsMapper.insertSelective(e);
		});
	}

	@Override
	public String previewCover(String templateCode) {
		List<String> json = JSONArray.parseArray(templateCode, String.class);
		
		// 生成封面
		BufferedImage image = generateCoverCore(json);
       
        // 输出图片
        String saveFilePath = TEMPLATEPATH + File.separator +  "预览.png";
        ImageUtils.saveImageToLocalDir(image, saveFilePath);
        System.out.println("生成成功 地址:" + saveFilePath);
        return saveFilePath;
	}

	@Transactional
	@Override
	public String setpreviewCover(String previewImage) {
		// 新建一个kid
		String kid = GuidUtils.getGUID(36);
		try {
			String newPath = PREVIEW_COVER_PATH + File.separator + kid + ".webp";
			// 按封面类型转换尺寸
	        String saveTransName = GuidUtils.getGUID(36) + ".png";
	        try {
				ImageUtils.resizeImage(previewImage, TEMPLATEPATH + File.separator + saveTransName,
						PrintSizeEnum.PREVIEW_IMAGE.getWidth(), PrintSizeEnum.PREVIEW_IMAGE.getHeight());
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException("转换图片尺寸出错");
			}
			// 移动到长期保存文件夹
			WebpUtils.png2webp(TEMPLATEPATH + File.separator + saveTransName, newPath);
			// 加入到uploadFile表中
//			uploadFileMapper.insertSelective(new UploadFileBean()
//					.setExt(".webp").setKid(kid).setIsdel(false).setSavePath(newPath));
			return PREVIEW_RELATIVE_PATH + File.separator + kid + ".webp";
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("设置预览图失败鸟,可能预览图已不存在，请重新生成");
		}
	}

	@Override
	public List<CovertemplateparamsBean> getGenerateForm(String kid) {
		return coverTemplateParamsMapper.select(new CovertemplateparamsBean().setTemplateKid(kid));
	}
	
	
//	public static void main(String[] args) {
//		CoverServiceImpl ci = new CoverServiceImpl();
//		HashMap<String, Object> dataMap = new HashMap<>();
//		List<String> json = new ArrayList<>();
//		json.add("{\"type\":\"image\",\"data\": {\"imagePath\":\"C:\\\\Users\\\\kayden\\\\Pictures\\\\00100.jpg\",\"x\":\"0\",\"y\":\"0\",\"width\":\"1600\",\"height\":\"1000\"}}");
//		json.add("{\"type\": \"generalPath\", \"data\": [" + 
//				"{\"type\": \"moveTo\", \"pointX\": \"0\",\"pointY\": \"0\"}," + 
//				"{\"type\": \"curveTo\", \"pointX\": \"1600\",\"pointY\": \"1000\", \"bezierX1\": \"800\", \"bezierY1\": \"200\", \"bezierX2\": \"200\", \"bezierY2\": \"800\"}," + 
//				"{\"type\": \"lineTo\", \"pointX\": \"0\",\"pointY\": \"1000\"}," + 
//				"{\"type\": \"closePath\"}" + 
//				"]}");
//		json.add("{\"type\":\"image\",\"data\": {\"imagePath\":\"C:\\\\Users\\\\kayden\\\\Pictures\\\\0002.jpg\",\"x\":\"0\",\"y\":\"0\",\"width\":\"1600\",\"height\":\"1000\"}}");
//		json.add("{\"type\": \"generalPath\", \"data\": [" + 
//				"{\"type\": \"reset\"}" + 
//				"]}");
//		json.add("{\"type\": \"text\", \"data\": {" + 
//				"\"isVerticalText\": true," + 
//				"\"fontName\": \"站酷庆科黄油体\"," + 
//				"\"fontStyle\": 1," + 
//				"\"fontSize\": 200," + 
//				"\"textColor\": \"rgba(255, 0, 0, 0.8)\"," + 
//				"\"borderColor\": \"rgba(0, 0, 0, 1)\"," + 
//				"\"borderWidth\": 10," + 
//				"\"positionX\": 0," + 
//				"\"positionY\": 500," + 
//				"\"textAreaWidth\": 1600," + 
//				"\"textAreaHeight\": 100," + 
//				"\"horizontalAlign\": \"center\"," + 
//				"\"verticalAlign\": \"center\"," + 
//				"\"text\": \"试试就逝世\"" + 
//				"}}");
//		String path = ci.GenerateCover(json, dataMap);
//		System.out.println(path + " 成功");
//	}

}
