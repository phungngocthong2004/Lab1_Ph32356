package com.example.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Forgot extends AppCompatActivity {
EditText ed_emailFo;
Button btnfo;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        ed_emailFo=findViewById(R.id.ed_emailfo);
        btnfo=findViewById(R.id.btnfogot);
        mAuth = FirebaseAuth.getInstance();
        btnfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=ed_emailFo.getText().toString();
                mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(Forgot.this, " Vui Lòng Kiểm tra hộp thư  để cập nhật mật khâu", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(Forgot.this, "Lỗi Gửi mail", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

    }
}