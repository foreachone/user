package com.gc.user.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoleEnum {

    BUYER(1, "买家"),
    SELLER(2, "卖家");

    private Integer code;

    private String message;
}
