package com.innowise.innowisebankproject.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class JwtProperties {
    public final String JWT_KEY = "JWT_KEY";
    public final String EMAIL_CLAIM = "email";
    public final String ROLE_CLAIM = "roles";
}
