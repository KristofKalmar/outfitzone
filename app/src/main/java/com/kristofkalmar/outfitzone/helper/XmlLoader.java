package com.kristofkalmar.outfitzone.helper;

import android.content.Context;

import com.kristofkalmar.outfitzone.R;
import com.kristofkalmar.outfitzone.models.NavigationListItem;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.List;

public class XmlLoader {
    public static List<NavigationListItem> loadNavigationItems(Context context) {
        List<NavigationListItem> items = new ArrayList<>();
        try {
            XmlPullParser parser = context.getResources().getXml(R.xml.navigation_items);
            String title = null;
            String image = null;

            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    String tagName = parser.getName();
                    if ("title".equals(tagName)) {
                        String value = parser.nextText();
                        if (value.startsWith("@string/")) {
                            int stringResId = context.getResources().getIdentifier(
                                    value.substring(8), "string", context.getPackageName());
                            title = context.getString(stringResId);
                        } else {
                            title = value;
                        }
                    } else if ("image".equals(tagName)) {
                        image = parser.nextText();
                    }
                } else if (eventType == XmlPullParser.END_TAG) {
                    if ("item".equals(parser.getName())) {
                        items.add(new NavigationListItem(image, title));
                        title = null;
                        image = null;
                    }
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return items;
    }
}