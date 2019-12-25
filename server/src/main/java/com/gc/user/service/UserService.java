package com.gc.user.service;

import com.gc.user.entity.UserInfo;

public interface UserService {

    UserInfo findByOpenid(String openid);
}
