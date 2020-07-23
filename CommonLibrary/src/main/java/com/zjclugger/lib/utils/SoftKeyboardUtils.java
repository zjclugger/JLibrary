package com.zjclugger.lib.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class SoftKeyboardUtils {

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm =
                (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);
        View v = activity.getWindow().peekDecorView();
        if (null != v) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    public static void hideKeyboard(Context context, ViewGroup view) {
        view.requestFocus();
        InputMethodManager im =
                (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        try {
            im.hideSoftInputFromWindow(view.getWindowToken(), 0);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showKeyboard(Context context, View view) {
        InputMethodManager im =
                (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        im.showSoftInput(view, 0);
    }
}
