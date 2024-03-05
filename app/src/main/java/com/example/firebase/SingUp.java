package com.example.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SingUp extends AppCompatActivity {
   TextInputLayout edusername,edpass,edhoten,eddienthoai,ednhaplai;
   Button btnSingup;
    private FirebaseAuth mAuth;
    TextView tvSignIn;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){

        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);

        edusername=findViewById(R.id.txt_signUp_tenDN);
        edpass=findViewById(R.id.txt_signUp_matKhau);
        btnSingup=findViewById(R.id.btn_SignUp);
        edhoten=findViewById(R.id.txt_signUp_HoTen);
        eddienthoai=findViewById(R.id.txt_signUp_DienThoai);
        ednhaplai=findViewById(R.id.txt_signUp_NhapLai);
        mAuth = FirebaseAuth.getInstance();
        tvSignIn=findViewById(R.id.tv_SignIn);
        btnSingup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edusername.getEditText().getText().toString();
                String password = edpass.getEditText().getText().toString();
                String phone = eddienthoai.getEditText().getText().toString();
                String hoten=edhoten.getEditText().getText().toString().trim();
                String nhaplai=ednhaplai.getEditText().getText().toString().trim();
                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)||TextUtils.isEmpty(hoten) || TextUtils.isEmpty(phone)||TextUtils.isEmpty(nhaplai)) {
                    Toast.makeText(getApplicationContext(), "Không được bỏ trống", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!isValidEmail(email)) {
                    Toast.makeText(getApplicationContext(), "Email không hợp lệ", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!password.equals(nhaplai)) {
                    Toast.makeText(getApplicationContext(), "Mật khẩu không trùng", Toast.LENGTH_SHORT).show();
                        return;
                }

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SingUp.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Toast.makeText(getApplicationContext(), "Đăng ký Thành Công " + user.getEmail(), Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(SingUp.this,MainActivity.class));
                                } else {
                                    Log.w("SignUp", "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(SingUp.this, "Đăng Ký thất Bại.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                tvSignIn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(SingUp.this,MainActivity.class));
                        finish();
                        Toast.makeText(SingUp.this, "sjbdsfdsgdf", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }
    private boolean isValidEmail(String email) {
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }
}