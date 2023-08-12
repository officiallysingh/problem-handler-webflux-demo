package com.ksoot.problem.demo.util;

import java.time.ZoneId;
import java.util.Locale;
import java.util.TimeZone;

/** Application common constants. */
public final class CommonConstant {

  public static final String SYSTEM_USER = "SYSTEM";

  public static final String SLASH = "/";

  // Datetime >>>
  public static final ZoneId ZONE_ID_UTC = ZoneId.of("UTC");

  public static final ZoneId ZONE_ID_IST = ZoneId.of("Asia/Kolkata");

  public static final TimeZone TIME_ZONE_UTC = TimeZone.getTimeZone(ZONE_ID_UTC);

  public static final TimeZone TIME_ZONE_IST = TimeZone.getTimeZone(ZONE_ID_IST);

  public static final ZoneId DEFAULT_ZONE_ID = ZoneId.systemDefault();

  public static final TimeZone DEFAULT_TIME_ZONE = TIME_ZONE_IST;
  // <<< Datetime

  public static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

  public static final Locale DEFAULT_LOCALE = Locale.getDefault();

  public static final String API = SLASH;

  public static final String VERSION_v1 = "v1";

  public static final String CURRENT_VERSION = VERSION_v1;

  private CommonConstant() {
    throw new IllegalStateException("Just a constants container, not supposed to be instantiated");
  }
}
