package com.bilibiliCover.generator.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bilibiliCover.generator.entity.CoverTemplateBean;
import com.bilibiliCover.generator.entity.CovertemplateparamsBean;
import com.bilibiliCover.generator.service.CoverService;
import com.bilibiliCover.generator.utils.R;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 封面控制层（服务端）
 * @author kayden
 * @version 0.0.1
 * @createTime 2019年1月27日
 * @package com.bilibiliCover.generator.controller
 */
@Api("封面管理控制层（服务端）")
@RestController
@RequestMapping("/cover")
public class CoverController {
	
	@Autowired
	private CoverService coverService;
	
	/**
	 * 获取所有在用模板
	 * @author kayden
	 * @version 0.0.1
	 * @createTime 2019年1月30日
	 * @return R
	 * @return
	 */
	@ApiOperation(value="获取所有在用模板")
	@GetMapping
	public R selectAll(@RequestParam("templateName")String templateName,
			@RequestParam("templateType")String templateType) {
		List<CoverTemplateBean> result = coverService.selectAll(templateName, templateType);
		return R.success().result(result);
	}
	
	/**
	 * 后台模板管理，提交模板
	 * @author kayden
	 * @version 0.0.1
	 * @createTime 2019年1月30日
	 * @return R
	 * @param data 模板的json
	 * @return
	 */
	@ApiOperation(value="后台模板管理，提交模板")
	@PostMapping("")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "coverTemplate", value = "模板", required = true, dataType = "String"),
        @ApiImplicitParam(name = "coverTemplateParams", value = "模板参数", required = true, dataType = "String"),
	})
	public R saveTemplate(@RequestBody String data) {
		JSONObject jo = JSON.parseObject(data);
		CoverTemplateBean coverTemplate = jo.getObject("coverTemplate", CoverTemplateBean.class);
		List<CovertemplateparamsBean> coverTemplateParams = JSON.parseArray(jo.getString("coverTemplateParams"), CovertemplateparamsBean.class);
		
		coverService.add(coverTemplate, coverTemplateParams);
		return R.success("增加成功");
	}
	
	/**
	 * 后台模板管理，批量删除
	 * @author kayden
	 * @version 0.0.1
	 * @createTime 2019年2月8日
	 * @return R
	 * @param ids
	 * @return
	 */
	@ApiOperation(value="后台模板管理，批量删除")
	@DeleteMapping
	@ApiImplicitParams({
        @ApiImplicitParam(name = "ids", value = "模板ids", required = true, dataType = "String"),
	})
	public R delTemplate(@RequestParam("ids")String ids) {
		String[] id = ids.split(",");
		
		coverService.delete(id);
		
		return R.success("删除成功");
	}
	
	/**
	 * 后台模板管理，根据kid获取模板，用于修改
	 * @author kayden
	 * @version 0.0.1
	 * @createTime 2019年1月30日
	 * @return R
	 * @param kid 唯一kid
	 * @return
	 */
	@ApiOperation(value="后台模板管理，根据kid获取模板，用于修改")
	@GetMapping("/{kid}")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "kid", value = "模板kid", required = true, dataType = "String"),
	})
	public R getTemplate(@PathVariable("kid")String kid) {
		String updateData = coverService.getUpdateData(kid);
		return R.success().result(updateData);
	}
	
	/**
	 * 后台模板管理，修改
	 * @author kayden
	 * @version 0.0.1
	 * @createTime 2019年2月8日
	 * @return R
	 * @param data
	 * @return
	 */
	@ApiOperation(value="后台模板管理，修改")
	@PutMapping()
	@ApiImplicitParams({
        @ApiImplicitParam(name = "coverTemplate", value = "模板", required = true, dataType = "String"),
        @ApiImplicitParam(name = "coverTemplateParams", value = "模板参数", required = true, dataType = "String"),
	})
	public R updateTemplate(@RequestBody String data) {
		JSONObject jo = JSON.parseObject(data);
		CoverTemplateBean coverTemplate = jo.getObject("coverTemplate", CoverTemplateBean.class);
		List<CovertemplateparamsBean> coverTemplateParams = JSON.parseArray(jo.getString("coverTemplateParams"), CovertemplateparamsBean.class);
		
		coverService.update(coverTemplate, coverTemplateParams);
		return R.success("修改成功");
	}
	
	/**
	 * 预览封面,用于后台模板设计
	 * @author kayden
	 * @version 0.0.1
	 * @createTime 2019年2月9日
	 * @return R
	 * @param data
	 * @return
	 */
	@ApiOperation(value="预览封面,用于后台模板设计")
	@PostMapping("/preview")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "templateCode", value = "模板内容", required = true, dataType = "String"),
	})
	public R previewCover(@RequestBody String data) {
		JSONObject jo = JSON.parseObject(data);
		String templateCode = jo.getString("templateCode");
		// 替换掉所有动态参数
		templateCode = templateCode.replaceAll("\\$\\{.*?\\}", " ");
		String path = coverService.previewCover(templateCode);
		return R.success().result(path);
	}
	
	/**
	 * 设置预览封面,用于后台模板设计
	 * @author kayden
	 * @version 0.0.1
	 * @createTime 2019年2月20日
	 * @return R
	 * @param data
	 * @return
	 */
	@ApiOperation(value="设置预览封面,用于后台模板设计")
	@PutMapping("/previewImage")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "previewImage", value = "需要登记为预览图的文件路径", required = true, dataType = "String"),
	})
	public R setpreviewCover(@RequestBody String data) {
		JSONObject jo = JSON.parseObject(data);
		String previewImage = jo.getString("previewImage");
		String previewImageKid = coverService.setpreviewCover(previewImage);
		return R.success("预览图设置成功").result(previewImageKid);
	}
}
