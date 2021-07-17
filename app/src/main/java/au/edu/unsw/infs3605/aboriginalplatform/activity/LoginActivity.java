package au.edu.unsw.infs3605.aboriginalplatform.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import au.edu.unsw.infs3605.aboriginalplatform.Constants;
import au.edu.unsw.infs3605.aboriginalplatform.R;
import au.edu.unsw.infs3605.aboriginalplatform.entity.UserExtendData;
import au.edu.unsw.infs3605.aboriginalplatform.utils.RegUtil;
import au.edu.unsw.infs3605.aboriginalplatform.utils.SPUtil;

import static au.edu.unsw.infs3605.aboriginalplatform.Constants.TABLE_USER_EXTEND;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "LoginActivity";

    private ImageView ivLogo;
    private TextView tvTitle;
    private TextView tvUserEmailDesc;
    private EditText etUserEmail;
    private TextView tvPwdDesc;
    private EditText etPwd;
    private TextView tvForgetPwdDesc;
    private TextView tvLogin;
    private TextView tvNoUserDesc;
    private TextView tvRegister;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ivLogo = findViewById(R.id.ivLogo);
        tvTitle = findViewById(R.id.tvTitle);
        tvUserEmailDesc = findViewById(R.id.tvUserEmailDesc);
        etUserEmail = findViewById(R.id.etUserEmail);
        tvPwdDesc = findViewById(R.id.tvPwdDesc);
        etPwd = findViewById(R.id.etPwd);
        tvForgetPwdDesc = findViewById(R.id.tvForgetPwdDesc);
        tvLogin = findViewById(R.id.tvLogin);
        tvNoUserDesc = findViewById(R.id.tvNoUserDesc);
        tvRegister = findViewById(R.id.tvRegister);
        // Enable Firestore logging
        FirebaseFirestore.setLoggingEnabled(true);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        tvForgetPwdDesc.setOnClickListener(this);
        tvLogin.setOnClickListener(this);
        tvRegister.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            etUserEmail.setText(currentUser.getEmail());
            etPwd.setText("123456");
            SPUtil.getInstance().setParam(Constants.CACHE_USER, new Gson().toJson(currentUser));
            startActivity(new Intent(this, MainActivity.class));
        }
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
            case R.id.tvForgetPwdDesc:
                forgetPwd();
                break;
            case R.id.tvLogin:
                String userEmail = etUserEmail.getText().toString().trim();
                String userPwd = etPwd.getText().toString().trim();
                if (TextUtils.isEmpty(userEmail)) {
                    Toast.makeText(this, "user email is not empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!RegUtil.isEmail(userEmail)) {
                    Toast.makeText(this, "user email is not valid", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(userPwd)) {
                    Toast.makeText(this, "user password is not empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                Log.i(TAG, "onClick:userEmail== " + userEmail);
                mAuth.signInWithEmailAndPassword(userEmail, userPwd)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                                    // Sign in success, update UI with the signed-in user's information
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    if (user != null) {
                                        FirebaseFirestore.getInstance().collection(TABLE_USER_EXTEND)
                                                .whereEqualTo("id", user.getUid())
                                                .get()
                                                .addOnSuccessListener(command -> {
                                                    if (command.getDocuments().size() > 0) {
                                                        SPUtil.getInstance().setParam(Constants.CACHE_USER, command.getDocuments().get(0).toObject(UserExtendData.class));
                                                    }
                                                });
                                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                        LoginActivity.this.finish();
                                    }
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(LoginActivity.this, "Login failed.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                break;
            case R.id.tvRegister:
                startActivity(new Intent(this, RegisterActivity.class));
                etUserEmail.setText("");
                etPwd.setText("");
                break;
            default:
        }
    }

    private void forgetPwd() {
        String userEmail = etUserEmail.getText().toString().trim();
        if (TextUtils.isEmpty(userEmail)) {
            Toast.makeText(this, "user email is not empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!RegUtil.isEmail(userEmail)) {
            Toast.makeText(this, "user email is not valid", Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.sendPasswordResetEmail(userEmail);
    }
}