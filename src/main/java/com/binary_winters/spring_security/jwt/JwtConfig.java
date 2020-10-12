package com.binary_winters.spring_security.jwt;

import org.springframework.context.annotation.Configuration;

import com.google.common.net.HttpHeaders;

//@ConfigurationProperties(prefix = "application.jwt")
@Configuration
public class JwtConfig {

    private String secretKey = "secure_eruces_serecu_curese_resecu";
    private String tokenPrefix = "Bearer ";
    private Integer tokenExpirationAfterDays = 10;

    public JwtConfig() {
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getTokenPrefix() {
        return tokenPrefix;
    }

    public void setTokenPrefix(String tokenPrefix) {
        this.tokenPrefix = tokenPrefix;
    }

    public Integer getTokenExpirationAfterDays() {
        return tokenExpirationAfterDays;
    }

    public void setTokenExpirationAfterDays(Integer tokenExpirationAfterDays) {
        this.tokenExpirationAfterDays = tokenExpirationAfterDays;
    }

    public String getAuthorizationHeader() {
        return HttpHeaders.AUTHORIZATION;
    }
}
