package com.darglk.socialapp.common;

import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class InstantWrapper {
    public Instant now() {
        return Instant.now();
    }
}
