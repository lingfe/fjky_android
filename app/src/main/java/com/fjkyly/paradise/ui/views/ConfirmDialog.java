package com.fjkyly.paradise.ui.views;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.fjkyly.paradise.R;

public class ConfirmDialog extends Dialog {

    private TextView mMessage;
    private TextView mGivUp;
    private TextView mConfirm;
    private OnDialogActionClickListener mOnDialogActionClickListener = null;

    public ConfirmDialog(@NonNull Context context) {
        super(context);
    }

    public ConfirmDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected ConfirmDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initListener();
    }

    private void initListener() {
        mConfirm.setOnClickListener(v -> {
            if (mOnDialogActionClickListener != null) {
                mOnDialogActionClickListener.onConfirmClick();
                dismiss();
            }
        });
        mGivUp.setOnClickListener(v -> {
            if (mOnDialogActionClickListener != null) {
                mOnDialogActionClickListener.onGiveUpClick();
                dismiss();
            }
        });
    }

    private void initView() {
        mMessage = findViewById(R.id.dialogMessageTv);
        mConfirm = findViewById(R.id.dialogConfirmTv);
        mGivUp = findViewById(R.id.dialogGiveUpTv);
    }

    public void setDialogMessage(String text) {
        mMessage.setText(text);
    }

    public void setOnDialogActionClickListener(OnDialogActionClickListener onDialogActionClickListener) {
        mOnDialogActionClickListener = onDialogActionClickListener;
    }

    public static class OnDialogActionSimpleListener implements OnDialogActionClickListener {

        @Override
        public void onConfirmClick() {

        }

        @Override
        public void onGiveUpClick() {

        }
    }

    public interface OnDialogActionClickListener {
        void onConfirmClick();

        void onGiveUpClick();
    }
}
