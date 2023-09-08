package com.test.oa;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.test.model.process.ProcessTemplate;
import com.test.oa.process.service.ProcessTemplateService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @return
 */
@SpringBootTest
public class getPageTest {
    @Autowired
    ProcessTemplateService processTemplateService;
    @Test
    public void ProcessTemplateServiceGetPage(){
        Page<ProcessTemplate> pageParam= new Page<>(1,5);
        IPage<ProcessTemplate> processTemplateIPage = processTemplateService.selectPage(pageParam);
        IPage<ProcessTemplate> processTemplateIPage2 = processTemplateService.selectPage2(pageParam);

        System.out.println("if processTemplateIPage equals processTemplateIPage2 ： " + processTemplateIPage.equals(processTemplateIPage2));

    }
}
