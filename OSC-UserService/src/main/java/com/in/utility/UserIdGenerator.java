package com.in.utility;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

//UserIdGenerator for generating unique user IDs like "user0001".
public class UserIdGenerator {
    private final AtomicInteger counter = new AtomicInteger(1);
    public String generateUserId() {
        // Generate a unique UUID
        String uniqueId = UUID.randomUUID().toString();

        // Use the hashCode of the UUID and restrict it to 4 digits
        int randomPart = uniqueId.hashCode() & 0xFFFF;

        // formatting it as "userXXXX" (e.g., user0271, user4312)
        return String.format("user%04d", Math.abs(randomPart) % 1000);
    }
}