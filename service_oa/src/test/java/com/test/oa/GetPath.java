package com.test.oa;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;

@SpringBootTest
public class GetPath {
    @Test
    public void getPath() throws FileNotFoundException {
        String path = new File(ResourceUtils.getURL("classpath:").getPath()).getAbsolutePath();
        System.out.println(path+"/processes");
    }
}
