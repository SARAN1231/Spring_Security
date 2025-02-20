package com.saran.SecurityDemo.Models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@RequiredArgsConstructor
public enum Permission {

    ADMIN_READ("admin:read"),
    ADMIN_CREATE("admin:create"),
    ADMIN_DELETE("admin:delete"),
    ADMIN_UPDATE("admin:update"),

    MANAGER_READ("manager:read"),
    MANAGER_CREATE("manager:create"),
    MANAGER_DELETE("manager:delete"),
    MANAGER_UPDATE("manager:update"),
    ;

    private final String permission;
}
