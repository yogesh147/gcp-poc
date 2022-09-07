package com.cloud.google.utility;

import java.math.BigInteger;
import java.util.UUID;

public enum AppUtil {

    INSTANCE;
    private final String BLANK_STRING = "";
    private final String HYPHEN_STRING = "-";
    public String uniqueIdentifier(String... prefix) {
        return (prefix.length > BigInteger.ZERO.intValue() ? prefix[BigInteger.ZERO.intValue()] : BLANK_STRING).concat(UUID.randomUUID().toString().replace(HYPHEN_STRING, BLANK_STRING));
    }
}

