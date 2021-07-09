package au.edu.unsw.infs3605.aboriginalplatform.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import au.edu.unsw.infs3605.aboriginalplatform.R;
import au.edu.unsw.infs3605.aboriginalplatform.utils.CountDownTimerSuspended;
import au.edu.unsw.infs3605.aboriginalplatform.utils.RegUtil;

public class ForgetPwdActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvTitle;
    private TextView tvUserNameDesc;
    private EditText etUserName;
    private TextView tvUserEmailDesc;
    private EditText etUserEmail;
    private TextView tvAuthCodeDesc;
    private EditText etAuthCode;
    private TextView tvPwdDesc;
    private EditText etPwd;
    private TextView tvConfirmPwdDesc;
    private EditText etConfirmPwd;
    private TextView tvSave;
    private TextView tvSendCode;

    private CountDownTimerSuspended mCountDownTimerSuspended;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pwd);
        tvTitle = findViewById(R.id.tvTitle);
        tvUserNameDesc = findViewById(R.id.tvUserNameDesc);
        etUserName = findViewById(R.id.etUserName);
        tvUserEmailDesc = findViewById(R.id.tvUserEmailDesc);
        etUserEmail = findViewById(R.id.etUserEmail);
        tvAuthCodeDesc = findViewById(R.id.tvAuthCodeDesc);
        etAuthCode = findViewById(R.id.etAuthCode);
        tvPwdDesc = findViewById(R.id.tvPwdDesc);
        etPwd = findViewById(R.id.etPwd);
        tvConfirmPwdDesc = findViewById(R.id.tvConfirmPwdDesc);
        etConfirmPwd = findViewById(R.id.etConfirmPwd);
        tvSave = findViewById(R.id.tvSave);
        tvSendCode = findViewById(R.id.tvSendCode);


        tvSendCode.setOnClickListener(this);
        tvSave.setOnClickListener(this);
        long intervalTime = 1000;
        mCountDownTimerSuspended = new CountDownTimerSuspended(60 * intervalTime, intervalTime) {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTick(long millisUntilFinished) {
                tvSendCode.setText("left " + millisUntilFinished / 1000 + " s");
            }

            @Override
            public void onFinish() {
                tvSendCode.setText("To resend");
            }
        };
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        String userEmail = etUserEmail.getText().toString().trim();
        if (TextUtils.isEmpty(userEmail)) {
            Toast.makeText(this, "user email is not empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!RegUtil.isEmail(userEmail)) {
            Toast.makeText(this, "user email is not valid", Toast.LENGTH_SHORT).show();
            return;
        }
        switch (v.getId()) {
            case R.id.tvSendCode:
                //Start timer
                mCountDownTimerSuspended.start();
                break;
            case R.id.tvSave:
                break;
            default:

        }
    }
}