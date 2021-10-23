package com.convenientservices.web.utilities;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class Utils {

    public static boolean passwordMatching(String password, String matchingPassword) {
        return password.equals(matchingPassword);
    }

    public static String getRandomActivationCode() {
        return UUID.randomUUID().toString();
    }
}
