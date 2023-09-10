package com.test.oa;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.test.model.process.Process;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;


public class GetPath {
    @Test
    public void getPath() throws FileNotFoundException {
        String path = new File(ResourceUtils.getURL("classpath:").getPath()).getAbsolutePath();
        System.out.println(path+"/processes");
    }
    @Test
    public void pageTest(){
        long page = 1L;
        long limit= 5L;
        Page<Process> pageParam= new Page<>(page,limit);
        long current = pageParam.getCurrent();
        long size = pageParam.getSize();
        System.out.println("if pageParam.getCurrent() == page :"+(current==page));
        System.out.println("if pageParam.getSize == limit :"+(size==limit));
    }
}
