package com.darglk.socialapp.posts.config;

import com.darglk.socialapp.common.InstantWrapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import static org.mockito.Mockito.mock;

@Configuration
public class TestConfigruation {

    @Bean
    @Profile("test")
    public InstantWrapper instantWrapper() {
        return mock(InstantWrapper.class);
    }
}
