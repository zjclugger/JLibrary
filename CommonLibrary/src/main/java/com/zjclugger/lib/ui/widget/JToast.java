package com.zjclugger.lib.ui.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zjclugger.lib.R;

/**
 * <br>
 * Created by King.Zi on 2020/4/9.<br>
 * Copyright (c) 2020 zjclugger.com
 */
public class JToast {
    private static final String TAG = "Toast";
    private static Toast mToast;
    private Context mContext;
    private boolean mIsLongDuration = false;

    /**
     * 消息提醒<br>
     *
     * @param context
     * @param textResId
     */
    public JToast(Context context, int textResId) {
        this(context, R.drawable.ic_information, textResId, false);
    }

    public JToast(Context context, int textResId, boolean isLongDuration) {
        this(context, R.drawable.ic_information, textResId, isLongDuration);
    }

    public JToast(Context context, String message, boolean isLongDuration) {
        this(context, R.drawable.ic_information, message, isLongDuration);
    }


    /**
     * 消息提醒<br>
     *
     * @param context
     * @param message
     */
    public JToast(Context context, String message) {
        this(context, R.drawable.ic_information, message, false);
    }

    /**
     * 消息提醒<br>
     *
     * @param context
     * @param iconResId
     * @param textResId
     */
    public JToast(Context context, int iconResId, int textResId) {
        this(context, iconResId, textResId, false);
    }

    /**
     * 消息提醒<br>
     *
     * @param context
     * @param iconResId
     * @param message
     */
    public JToast(Context context, int iconResId, String message) {
        this(context, iconResId, message, false);
    }

    /**
     * 消息提醒<br>
     *
     * @param context
     * @param iconResId
     * @param textResId
     * @param isLongDuration
     */
    public JToast(Context context, int iconResId, int textResId, boolean isLongDuration) {
        this(context, iconResId, context.getString(textResId), isLongDuration);
    }

    /**
     * 消息提醒<br>
     *
     * @param context
     * @param iconResId
     * @param message
     * @param isLongDuration
     */
    public JToast(Context context, int iconResId, String message, boolean isLongDuration) {
        this.mContext = context;
        mIsLongDuration = isLongDuration;
        this.show(iconResId, message);
    }

    public JToast(Context context, int textResId, boolean isLongDuration, boolean hasIcon) {
        this.mContext = context;
        mIsLongDuration = isLongDuration;
        if (hasIcon) {
            this.show(R.drawable.ic_information, context.getString(textResId));
        } else {
            this.show(context.getString(textResId));
        }
    }

    public JToast(Context context, String message, boolean isLongDuration, boolean hasIcon) {
        this.mContext = context;
        mIsLongDuration = isLongDuration;
        if (hasIcon) {
            this.show(R.drawable.ic_information, message);
        } else {
            this.show(message);
        }
    }

    private void show(int iconId, String message) {
        if (mContext != null) {
            if (mToast != null) {
                mToast.cancel();
                mToast = null;
            }

            if (!TextUtils.isEmpty(message)) {
                mToast = new Toast(this.mContext);
                View toastView = LayoutInflater.from(this.mContext).inflate(R.layout.toast_layout
                        , null);
                ImageView toastIcon = (ImageView) toastView.findViewById(R.id.icon);
                TextView toastValue = (TextView) toastView.findViewById(R.id.message);
                mToast.setDuration(mIsLongDuration ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
                mToast.setGravity(Gravity.CENTER, 0, 0);
                mToast.setView(toastView);
                if (iconId > 0) {
                    toastIcon.setImageResource(iconId);
                } else {
                    toastIcon.setVisibility(View.GONE);
                }
                toastValue.setText(message);
                mToast.show();
            } else {
                Log.d(TAG, "JToast message is null");
            }
        } else {
            Log.e(TAG, "will show the toast " + message + ", but context is null");
        }
    }

    private void show(String message) {
        if (mContext != null) {
            if (mToast != null) {
                mToast.cancel();
                mToast = null;
            }

            if (!TextUtils.isEmpty(message)) {
                mToast = new Toast(this.mContext);
                View toastView = LayoutInflater.from(this.mContext).inflate(R.layout.toast_message_layout
                        , null);
                TextView toastValue = (TextView) toastView.findViewById(R.id.message);
                mToast.setDuration(mIsLongDuration ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
                mToast.setGravity(Gravity.BOTTOM, 0, 0);
                mToast.setView(toastView);
                toastValue.setText(message);
                mToast.show();
            } else {
                Log.d(TAG, "JToast message is null");
            }
        } else {
            Log.e(TAG, "will show the toast " + message + ", but context is null");
        }
    }
}
