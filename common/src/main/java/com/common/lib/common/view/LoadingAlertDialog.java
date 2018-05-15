package com.common.lib.common.view;


import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import com.common.lib.common.R;
import com.common.lib.common.utils.StringUtils;


/**
 * Created by ly on 2016/11/16.
 */
public class LoadingAlertDialog extends AlertDialog {
    private TextView mMessage;

    public LoadingAlertDialog(Context context) {
        super(context, R.style.LoadingAlertDialog);
    }
    public LoadingAlertDialog(Context context, int theme) {
        super(context,theme);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading_layout);
        //点击imageview外侧区域，动画不会消失
        init();
        mMessage = (TextView) findViewById(R.id.message);
    }

    private void init() {
        //设置不可取消，点击其他区域不能取消，实际中可以抽出去封装供外包设置
        setCancelable(false);
        setCanceledOnTouchOutside(false);
    }

    public void show(String msg) {
        super.show();
        if (!StringUtils.isEmptyType(msg)) {
            mMessage.setText(msg);
        }else{
            mMessage.setVisibility(View.GONE);
        }
    }

}
