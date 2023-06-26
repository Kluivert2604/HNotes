package com.kieudatquochung.hnotes;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import androidx.annotation.NonNull;

public class MyDialog extends Dialog {
    private Button mBtnDone;
    public MyDialog(@NonNull Context context) {
        super(context);
        initView();
    }

    private void initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_notification);
        setCanceledOnTouchOutside(false);

        mBtnDone = findViewById(R.id.btnDone);
        mBtnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
