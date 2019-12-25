package com.gc.user.controller;

import com.gc.user.constant.CookieConstant;
import com.gc.user.constant.RedisConstant;
import com.gc.user.entity.UserInfo;
import com.gc.user.enums.ResultEnum;
import com.gc.user.enums.RoleEnum;
import com.gc.user.service.UserService;
import com.gc.user.utils.CookieUtil;
import com.gc.user.utils.ResultVOUtil;
import com.gc.user.vo.ResultVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private StringRedisTemplate redisTemplate;


    @GetMapping("/buyer")
    public ResultVO buyer(@RequestParam("openid") String openid,
                          HttpServletResponse response) {
        UserInfo userInfo = userService.findByOpenid(openid);
        if (userInfo == null) {
            return ResultVOUtil.errror(ResultEnum.LOGIN_FAILED);
        }
        if (RoleEnum.BUYER.getCode() != userInfo.getRole()) {
            return ResultVOUtil.errror(ResultEnum.ROLE_ERROR);
        }

        CookieUtil.set(response, CookieConstant.OPENID, openid, CookieConstant.EXPIRE);
        return ResultVOUtil.success();
    }

    @GetMapping("/seller")
    public ResultVO seller(@RequestParam("openid") String openid,
                           HttpServletResponse response,
                           HttpServletRequest request) {

        Cookie cookie = CookieUtil.get(request, CookieConstant.TOKEN);

        if (cookie != null
                && StringUtils.isNotBlank(redisTemplate.opsForValue().get(
                String.format(RedisConstant.TOKEN_TEPLATE, cookie.getValue())))) {
            return ResultVOUtil.success();
        }

        UserInfo userInfo = userService.findByOpenid(openid);
        if (userInfo == null) {
            return ResultVOUtil.errror(ResultEnum.LOGIN_FAILED);
        }
        if (RoleEnum.SELLER.getCode() != userInfo.getRole()) {
            return ResultVOUtil.errror(ResultEnum.ROLE_ERROR);
        }

        String token = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(String.format(RedisConstant.TOKEN_TEPLATE, token),
                openid,
                CookieConstant.EXPIRE,
                TimeUnit.SECONDS);

        CookieUtil.set(response, CookieConstant.TOKEN, token, CookieConstant.EXPIRE);
        return ResultVOUtil.success();
    }
}
