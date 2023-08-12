package com.ksoot.problem.demo.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class AppConstants {

    public static final String REGEX_STATE_CODE = "^[A-Z]{2}$";

    public static final String REGEX_ALPHABETS_AND_SPACES = "^[a-zA-Z ]*$";

    public static final String REGEX_GSTIN =
        "^[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z]{1}[1-9A-Z]{1}Z[0-9A-Z]{1}$";

    public static final String REGEX_HSN_CODE = "^(?:\\d{4}|\\d{6}|\\d{8})$";

    public static final String GST_STATE_CODE = "^(?:\\d{1}|\\d{2})$";
}
