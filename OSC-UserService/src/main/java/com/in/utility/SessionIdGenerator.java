package com.in.utility;

import java.util.UUID;

public class SessionIdGenerator {
    public String generateSessionId() {
        long mostSigBits = UUID.randomUUID().getMostSignificantBits();
        return String.format("%08d", Math.abs(mostSigBits % 100000000));
    }
}
