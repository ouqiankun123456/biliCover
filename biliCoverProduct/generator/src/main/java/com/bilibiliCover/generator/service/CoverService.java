package com.bilibiliCover.generator.service;

import java.util.HashMap;
import java.util.List;

import com.bilibiliCover.generator.entity.CoverTemplateBean;
import com.bilibiliCover.generator.entity.CovertemplateparamsBean;

/**
 * 封面服务层接口
 * @author kayden
 * @version 0.0.1
 * @createTime 2019年2月1日
 * @package com.bilibiliCover.generator.service
 */
public interface CoverService {
	
	/**
	 * 根据配置json和dataMap生成封面，返回url
	 * @author kayden
	 * @version 0.0.1
	 * @createTime 2019年2月1日
	 * @return String
	 * @param templateKid 封面的kid
	 * @param dataMap 用户输入数据map
	 * @param printSize 输出的尺寸类型
	 * @return
	 */
	String generateCover(String templateKid, HashMap<String, Object> dataMap, String printSize);

	/**
	 * 查询所有模板 / 搜索指定模板
	 * @author kayden
	 * @version 0.0.1
	 * @param templateType 
	 * @param templateName 
	 * @createTime 2019年2月7日
	 * @return List<CoverTemplateBean>
	 * @return
	 */
	List<CoverTemplateBean> selectAll(String templateName, String templateType);

	/**
	 * 增加模板，分别增加coverTemplate表和coverTemplateParams记录
	 * @author kayden
	 * @version 0.0.1
	 * @createTime 2019年2月7日
	 * @return void
	 * @param coverTemplate 此为一个string直接保存表中
	 * @param coverTemplateParams 此为一个list 里面有此模板的参数名和参数输入类型
	 */
	void add(CoverTemplateBean coverTemplate, List<CovertemplateparamsBean> coverTemplateParams);

	/**
	 * 删除模板，删除模板和模板对应的所有参数
	 * @author kayden
	 * @version 0.0.1
	 * @createTime 2019年2月8日
	 * @return void
	 * @param id
	 */
	void delete(String... id);

	/**
	 * 获取修改的模板信息，包括模板和模板参数
	 * @author kayden
	 * @version 0.0.1
	 * @createTime 2019年2月8日
	 * @return String
	 * @param kid
	 * @return
	 */
	String getUpdateData(String kid);

	/**
	 * 修改模板，将旧的模板删除，再重新加入
	 * @author kayden
	 * @version 0.0.1
	 * @createTime 2019年2月8日
	 * @return void
	 * @param coverTemplate
	 * @param coverTemplateParams
	 */
	void update(CoverTemplateBean coverTemplate, List<CovertemplateparamsBean> coverTemplateParams);

	/**
	 * 预览模板设计图
	 * @author kayden
	 * @version 0.0.1
	 * @createTime 2019年2月9日
	 * @return String
	 * @param templateCode
	 * @return
	 */
	String previewCover(String templateCode);

	/**
	 * 将临时图路径传入固定路径并记录到uploadfile表中，返回uploadFile表的kid
	 * @author kayden
	 * @version 0.0.1
	 * @createTime 2019年2月20日
	 * @return String
	 * @param previewImage
	 * @return
	 */
	String setpreviewCover(String previewImage);

	/**
	 * 获取用户需要填写的表单信息
	 * @author kayden
	 * @version 0.0.1
	 * @createTime 2019年2月23日
	 * @return List<CovertemplateparamsBean>
	 * @param kid
	 * @return
	 */
	List<CovertemplateparamsBean> getGenerateForm(String kid);
}
