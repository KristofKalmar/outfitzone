package com.kristofkalmar.outfitzone.helper;

import android.content.Context;
import android.content.res.Configuration;

public class IsDarkMode {
    public static boolean isDarkModeEnabled(Context context) {
        int nightModeFlags = context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return nightModeFlags == Configuration.UI_MODE_NIGHT_YES;
    }
}
