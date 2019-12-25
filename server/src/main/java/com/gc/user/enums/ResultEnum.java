package com.gc.user.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultEnum {

    LOGIN_FAILED(1, "登录失败"),
    ROLE_ERROR(2,"角色权限有误");

    private Integer code;

    private String message;
}
