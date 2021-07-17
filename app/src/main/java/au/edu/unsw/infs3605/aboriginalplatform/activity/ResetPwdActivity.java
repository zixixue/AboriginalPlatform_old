package au.edu.unsw.infs3605.aboriginalplatform.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;

import au.edu.unsw.infs3605.aboriginalplatform.R;
import au.edu.unsw.infs3605.aboriginalplatform.entity.UserVerificationCode;
import au.edu.unsw.infs3605.aboriginalplatform.utils.CountDownTimerSuspended;
import au.edu.unsw.infs3605.aboriginalplatform.utils.RegUtil;
import au.edu.unsw.infs3605.aboriginalplatform.utils.SendEmailTool;

import static au.edu.unsw.infs3605.aboriginalplatform.Constants.TABLE_USER_VERIFY_CODE;

public class ResetPwdActivity extends AppCompatActivity implements View.OnClickListener {

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

    private String mVerificationCodeId;
    private FirebaseFirestore mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pwd);
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

        mDb = FirebaseFirestore.getInstance();
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
                //存储到firebase
                mDb.collection(TABLE_USER_VERIFY_CODE)
                        .whereEqualTo("userEmail", userEmail)
                        .get()
                        .addOnSuccessListener(queryDocumentSnapshots -> {
                            DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                            if (documentSnapshot == null) {
                                addCode(userEmail);
                            } else {
                                UserVerificationCode userVerificationCode = documentSnapshot.toObject(UserVerificationCode.class);
                                long createTime = SystemClock.currentThreadTimeMillis();
                                if (userVerificationCode == null || createTime > userVerificationCode.getInvalidTime()) {
                                    //存储到firebase，验证码库，邮箱唯一标识，时间戳15分钟有效期
                                    if (userVerificationCode == null) {
                                        addCode(userEmail);
                                    } else {
                                        DocumentReference documentReference = documentSnapshot.getReference();
                                        //有效时间15分钟
                                        long invalidTime = createTime + 15 * 60 & 1000;
                                        String code = generateVerifyCode();
                                        documentReference.update("verifyCode", code,
                                                "createTime", createTime,
                                                "invalidTime", invalidTime)
                                                .addOnSuccessListener(unused -> {
                                                    Toast.makeText(ResetPwdActivity.this, "Send VerificationCode Success", Toast.LENGTH_SHORT).show();
                                                    Executors.newSingleThreadExecutor().execute(() -> SendEmailTool.getInstance().sendEmail("xuezixi1124@gmail.com", ".", "找回密码", "找回密码的验证码为：" + code));
                                                    mCountDownTimerSuspended.start();
                                                })
                                                .addOnFailureListener(e -> Toast.makeText(ResetPwdActivity.this, "Send VerificationCode failed", Toast.LENGTH_SHORT).show());
                                    }
                                }
                            }
                        })
                        .addOnFailureListener(e -> Toast.makeText(ResetPwdActivity.this, "Send VerificationCode failed", Toast.LENGTH_SHORT).show());
                break;
            case R.id.tvSave:
                long createTime = SystemClock.currentThreadTimeMillis();
                String userPwd = etPwd.getText().toString().trim();
                String userConfirmPwd = etConfirmPwd.getText().toString().trim();
                if (TextUtils.isEmpty(userPwd)) {
                    Toast.makeText(this, "USER PASSWORD CANNOT BE EMPTY", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(userConfirmPwd)) {
                    Toast.makeText(this, "USER CONFIRM PASSWORD CANNOT BE EMPTY", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!TextUtils.equals(userPwd, userConfirmPwd)) {
                    Toast.makeText(this, "THE TWO PASSWORDS YOU TYPED DO NOT MATCH", Toast.LENGTH_SHORT).show();
                    return;
                }
                mDb.collection(TABLE_USER_VERIFY_CODE)
                        .whereEqualTo("userEmail", userEmail)
                        .whereLessThanOrEqualTo("invalidTime", createTime)
                        .whereGreaterThanOrEqualTo("createTime", createTime)
                        .get()
                        .addOnSuccessListener(queryDocumentSnapshots -> {
                            DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                            if (documentSnapshot != null) {
                                UserVerificationCode userVerificationCode = documentSnapshot.toObject(UserVerificationCode.class);
                                if (userVerificationCode != null) {
                                    FirebaseAuth auth = FirebaseAuth.getInstance();
                                    // TODO: 2021/7/17 尝试firebase自己发送验证码和找回密码 
//                                    FirebaseUser user = auth.getCurrentUser();
//                                    auth.confirmPasswordReset();
//                                    auth.verifyPasswordResetCode("");
//                                    auth.sendPasswordResetEmail()
//                                    user.updatePassword();
                                }
                            }
                        })
                        .addOnFailureListener(e -> Toast.makeText(ResetPwdActivity.this, "Send VerificationCode failed", Toast.LENGTH_SHORT).show());
                break;
            default:
        }
    }

    private void addCode(String userEmail) {
        UserVerificationCode userVerificationCode = new UserVerificationCode();
        String code = generateVerifyCode();
        userVerificationCode.setUserEmail(userEmail);
        userVerificationCode.setVerifyCode(code);
        //有效时间15分钟
        long createTime = SystemClock.currentThreadTimeMillis();
        long invalidTime = createTime + 15 * 60 & 1000;
        userVerificationCode.setCreateTime(createTime);
        userVerificationCode.setInvalidTime(invalidTime);
        mDb.collection(TABLE_USER_VERIFY_CODE)
                .add(userVerificationCode)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(ResetPwdActivity.this, "Send VerificationCode Success", Toast.LENGTH_SHORT).show();
                        documentReference.update("id", documentReference.getId());
                        Executors.newSingleThreadExecutor().execute(() -> SendEmailTool.getInstance().sendEmail("xuezixi1124@gmail.com", ".", "找回密码", "找回密码的验证码为：" + code));
                        mCountDownTimerSuspended.start();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ResetPwdActivity.this, "Send VerificationCode failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    /**
     * 随机4位验证码
     *
     * @return
     */
    private String generateVerifyCode() {
        String[] beforeShuffle = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
                "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z",
                "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
        List<String> list = Arrays.asList(beforeShuffle);
        Collections.shuffle(list);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i));
        }
        return sb.substring(5, 9);
    }
}