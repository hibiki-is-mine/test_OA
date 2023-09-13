package com.test.oa.wechat.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.test.common.jwt.JwtHelper;
import com.test.common.result.Result;
import com.test.model.system.SysUser;
import com.test.oa.auth.service.SysUserService;
import com.test.vo.wechat.BindPhoneVo;
import io.swagger.annotations.ApiOperation;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import me.chanjar.weixin.common.bean.oauth2.WxOAuth2AccessToken;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

@Controller
@RequestMapping("/admin/wechat")
@CrossOrigin
public class WechatController {
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private WxMpService wxMpService;
    @Value("${wechat.userInfoUrl}")
    private String userInfoUrl;//设置授权回调接口的路径
    @GetMapping("/authorize")
    public String authorize(@RequestParam("returnUrl") String returnUrl,
                            HttpServletRequest request){
        /*
        buildAuthorizationUrl()方法具有三个参数：
            1.授权路径，表示在那个路径获取微信信息
            2.固定值，表示授权类型,WxConsts.OAuth2Scope.SNSAPI_USERINFO;
            3.表示完成授权后需要跳转的路径
        */
        /*String redirectUrl;
        try{
            redirectUrl = wxMpService.getOAuth2Service().buildAuthorizationUrl(
                    userInfoUrl,
                    WxConsts.OAuth2Scope.SNSAPI_USERINFO,
                    URLEncoder.encode(returnUrl.replace("testOa", "#")));
        }catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }*/

        String redirectUrl = wxMpService.getOAuth2Service().buildAuthorizationUrl(
                userInfoUrl,
                WxConsts.OAuth2Scope.SNSAPI_USERINFO,
                URLEncoder.encode(returnUrl.replace("testOa", "#")));
        return  "redirect:"+ redirectUrl;
    }
    @GetMapping("/userInfo")
    public String userInfo(@RequestParam("code") String code,
                           @RequestParam("state") String returnUrl) throws Exception {
        //获取accessToken
        WxOAuth2AccessToken accessToken = wxMpService.getOAuth2Service().getAccessToken(code);

        //使用accessToken获取openId
        String openId = accessToken.getOpenId();

        System.out.println("openId: "+openId);

        //获取微信用户信息
        WxOAuth2UserInfo wxMpUser = wxMpService.getOAuth2Service().getUserInfo(accessToken, null);
        System.out.println("微信用户信息: "+ JSON.toJSONString(wxMpUser));

        //根据openid查询用户表
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getOpenId,openId);
        SysUser sysUser = sysUserService.getOne(wrapper);
        String token = "";
        //判断openid是否存在
        if(sysUser != null) {
            token = JwtHelper.createToken(sysUser.getId(),sysUser.getUsername());
        }
        //if(returnUrl.indexOf("?") == -1) {
            return "redirect:" + returnUrl + "?token=" + token + "&openId=" + openId;
        //} else {
            //return "redirect:" + returnUrl + "&token=" + token + "&openId=" + openId;
        //}
    }

    @ApiOperation(value = "微信账号绑定手机")
    @PostMapping("bindPhone")
    @ResponseBody
    public Result bindPhone(@RequestBody BindPhoneVo bindPhoneVo) {
        SysUser sysUser = sysUserService.getOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getPhone, bindPhoneVo.getPhone()));
        if(null != sysUser) {
            sysUser.setOpenId(bindPhoneVo.getOpenId());
            sysUserService.updateById(sysUser);

            String token = JwtHelper.createToken(sysUser.getId(), sysUser.getUsername());
            return Result.success(token);
        } else {
            return Result.fail("手机号码不存在，绑定失败");
        }
    }



}
