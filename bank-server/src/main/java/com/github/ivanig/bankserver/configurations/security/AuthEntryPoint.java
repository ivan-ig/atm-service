package com.github.ivanig.bankserver.configurations.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
public class AuthEntryPoint extends BasicAuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        response.addHeader("WWW-Authenticate", "Realm: \"" + getRealmName() + "\"");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        log.debug("Authorization failed.");

        PrintWriter writer = response.getWriter();
        writer.println("HTTP Status 401 - Authorization required.");
    }

    @Override
    public void afterPropertiesSet() {
        setRealmName("Bank ATM");
        super.afterPropertiesSet();
    }
}
