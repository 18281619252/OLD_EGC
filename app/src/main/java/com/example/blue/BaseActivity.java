package com.example.blue;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

public class BaseActivity extends Activity {

    protected ProgressDialog mProgressDlg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    protected void showProgressDialog(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mProgressDlg == null) {
                    mProgressDlg = ProgressDialog.show(BaseActivity.this, null, msg, true);
                    mProgressDlg.setCancelable(true);
                }

            }
        });

    }

    protected void dismissProgressDialog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mProgressDlg != null) {
                    mProgressDlg.dismiss();
                    mProgressDlg = null;
                }
            }
        });

    }

    protected void setProgressMsg(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mProgressDlg != null && mProgressDlg.isShowing()) {
                    mProgressDlg.setMessage(msg);
                }
            }
        });

    }

    public void showToast(final String msg) {
        if (this.isFinishing()) {
            return;
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(BaseActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
