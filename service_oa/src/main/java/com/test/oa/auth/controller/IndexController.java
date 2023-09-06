package com.test.oa.auth.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.test.common.jwt.JwtHelper;
import com.test.common.result.Result;
import com.test.common.utils.MD5Util;
import com.test.model.system.SysUser;
import com.test.oa.auth.service.SysMenuService;
import com.test.oa.auth.service.SysUserService;
import com.test.vo.system.LoginVo;
import com.test.vo.system.RouterVo;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @return
 */
@Api("用户登录")
@RestController
@RequestMapping("/admin/system/index")
public class IndexController {

    @Autowired
    SysUserService sysUserService;
    @Autowired
    SysMenuService sysMenuService;


    /**
     * 登录
     *
     * @param loginVo 登录签证官
     * @return {@link Result}
     */
    @PostMapping("login")
    public Result login(@RequestBody LoginVo loginVo) {
        /*Map<String, Object> map = new HashMap<>();
        map.put("token","admin");
        return Result.success(map);*/
        //根据用户名查询数据库
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUsername,loginVo.getUsername());
        SysUser user = sysUserService.getOne(wrapper);
        if (user==null){
            return Result.fail().message("用户不存在");
        }
        //获取数据库中的用户密码，将输入密码加密后，比较字符串是否相等
        else if(!user.getPassword().equals(MD5Util.getMD5(loginVo.getPassword()))){
            return Result.fail().message("密码错误");
        }
        //判断用户是否被禁用,1表示可用,0表示禁用
        else if(user.getStatus() ==0){
            return Result.fail().message("用户已被禁用");
        }else {
            //使用jwt生成token
            String token = JwtHelper.createToken(user.getId(), user.getUsername());
            Map<String ,Object> map = new HashMap<>();
            map.put("token",token);
            return Result.success(map);
        }




    }
    /**
     * 获取用户信息
     * @return
     */
    @GetMapping("info")
    public Result info(@RequestHeader("token") String token) {//从请求头中获取用户信息
/*      map.put("roles","[admin]");
        map.put("name","admin");
        map.put("avatar","https://oss.aliyuncs.com/aliyun_id_photo_bucket/default_handsome.jpg");
        return Result.success(map);*/
        Map<String,Object> map = new HashMap<>();

        //从token中获取用户id
        Long userId = JwtHelper.getUserId(token);
        String username = JwtHelper.getUsername(token);

        //根据用户id查询数据库，获取用户信息
        SysUser user = sysUserService.getById(userId);

        //根据用户id获取用户可以操作的菜单列表，动态创建路由结构进行显示
        List<RouterVo> routerList= sysMenuService.findUserMenuListByUserId(userId);
        //根据用户id获取用户可以操作的按键列表
        List<String> permsList = sysMenuService.findUserPermsByUserId(userId);


        map.put("name",username);
        //返回用户可以操作的菜单
        map.put("routers",routerList);
        //返回用户可以操作的按钮
        map.put("buttons",permsList);
        return Result.success(map);
    }
    /**
     * 退出
     * @return
     */
    @PostMapping("logout")
    public Result logout(){
        return Result.success();
    }

}
