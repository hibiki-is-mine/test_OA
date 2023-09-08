package com.test.oa.process.service.Impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.test.model.process.Process;
import com.test.oa.process.mapper.ProcessMapper;
import com.test.oa.process.service.ProcessService;
import com.test.vo.process.ProcessQueryVo;
import com.test.vo.process.ProcessVo;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.zip.ZipInputStream;

/**
 * @return
 */
@Service
public class ProcessServiceImpl extends ServiceImpl<ProcessMapper, Process> implements ProcessService {

    @Autowired
    private RepositoryService repositoryService;
    @Override
    public IPage<ProcessVo> selectPage(Page<ProcessVo> pageParam, ProcessQueryVo processQueryVo) {
        return baseMapper.selectPage(pageParam,processQueryVo);
    }

    @Override
    public void deployByZip(String deployPath) {
        //通过InputStream获取zip压缩文件
        InputStream inputStream = this.
                getClass().
                getClassLoader().
                getResourceAsStream(deployPath);
        if (inputStream != null) {
            ZipInputStream zipInputStream = new ZipInputStream(inputStream);
            Deployment deployment = repositoryService.
                    createDeployment().
                    addZipInputStream(zipInputStream).
                    deploy();
        }

    }

}
