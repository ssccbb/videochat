package com.feiyu.videochat.views.mine;

import android.app.Activity;
import android.view.View;

import com.feiyu.videochat.BuildConfig;

/**
 * Created by Sun on 15/12/2017.
 */

public class V {

    @SuppressWarnings("unchecked")
    public static <T extends View> T get(View parent, int id) {
        if (parent == null) {
            return null;
        }
        try {
            return (T) parent.findViewById(id);
        } catch (Exception e) {
            if (BuildConfig.DEBUG) {
                throw new RuntimeException("findViewById error");
            } else {
                return null;
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static <T extends View> T get(Activity activity, int id) {
        if (activity == null) {
            return null;
        }
        try {
            return (T) activity.findViewById(id);
        } catch (Exception e) {
            if (BuildConfig.DEBUG) {
                throw new RuntimeException("findViewById error");
            } else {
                return null;
            }
        }
    }
}
