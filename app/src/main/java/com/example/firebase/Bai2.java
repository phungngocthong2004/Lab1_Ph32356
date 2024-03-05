package com.example.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.play.integrity.internal.c;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class Bai2 extends AppCompatActivity {
    EditText edphone,edopt;

    Button btnphone,btnotp;
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String mVericationId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bai2);
    edopt=findViewById(R.id.ed_otp);
    edphone=findViewById(R.id.ed_phone);
    btnphone=findViewById(R.id.btnotp);
    btnotp=findViewById(R.id.btnb2Login);
    mAuth=FirebaseAuth.getInstance();

    mCallbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            edopt.setText(phoneAuthCredential.getSmsCode());
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {

        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
       mVericationId=s;
        }
    };

    btnphone.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String Phone=edphone.getText().toString();
            getOTP(Phone);
        }
    });
btnotp.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        String otp=edopt.getText().toString();
        vertiOPT(otp);
    }
});
    }
    private  void getOTP(String phone){
        PhoneAuthOptions options=PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber("+84"+phone)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(mCallbacks)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
    private  void vertiOPT(String Code){
        PhoneAuthCredential credential=PhoneAuthProvider.getCredential(mVericationId,Code);
        signWithPhoneAyth(credential);
    }
    private  void signWithPhoneAyth(PhoneAuthCredential credential){
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(Bai2.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(getApplicationContext(), "Đăng Nhập Thành công:"+user.getEmail(), Toast.LENGTH_LONG).show();
                            startActivity(new Intent(Bai2.this, Home.class));
                        } else {
                            Log.w("Main", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(Bai2.this, "Đăng Nhập Thất Bại", Toast.LENGTH_SHORT).show();
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException){

                            }
                        }
                    }
                });

    }
}