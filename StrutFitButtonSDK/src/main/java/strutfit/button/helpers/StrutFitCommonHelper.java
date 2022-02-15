package strutfit.button.helpers;

import android.content.Context;
import android.content.SharedPreferences;

public class StrutFitCommonHelper {

    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String Measurement_Code = "measurementCode";

    public static String getLocalMcode(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Measurement_Code, "");
    }

    public static Boolean getStrutFitInUse(Context context) {
        return getLocalMcode(context) != "";
    }

    public static void setLocalMcode(Context context, String measurementCode) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(Measurement_Code, measurementCode);

        editor.apply();
    }
}
