package com.test.oa.process.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.test.model.process.ProcessTemplate;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProcessTemplateMapper extends BaseMapper<ProcessTemplate> {
    IPage<ProcessTemplate> selectPage(Page<ProcessTemplate> pageParam);
}
