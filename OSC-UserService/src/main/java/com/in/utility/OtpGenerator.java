package com.in.utility;

//OtpGenerator for generating unique 6-digit OTPs.
public class OtpGenerator {
    public String generateOtp() {
        int otp = (int) (Math.random() * 900000) + 100000; // Range: 100000 to 999999
        return String.valueOf(otp);
    }

    /*public String generateOtp() {
        // Generate a random UUID
        UUID uuid = UUID.randomUUID();

        // Take the least significant 8 characters of the UUID and convert it to a long value
        long otpSeed = Math.abs(uuid.getMostSignificantBits()) % 1000000;  // Ensure it fits within 6 digits

        // Ensure the OTP is within the 6-digit range (100000 to 999999)
        while (otpSeed < 100000) {
            otpSeed = Math.abs(uuid.getLeastSignificantBits()) % 1000000;
        }

        return String.format("%06d", otpSeed);  // Format as a 6-digit OTP
    }*/
}