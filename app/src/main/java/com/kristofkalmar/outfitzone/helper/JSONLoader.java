package com.kristofkalmar.outfitzone.helper;

import android.content.Context;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class JSONLoader {
    public static String loadJson(Context context, String fileName) {
        String jsonString = null;
        try {
            InputStream inputStream = context.getAssets().open("config/" + fileName + ".json");

            int size = inputStream.available();
            byte[] buffer = new byte[size];

            inputStream.read(buffer);
            inputStream.close();

            jsonString = new String(buffer, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonString;
    }
}
