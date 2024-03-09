package com.arts.work.controller;

import com.arts.work.common.pojo.dto.LoginDTO;
import com.arts.work.common.utils.RestResponse;
import com.arts.work.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    /**
     * 注入redisTemplate用于缓存数据
     */
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 注入loginService用于调用其中自定义的邮件发送方法
     */
    @Autowired
    private UserService userService;

    /**
     * 发送邮件
     *
     * @param loginDTO 目标邮件地址，作为redis存储的key
     */
    @PostMapping("sendEmailCode")
    public RestResponse sendEmail(@RequestBody LoginDTO loginDTO) {
        return userService.sendAuthCodeEmail(loginDTO.getEmail());
    }

    /**
     * 登录验证
     *
     * @param loginDTO 接收前端传入的邮箱地址和code
     * @return 返回执行结果
     */
    @PostMapping("login")
    public RestResponse login(@RequestBody LoginDTO loginDTO) {
        return userService.login(loginDTO);
    }

    /**
     * 生成邀请码
     *
     * @param userId 接收前端的用户id
     * @return 返回执行结果
     */
    @PostMapping("generateInvitationCode")
    public RestResponse generateInvitationCode(@RequestParam String userId) {
        return userService.generateInvitationCode(userId);
    }


    /**
     * 退出登录
     *
     * @return
     */
    @PostMapping("loginOut")
    public String logout(HttpServletRequest request) {
        //清除session
        request.getSession().removeAttribute("user");
        request.getSession().removeAttribute("phone");
        // request.getSession().removeAttribute("code");
        return "退出登录成功";
    }
}
