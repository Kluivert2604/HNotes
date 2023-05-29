package com.kieudatquochung.hnotes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    EditText mLoginEmail, mLoginPassword;
    Button mLoginBtn;
    TextView mGoToForgotPassword, mGoToSignUp;
    ProgressBar mProgressBarOfMainActivity;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLoginEmail = findViewById(R.id.loginEmail);
        mLoginPassword = findViewById(R.id.loginPassword);
        mLoginBtn = findViewById(R.id.LoginBtn);
        mGoToForgotPassword = findViewById(R.id.goToForgotPassword);
        mGoToSignUp = findViewById(R.id.goToSignUp);
        mProgressBarOfMainActivity = findViewById(R.id.progressBarOfMainActivity);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null)
        {
            finish();
            startActivity(new Intent(MainActivity.this, NotesActivity.class));
        }

        mGoToSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SignUpActivity.class));
            }
        });

        mGoToForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ForgotPasswordActivity.class));
            }
        });
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = mLoginEmail.getText().toString().trim();
                String password = mLoginPassword.getText().toString().trim();

                if (mail.isEmpty() || password.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "All Fields Are Required", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //Login the user
                    mProgressBarOfMainActivity.setVisibility(View.VISIBLE);

                    firebaseAuth.signInWithEmailAndPassword(mail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful())
                            {
                                checkmailverfication();
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), "Account Doesn't Exist", Toast.LENGTH_SHORT).show();
                                mProgressBarOfMainActivity.setVisibility(View.INVISIBLE);
                            }
                        }
                    });
                }
            }
        });
    }
    private void checkmailverfication()
    {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser.isEmailVerified() == true)
        {
            Toast.makeText(getApplicationContext(), "Logged In", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this, NotesActivity.class));
        }
        else
        {
            mProgressBarOfMainActivity.setVisibility(View.INVISIBLE);
            Toast.makeText(getApplicationContext(), "Verify your mail first", Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }
    }

}