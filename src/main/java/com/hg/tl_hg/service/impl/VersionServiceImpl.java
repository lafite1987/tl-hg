package com.hg.tl_hg.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.hg.tl_hg.entity.VersionEntity;
import com.hg.tl_hg.mapper.VersionMapper;
import com.hg.tl_hg.service.VersionService;

@Service
public class VersionServiceImpl extends ServiceImpl<VersionMapper,VersionEntity> implements VersionService {

}
