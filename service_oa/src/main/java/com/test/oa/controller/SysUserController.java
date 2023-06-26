package com.test.oa.controller;

import com.test.oa.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @return
 */
@RestController
@RequestMapping("/admin/system/sysRole")
public class SysUserController {
    @Autowired
    SysUserService sysUserService;


}
