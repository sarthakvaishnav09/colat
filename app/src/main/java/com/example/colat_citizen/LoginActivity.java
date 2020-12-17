package com.example.colat_citizen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.colat_citizen.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private TextView signupText1;
    private EditText UserEmail,UserPassword;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;
    private Button LoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signupText1=(TextView)findViewById(R.id.signText);
        UserEmail =(EditText) findViewById(R.id.login_email);
        UserPassword=(EditText) findViewById(R.id.login_password);
        LoginButton= findViewById(R.id.login_Button);
        loadingBar=new ProgressDialog(this);
        mAuth=FirebaseAuth.getInstance();


        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AllowingUserToLogin();
            }
        });

//        signupText1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v)
//            {
//                //SendUserToSignupPage();
//                System.out.println("Login->signup");
//            }
//        });


    }

    private void AllowingUserToLogin()
    {
        String email=UserEmail.getText().toString();
        String password=UserPassword.getText().toString();

        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(LoginActivity.this,"Write Your Email",Toast.LENGTH_SHORT).show();

        }
        else if(TextUtils.isEmpty(password))
        {
            Toast.makeText(LoginActivity.this,"Write Your Password",Toast.LENGTH_SHORT).show();
        }
        else
        {

            loadingBar.setTitle("Logging in...");
            loadingBar.setMessage("Please wait while Logging in");
            loadingBar.setCanceledOnTouchOutside(true);
            loadingBar.show();


            mAuth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                            if(task.isSuccessful())
                            {
                                SendUserToMainActivity();
                                Toast.makeText(LoginActivity.this,"You are logged in successfully",Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                            }
                            else
                            {
                                String message=task.getException().getMessage();
                                Toast.makeText(LoginActivity.this,"Error Occured"+message,Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }
                        }
                    });
        }
    }

    private void SendUserToMainActivity()
    {
        Intent mainActivityIntent=new Intent(LoginActivity.this,MainActivity.class);
        mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainActivityIntent);
        finish();
    }


    public void SendUserToSignupPage(View view)
    {
        Intent registerIntent=new Intent(this,signup.class);
        startActivity(registerIntent);
    }


}
