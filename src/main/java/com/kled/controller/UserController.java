package com.kled.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kled.common.BaseContext;
import com.kled.common.R;
import com.kled.domain.ShoppingCart;
import com.kled.domain.User;
import com.kled.service.UserService;
import com.kled.utils.SMSUtils;
import com.kled.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    /**
     * 移动端发送短信
     *
     * @param user
     * @param session
     * @return
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session) {
        //获取手机号
        String phone = user.getPhone();

        if (StringUtils.isNotEmpty(phone)) {
            //生成随机4位验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("code={}", code);

            //调用阿里云提供的短信服务，发送短信
//            SMSUtils.sendMessage("阿里云短信测试","SMS_154950909",phone,code);
            //存储验证码保存到session
//            session.setAttribute(phone,code);
            //将生成的验证码缓存到Redis中设置有效期五分钟
            stringRedisTemplate.opsForValue().set(phone, code, 5, TimeUnit.MINUTES);
            return R.success("手机验证码短信发送成功");
        }

        return R.error("短信发送失败");
    }

    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session) {
        //获取手机号

        String phone = map.get("phone").toString();
        //获取验证码
        String code = map.get("code").toString();
        //从session中获取保存的验证码
//        Object codeInSession = session.getAttribute(phone);
        //从redis中获取缓存的验证码
        Object codeInSession = stringRedisTemplate.opsForValue().get(phone);
        //进行验证码的对比(页面提交的验证码和Session中保存的验证码比对)
        if (codeInSession != null && codeInSession.equals(code)) {
            //如果能够比对成功，说明登录成功

            //判断当前手机号对应的用户位新用户，如果是新用户就自动进行注册
            LambdaQueryWrapper<User> qw = new LambdaQueryWrapper<>();
            qw.eq(User::getPhone, phone);

            User user = userService.getOne(qw);
            if (user == null) {
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }
            session.setAttribute("user", user.getId());
            //如果用户登录曾公
            stringRedisTemplate.delete(phone);
            return R.success(user);
        }
        return R.error("登录失败，手机号或验证码不正确");
    }


}
