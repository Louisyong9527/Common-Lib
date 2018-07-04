package com.common.lib.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.common.lib.common.utils.AndroidUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView mTvHellow = findViewById(R.id.tv_hello);

        mTvHellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AndroidUtils.showToast(getBaseContext(),MainActivity.this,"登录失败，请稍后再试");

            }
        });

    }
}
