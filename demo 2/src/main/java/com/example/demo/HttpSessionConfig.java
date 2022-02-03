package com.agk.nftGame.config;

import org.springframework.context.annotation.Bean;
import org.springframework.session.data.mongo.JdkMongoSessionConverter;
import org.springframework.session.data.mongo.config.annotation.web.http.EnableMongoHttpSession;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.session.web.http.HeaderHttpSessionIdResolver;
import org.springframework.session.web.http.HttpSessionIdResolver;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;
import java.time.Duration;

@EnableMongoHttpSession(maxInactiveIntervalInSeconds = 3600)
public class HttpSessionConfig {

    @Bean
    public JdkMongoSessionConverter jdkMongoSessionConverter() {
        return new JdkMongoSessionConverter(Duration.ofMinutes(30));
    }

//    @Bean
//    public DefaultCookieSerializer customCookieSerializer(){
//        DefaultCookieSerializer cookieSerializer = new DefaultCookieSerializer();
//
//        cookieSerializer.setUseHttpOnlyCookie(false);
//        return cookieSerializer;
//    }
//
//    @Bean
//    public CookieSerializer cookieSerializer() {
//        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
//        serializer.setCookieName("JSESSIONID");
//        serializer.setCookiePath("/");
//        serializer.setUseSecureCookie(true);
//        serializer.setDomainNamePattern("^.+?\\.(\\w+\\.[a-z]+)$");
//        return serializer;
//    }
}