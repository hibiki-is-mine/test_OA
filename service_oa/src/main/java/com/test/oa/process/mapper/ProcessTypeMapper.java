package com.test.oa.process.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.test.model.process.ProcessType;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProcessTypeMapper extends BaseMapper<ProcessType> {
    List<ProcessType> findProcessType();
}
