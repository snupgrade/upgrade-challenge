package com.upgrade.challenge.commonservicelibrary.constant;

public class CommonRegex {

    public static final String UUID_REGEX = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";
    public static final String DATABASE_TYPE_REGEX = "^[A-Z0-9_]{2,}$";
    public static final String NAME_REGEX = "^[\\p{L} ,.'-]*$";
    public static final String BASE_36_ID_REGEX = "^[A-Z0-9_]{7}$";

    private CommonRegex() {

    }
}
