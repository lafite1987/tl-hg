package com.hg.tl_hg.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.hg.tl_hg.entity.AdvertisementEntity;
import com.hg.tl_hg.mapper.AdvertisementMapper;
import com.hg.tl_hg.service.AdvertisementService;

@Service
public class AdvertisementServiceImpl extends ServiceImpl<AdvertisementMapper,AdvertisementEntity> implements AdvertisementService {

}
