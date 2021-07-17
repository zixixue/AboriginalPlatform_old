package au.edu.unsw.infs3605.aboriginalplatform.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Transaction;

import java.util.Objects;

import au.edu.unsw.infs3605.aboriginalplatform.R;
import au.edu.unsw.infs3605.aboriginalplatform.entity.UserExtendData;
import au.edu.unsw.infs3605.aboriginalplatform.utils.RegUtil;

import static au.edu.unsw.infs3605.aboriginalplatform.Constants.TABLE_USER_EXTEND;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "RegisterActivity";

    private TextView tvUserNameDesc;
    private EditText etUserName;
    private TextView tvUserEmailDesc;
    private EditText etUserEmail;
    private TextView tvPwdDesc;
    private EditText etPwd;
    private TextView tvConfirmPwdDesc;
    private EditText etConfirmPwd;
    private TextView tvRoleDesc;
    private RadioGroup rgRole;
    private RadioButton rbOk;
    private RadioButton rbNot;
    private CheckBox cbAgreement;
    private TextView tvRegister;
    private TextView tvAgreement;

    private FirebaseAuth mAuth;
    private boolean isRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        tvUserNameDesc = findViewById(R.id.tvUserNameDesc);
        etUserName = findViewById(R.id.etUserName);
        tvUserEmailDesc = findViewById(R.id.tvUserEmailDesc);
        etUserEmail = findViewById(R.id.etUserEmail);
        tvPwdDesc = findViewById(R.id.tvPwdDesc);
        etPwd = findViewById(R.id.etPwd);
        tvConfirmPwdDesc = findViewById(R.id.tvConfirmPwdDesc);
        etConfirmPwd = findViewById(R.id.etConfirmPwd);
        tvRoleDesc = findViewById(R.id.tvRoleDesc);
        rgRole = findViewById(R.id.rgRole);
        rbOk = findViewById(R.id.rbOk);
        rbNot = findViewById(R.id.rbNot);
        cbAgreement = findViewById(R.id.cbAgreement);
        tvRegister = findViewById(R.id.tvRegister);
        tvAgreement = findViewById(R.id.tvAgreement);
        // Enable Firestore logging
        FirebaseFirestore.setLoggingEnabled(true);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        tvAgreement.setOnClickListener(this);
        tvRegister.setOnClickListener(this);
        rgRole.setOnCheckedChangeListener((group, checkedId) -> isRole = checkedId == R.id.rbOk);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvRegister:
                String userName = etUserName.getText().toString().trim();
                String userEmail = etUserEmail.getText().toString().trim();
                String userPwd = etPwd.getText().toString().trim();
                String userConfirmPwd = etConfirmPwd.getText().toString().trim();
                if (TextUtils.isEmpty(userName)) {
                    Toast.makeText(this, "USER NAME CANNOT BE EMPTY", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(userEmail)) {
                    Toast.makeText(this, "USER EMAIL CANNOT BE EMPTY", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!RegUtil.isEmail(userEmail)) {
                    Toast.makeText(this, "USER EMAIL IS NOT VALID", Toast.LENGTH_SHORT).show();
                    return;
                }
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
                if (!cbAgreement.isChecked()) {
                    Toast.makeText(this, "PLEASE AGREE TO THE USER AGREEMENT", Toast.LENGTH_SHORT).show();
                    return;
                }
                mAuth.createUserWithEmailAndPassword(userEmail, userPwd)
                        .addOnCompleteListener(this, task -> {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                FirebaseUser user = mAuth.getCurrentUser();
                                if (user != null) {
                                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                                    db.runTransaction((Transaction.Function<Void>) transaction -> {
                                        UserExtendData userExtendData = new UserExtendData();
                                        userExtendData.setUserId(user.getUid());
                                        userExtendData.setAusOriginal(isRole);
                                        db.collection(TABLE_USER_EXTEND)
                                                .add(userExtendData)
                                                .addOnSuccessListener(documentReference ->
                                                        transaction.update(documentReference, "id", documentReference.getId()));
                                        user.updateProfile(new UserProfileChangeRequest.Builder()
                                                .setDisplayName(userName)
                                                .build());
                                        return null;
                                    }).addOnSuccessListener(aVoid -> {
                                        Toast.makeText(RegisterActivity.this, "Register Success", Toast.LENGTH_SHORT).show();
                                        RegisterActivity.this.finish();
                                    }).addOnFailureListener(e -> Toast.makeText(RegisterActivity.this, "Register failed", Toast.LENGTH_SHORT).show());
                                }
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(RegisterActivity.this, "Register failed", Toast.LENGTH_SHORT).show();
                                Objects.requireNonNull(task.getException()).printStackTrace();
                            }
                        });
                break;
            case R.id.tvAgreement:
                startActivity(new Intent(this, UserAgreementActivity.class));
                break;
            default:
        }
    }

}