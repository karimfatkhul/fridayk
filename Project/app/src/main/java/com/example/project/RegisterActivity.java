package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity
{

    private Button registerButton;
    private EditText Rusername, Rpass, Rphone;
    private ProgressDialog loadingbar;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerButton = findViewById(R.id.registerButton);
        Rusername = findViewById(R.id.usernameRegister);
        Rpass = findViewById(R.id.passRegister);
        Rphone = findViewById(R.id.phoneRegister);
        loadingbar = new ProgressDialog(this);

        registerButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                CreateAccount();
            }
        });

    }

    private void CreateAccount()
    {
        String username = Rusername.getText().toString();
        String password = Rpass.getText().toString();
        String phone = Rphone.getText().toString();

        if (TextUtils.isEmpty(username))
        {
            Toast.makeText(this, "Please enter your username", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show();
        }else if (password.length()<6)
        {
            Toast.makeText(this, "Password should be more than 6", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(phone))
        {
            Toast.makeText(this, "Please enter your phone number", Toast.LENGTH_SHORT).show();
        }else
            {
                loadingbar.setTitle("Create Account");
                loadingbar.setMessage("Please wait while we checking the credential");
                loadingbar.setCanceledOnTouchOutside(false);
                loadingbar.show();

                ValidateUsername(username, phone, password);
            }
    }

    private void ValidateUsername(final String username, final String phone, final String password)
    {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!(dataSnapshot.child("Users").child(username).exists())){
                    HashMap<String,Object> userdataMap=new HashMap<>();
                    userdataMap.put("username",username);
                    userdataMap.put("password",password);
                    userdataMap.put("phone",phone);

                    RootRef.child("Users").child(username).updateChildren(userdataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(RegisterActivity.this, "Your account have been created", Toast.LENGTH_SHORT).show();
                                loadingbar.dismiss();

                                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                                startActivity(intent);
                            } else {
                                loadingbar.dismiss();
                                Toast.makeText(RegisterActivity.this, "Network Error: Please Try again !", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(RegisterActivity.this, "This "+username+" is already exist !", Toast.LENGTH_SHORT).show();
                    loadingbar.dismiss();
                    Toast.makeText(RegisterActivity.this, "Please Try another username", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}