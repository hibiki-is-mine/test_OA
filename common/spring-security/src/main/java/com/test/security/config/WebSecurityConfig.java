package com.test.security.config;

import com.test.security.custom.CustomMD5PasswordEncoder;
import com.test.security.custom.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;

/**
 * @return
 */
@Configuration
@EnableWebSecurity//开启springSecurity的默认行为
public class WebSecurityConfig extends WebSecurityConfiguration {
    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    CustomMD5PasswordEncoder customMd5PasswordEncoder;

}
