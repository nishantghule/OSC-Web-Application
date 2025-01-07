package com.in.configs;

import com.in.utility.SessionIdGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UtilityConfig {
    @Bean
    public SessionIdGenerator sessionIdGenerator(){
      return new SessionIdGenerator();
  }
}
