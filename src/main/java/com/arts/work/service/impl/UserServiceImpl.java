package com.arts.work.service.impl;

import com.arts.work.common.pojo.dto.LoginDTO;
import com.arts.work.common.pojo.vo.UserVO;
import com.arts.work.common.utils.*;
import com.arts.work.mapper.UserMapper;
import com.arts.work.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.HtmlEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper loginMapper;

    /**
     * 注入redisTemplate用于缓存数据
     */
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public RestResponse login(LoginDTO loginDTO) {
        if (null == loginDTO) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        try {
            if (redisTemplate.hasKey(loginDTO.getEmail())) {
                // 取出对应邮箱地址为key的value值
                String storyCode = redisTemplate.opsForValue().get(loginDTO.getEmail());
                if (StringUtils.isBlank(storyCode)) {
                    return ResultUtils.error(ErrorCode.EMAIL_CODE_EXPIRE);
                }
                // 判断存在redis中的验证码和传入验证码是否相同，相同返回true
                if (!loginDTO.getCode().equals(storyCode)) {
                    return ResultUtils.error(ErrorCode.EMAIL_CODE_INVALID);
                }
                // 给用户jwt加密生成token【创建JWT令牌】
                String token = null;
                String userId = UUIDUtils.get8UUID();
                Map<String, Object> map = new HashMap<>();
                // 判断该用户是否注册 用户还未注册，自动注册
                UserVO userVO = loginMapper.login(loginDTO.getEmail());
                if (EmptyUtils.isEmpty(userVO)) {
                    token = JwtUtils.getToken(userId, 24 * 3600 * 1000L);
                    loginMapper.register(userId, loginDTO.getEmail());
                    map.put("success", "用户注册成功");
                    map.put("userId", userId);
                } else {
                    token = JwtUtils.getToken(userVO.getUserId(), 24 * 3600 * 1000L);
                    map.put("success", "用户登录成功");
                    map.put("userId", userVO.getUserId());
                }
                map.put("email", loginDTO.getEmail());
                map.put("token", token);
                return ResultUtils.success(map);
            }
        } catch (Exception e) {
            log.error(e.toString());
        }
        return ResultUtils.error(ErrorCode.EMAIL_CODE_EXPIRE);
    }

    @Override
    public RestResponse sendAuthCodeEmail(String emailAddress) {
        if (StringUtils.isBlank(emailAddress)) {
            ResultUtils.error(ErrorCode.PARAMS_ERROR, "邮箱不能为空");
        }
        String verificationCode = null;
        try {
            log.info("====>请求参数邮箱地址为{}", emailAddress);
            // 生成4位随机验证码
            // 截取当前时间戳的后四位模拟验证码
            long timestamp = System.currentTimeMillis();
            verificationCode = Long.toString(timestamp).substring(Long.toString(timestamp).length() - 4);
            log.info("====>验证码为:{}", verificationCode);
            // 存入redis
            // 将传入邮箱地址为key，验证码为value，存入redis中，并设置超时时间为5分钟
            redisTemplate.opsForValue().set(emailAddress, verificationCode, 10, TimeUnit.MINUTES);
            // 发送验证码到目标邮箱
            HtmlEmail mail = new HtmlEmail();
            /*发送邮件的服务器 126邮箱为smtp.126.com,163邮箱为163.smtp.com，QQ为smtp.qq.com*/
            mail.setHostName("smtp.163.com");
            /*不设置发送的消息有可能是乱码*/
            mail.setCharset("UTF-8");
            /*IMAP/SMTP服务的密码 username为你开启发送验证码功能的邮箱号 password为你在qq邮箱获取到的一串字符串*/
            mail.setAuthentication("15285480846@163.com", "XIOBALSLDSSRRGAC");
            /*发送邮件的邮箱和发件人*/
            mail.setFrom("15285480846@163.com", "屠国洋");
            /*使用安全链接*/
            mail.setSSLOnConnect(true);
            /*接收的邮箱*/
            mail.addTo(emailAddress);
            /*设置邮件的主题*/
            mail.setSubject("登录验证码");
            /*设置邮件的内容*/
            mail.setMsg("尊敬的用户:您好! 您的邮箱登录验证码为:" + verificationCode + "(有效期为一分钟)");
            mail.send();//发送
            long end = System.currentTimeMillis();
            log.info("====>邮箱验证码发送成功,耗时为：" + (end - timestamp) / 1000 + "秒");
        } catch (Exception e) {
            return ResultUtils.error(ErrorCode.EMAIL_SEND_ERROR);
        }
        return ResultUtils.success("邮箱验证码发送成功,验证码为：" + verificationCode);
    }

    @Override
    public RestResponse generateInvitationCode(String userId) {

        return null;
    }
}
