package com.hg.tl_hg.ctrl.rest;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hg.tl_hg.configure.FileServerConfig;
import com.hg.tl_hg.entity.AdvertisementEntity;
import com.hg.tl_hg.service.AdvertisementService;

import cn.lfy.common.Result;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/advertisement")
public class AdvertisementRest {

	@Autowired
	private AdvertisementService AdvertisementService;
	
	@Autowired
	private FileServerConfig fileServerConfig;
	
	/**
	 * 随机推荐广告
	 * @param type：广告类型：1-开机广告；2-首页广告；3-详情页普通广告；4-详情页大广告
	 * @param count：数量
	 * @return
	 */
	@RequestMapping(value = "/recommend")
	@ApiOperation(value = "随机推荐广告", httpMethod = "GET", notes = "推荐广告接口")
	public @ResponseBody Result<List<AdvertisementEntity>> recommend(
			@RequestParam(name = "type", defaultValue = "1", required = false)int type, 
			@RequestParam(name = "size", defaultValue = "1", required = false)int size) {
		Result<List<AdvertisementEntity>> result = Result.success();
		AdvertisementEntity advertisementEntity = new AdvertisementEntity();
		advertisementEntity.setType(type);
		advertisementEntity.setAuditStatus(3);
		advertisementEntity.setState(2);
		EntityWrapper<AdvertisementEntity> wrapper = new EntityWrapper<AdvertisementEntity>(advertisementEntity);
		wrapper.orderBy("rand()");
		wrapper.last(" limit " + size);
		List<AdvertisementEntity> list = AdvertisementService.selectList(wrapper);
		for(AdvertisementEntity entity : list) {
			if(StringUtils.isNotBlank(entity.getImagePath()) && !entity.getImagePath().startsWith("http://")) {
				if(entity.getImagePath().startsWith("http://")) {
					entity.setImageUrl(entity.getImagePath());
				} else {
					entity.setImageUrl(fileServerConfig.getDomain() + entity.getImagePath());
				}
			}
		}
		result.setData(list);
		return result;
	}
}
