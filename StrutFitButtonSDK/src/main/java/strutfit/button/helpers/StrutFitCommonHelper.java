package strutfit.button.helpers;

import android.content.Context;
import android.content.SharedPreferences;

public class StrutFitCommonHelper {

    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String ACTIVE_FOOT_MEASUREMENT_CODE = "StrutFitActiveFootMeasurementCode";
    private static final String ACTIVE_BODY_MEASUREMENT_CODE = "StrutFitActiveBodyMeasurementCode";
    private static final String UNIQUE_USER_ID = "StrutFitUniqueUserId";

    public static String getLocalFootMCode(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        return sharedPreferences.getString(ACTIVE_FOOT_MEASUREMENT_CODE, null);
    }

    public static void setLocalFootMCode(Context context, String measurementCode) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(ACTIVE_FOOT_MEASUREMENT_CODE, measurementCode);

        editor.apply();
    }

    public static String getLocalBodyMCode(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        return sharedPreferences.getString(ACTIVE_BODY_MEASUREMENT_CODE, null);
    }

    public static void setLocalBodyMCode(Context context, String measurementCode) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(ACTIVE_BODY_MEASUREMENT_CODE, measurementCode);

        editor.apply();
    }

    public static String getLocalUserId(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        return sharedPreferences.getString(UNIQUE_USER_ID, null);
    }

    public static void setLocalUserId(Context context, String userId) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(UNIQUE_USER_ID, userId);

        editor.apply();
    }
}
