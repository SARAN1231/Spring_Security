package com.saran.SecurityDemo.Models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static com.saran.SecurityDemo.Models.Permission.*;

@RequiredArgsConstructor
@Getter
public enum Role {
    USER (Collections.emptySet()), // for user no permissions

    ADMIN(
            Set.of(
                    ADMIN_UPDATE,
                    ADMIN_CREATE,
                    ADMIN_DELETE,
                    ADMIN_READ,
                    MANAGER_CREATE,
                    MANAGER_DELETE,
                    MANAGER_UPDATE,
                    MANAGER_READ
            )
    ),

    MANAGER(
            Set.of(
                    MANAGER_CREATE,
                    MANAGER_DELETE,
                    MANAGER_UPDATE,
                    MANAGER_READ
            )
    )

    ;

    private final Set<Permission> permissions;


    // method to get permissions and role
    public List<SimpleGrantedAuthority> getSimpleGrantedAuthorities() {
        var authorities = new java.util.ArrayList<>(getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .toList()); // getting the permissions of role and converting to SimpleGrantedAuthority
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name())); // getting the role and converting to SimpleGrantedAuthority
        return authorities;
    }
}
