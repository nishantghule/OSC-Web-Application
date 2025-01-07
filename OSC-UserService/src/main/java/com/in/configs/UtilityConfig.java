package com.in.configs;

import com.in.utility.SessionIdGenerator;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import com.in.utility.OtpGenerator;
import com.in.utility.UserIdGenerator;


@Configuration
public class UtilityConfig {

    @Bean
    public UserIdGenerator userIdGenerator() {
        return new UserIdGenerator();
    }

    @Bean
    public OtpGenerator otpGenerator() {
        return new OtpGenerator();
    }

    @Bean
    public SessionIdGenerator sessionIdGenerator(){return new SessionIdGenerator();}
}
