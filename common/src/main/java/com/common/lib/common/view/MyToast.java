package com.common.lib.common.view;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.common.lib.common.R;

/**
 * Created  on 2018/5/23.
 */

public class MyToast {
    public static final int LENGTH_SHORT = Toast.LENGTH_SHORT;
    public static final int LENGTH_LONG = Toast.LENGTH_LONG;

    Toast toast;
    Context mContext;
    TextView toastTextField;

    public MyToast(Context context, Activity activity) {
        mContext = context;
        toast = new Toast(mContext);
        toast.setGravity(Gravity.CENTER, 0, 0);// 位置居中
        View toastRoot = activity.getLayoutInflater().inflate(R.layout.toast_view, null);
        toastTextField = (TextView) toastRoot.findViewById(R.id.toast_text);
        toast.setView(toastRoot);
    }

    public void setDuration(int d) {
        toast.setDuration(d);
    }

    public void setText(String t) {
        toastTextField.setText(t);
    }

    public static MyToast makeText(Context context, Activity activity, String text, int duration) {
        MyToast myToast = new MyToast(context, activity);
        myToast.setText(text);
        myToast.setDuration(duration);
        return myToast;
    }

    public void show() {
        toast.show();
    }
}
