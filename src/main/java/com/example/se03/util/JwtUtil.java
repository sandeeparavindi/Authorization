package com.example.se03.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@PropertySource(ignoreResourceNotFound = true,
value = "classpath:otherprops.properties")

public class JwtUtil implements Serializable {
    private static final long serialVersionUTD = 234234523523L;
    public static final long JWT_TOKEN_VALIDITY = 24 * 60 * 60 * 12;
    @Value("${jwt.secret}")
    private String secretKey;
}
