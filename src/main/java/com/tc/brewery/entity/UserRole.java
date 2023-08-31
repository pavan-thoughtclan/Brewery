package com.tc.brewery.entity;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum UserRole {
    ROLE_USER,
    ROLE_ADMIN;

    public static List<UserRole> fromString(String rolesString) {
        return Arrays.stream(rolesString.split(","))
                .map(role -> UserRole.valueOf(role.trim()))
                .collect(Collectors.toList());
    }
}
