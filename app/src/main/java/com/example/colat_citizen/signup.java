package com.example.colat_citizen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class signup extends AppCompatActivity {

    private EditText UserEmail, UserPassword, UserConfirmPassword, userAge, userAddress, userName;
    private Button CreateAccountButton;
    private ProgressDialog loadingBar;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        UserEmail=(EditText) findViewById(R.id.signup_email);
        UserPassword=(EditText) findViewById(R.id.signup_password);
        UserConfirmPassword=(EditText) findViewById(R.id.signup_password2);
        userAddress=(EditText) findViewById(R.id.signup_address);
        userAge=(EditText) findViewById(R.id.signup_age);
        userName=(EditText) findViewById(R.id.signup_name);
        CreateAccountButton=(Button) findViewById(R.id.signup_button);
        loadingBar=new ProgressDialog(this);

        mAuth=FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        CreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateNewAccount();
            }
        });
        
        
    }

    private void CreateNewAccount()
    {
        final String email=UserEmail.getText().toString();
        String password=UserPassword.getText().toString();
        String confirmPassword=UserConfirmPassword.getText().toString();
        final String address=userAddress.getText().toString();
        final String age=userAge.getText().toString();
        final String name=userName.getText().toString();

        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(this,"Please Write Your Email",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this,"Please Write Your Password",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(confirmPassword))
        {
            Toast.makeText(this,"Please confirm your  password",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(address))
        {
            Toast.makeText(this,"Please enter your address",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(age))
        {
            Toast.makeText(this,"Please enter your age",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(name))
        {
            Toast.makeText(this,"Please enter your Name",Toast.LENGTH_SHORT).show();
        }
        else if(!password.equals(confirmPassword))
        {
            Toast.makeText(this,"Passwords do not match",Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setTitle("Creating Account...");
            loadingBar.setMessage("Please wait while creating your Account");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);

//            mAuth.getInstance().signInWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
//                @Override
//                public void onSuccess(AuthResult authResult) {
//                    Toast.makeText(signup.this,"You are Authenticated Successfully...",Toast.LENGTH_SHORT).show();
//                    FirebaseUser user = authResult.getUser();
//                    System.out.println(user.getUid());
//                }
//            });

            mAuth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                            if(task.isSuccessful())
                            {

                                FirebaseUser currentFirebaseUser = mAuth.getInstance().getCurrentUser() ;
                                System.out.println(currentFirebaseUser.getUid());

                                Toast.makeText(signup.this,"You are Authenticated Successfully...",Toast.LENGTH_SHORT).show();

                                Map<String, Object> user2 = new HashMap<>();
                                user2.put("name", name);
                                user2.put("address", address);
                                user2.put("age", age);
                                user2.put("email",email);



                                db.collection("users").document(currentFirebaseUser.getUid())
                                        .set(user2);
//                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                                            @Override
//                                            public void onSuccess(DocumentReference documentReference) {
//                                                System.out.println("Done");
//                                            }
//                                        })
//                                        .addOnFailureListener(new OnFailureListener() {
//                                            @Override
//                                            public void onFailure(@NonNull Exception e) {
//                                                Log.w("Error adding document", e);
//                                            }
//                                        });

                                loadingBar.dismiss();
                                SendUserToMainActivity();
                            }
                            else
                            {
                                String message=task.getException().getMessage();
                                Toast.makeText(signup.this,"Error Occured"+message,Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }
                        }
                    });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser=mAuth.getCurrentUser();
        if (currentUser!=null)
        {
            SendUserToMainActivity();
        }
    }

    private void SendUserToMainActivity()
    {
        Intent mainActivityIntent=new Intent(signup.this,MainActivity.class);
        mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainActivityIntent);
        finish();
    }
}
