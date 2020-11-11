package com.common.lib.common.etoast2;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.common.lib.common.R;
import com.common.lib.common.etoast2.EToastUtils;

/**
 * Author: Blincheng.
 * Date: 2017/6/30.
 * Description:
 */

public class Toast {
    private Object mToast;

    private Toast(Activity context, CharSequence message, int duration, boolean withBg) {
        if (EToastUtils.isNotificationEnabled(context)) {
            if (withBg){
                mToast = new android.widget.Toast(context);
                ((android.widget.Toast) mToast).setGravity(Gravity.CENTER, 0, 0);// 位置居中
                ((android.widget.Toast) mToast).setDuration(duration);
                View toastRoot = context.getLayoutInflater().inflate(R.layout.toast_view_bg , null);
                TextView toastTextField = (TextView) toastRoot.findViewById(R.id.toast_text);
                toastTextField.setText(message);
                ((android.widget.Toast) mToast).setView(toastRoot);
            }else {
                mToast = android.widget.Toast.makeText(context, message, duration);
            }

        } else {
            //当没有通知权限，并且context是Application时，调用EToastUtils获取当前Activity
            if (EToastUtils.getInstance().getActivity() != null)
                mToast = withBg? EToast2.makeTextWithBg(EToastUtils.getInstance().getActivity(), message, duration): EToast2.makeText(EToastUtils.getInstance().getActivity(), message, duration);
        }
    }

    private Toast(Activity context, int resId, int duration) {
        if (EToastUtils.isNotificationEnabled(context)) {
            mToast = android.widget.Toast.makeText(context, resId, duration);
        } else {
            //当没有通知权限，并且context是Application时，调用EToastUtils获取当前Activity
            if (EToastUtils.getInstance().getActivity() != null)
                mToast = EToast2.makeText(EToastUtils.getInstance().getActivity(), resId, duration);
        }
    }

    public static Toast makeText(Activity context, CharSequence message, int duration) {
        return new Toast(context, message, duration,false);
    }

    public static Toast makeTextWithBg(Activity context, CharSequence message, int duration) {
        return new Toast(context, message, duration,true);
    }


    public static Toast makeText(Activity context, int resId, int duration) {
        return new Toast(context, resId, duration);
    }

    public static Toast makeTextWithBg(Activity context, int resId, int duration) {
        return new Toast(context, resId, duration);
    }

    public void show() {
        if (mToast instanceof EToast2) {
            ((EToast2) mToast).show();
        } else if (mToast instanceof android.widget.Toast) {
            ((android.widget.Toast) mToast).show();
        }
    }

    public void cancel() {
        if (mToast instanceof EToast2) {
            ((EToast2) mToast).cancel();
        } else if (mToast instanceof android.widget.Toast) {
            ((android.widget.Toast) mToast).cancel();
        }
    }

    public void setText(CharSequence s) {
        if (mToast instanceof EToast2) {
            ((EToast2) mToast).setText(s);
        } else if (mToast instanceof android.widget.Toast) {
            ((android.widget.Toast) mToast).setText(s);
        }
    }
}