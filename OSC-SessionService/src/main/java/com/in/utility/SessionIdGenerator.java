package com.in.utility;

import java.util.Random;

public class SessionIdGenerator {
    public String generateSessionId() {
        return String.format("%08d", new Random().nextInt(100000000));
    }
}
