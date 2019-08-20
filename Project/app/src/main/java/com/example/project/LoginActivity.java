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
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.Prevalent.Prevalent;
import com.example.project.model.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class LoginActivity extends AppCompatActivity {

    private Button loginButton;
    private EditText Lusername, Lpass;
    private TextView forgetpass, adminLink, userLink;
    private ProgressDialog loadingbar;
    private String parentDbName = "Admins";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = findViewById(R.id.loginButton);
        Lusername = findViewById(R.id.usernameLogin);
        Lpass = findViewById(R.id.passLogin);
        adminLink = findViewById(R.id.adminLink);
        userLink = findViewById(R.id.userLink);
        forgetpass = findViewById(R.id.forgetLogin);
        loadingbar= new ProgressDialog(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginUser();
            }
        });
        adminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginButton.setText("Login as Admin");
                userLink.setVisibility(View.VISIBLE);
                adminLink.setVisibility(View.INVISIBLE);
                parentDbName = "Admins";
            }
        });
        userLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginButton.setText("Login as User");
                userLink.setVisibility(View.INVISIBLE);
                adminLink.setVisibility(View.VISIBLE);
                parentDbName="Users";
            }
        });

    }

    private void LoginUser() {
        String username = Lusername.getText().toString();
        String password = Lpass.getText().toString();

        if (TextUtils.isEmpty(username)){
            Toast.makeText(this, "Please enter the username", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please enter the password", Toast.LENGTH_SHORT).show();
        }
        else{
            loadingbar.setTitle("Login Account");
            loadingbar.setMessage("Please wait while we checking the credential");
            loadingbar.setCanceledOnTouchOutside(false);
            loadingbar.show();

            AllowAccessToAccount(username,password);
        }
    }

    private void AllowAccessToAccount(final String username, final String password) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(parentDbName).child(username).exists()){
                    Users usersData = dataSnapshot.child(parentDbName).child(username).getValue(Users.class);

                    if (usersData.getUsername().equals(username)){
                        if (usersData.getPassword().equals(password)){
                            if (parentDbName.equals("Users")){
                                Toast.makeText(LoginActivity.this, "Logged as User succesfully", Toast.LENGTH_SHORT).show();
                                loadingbar.dismiss();
                                Prevalent.currentOnlineUser = usersData;
                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                startActivity(intent);
                            }
                            else if (parentDbName.equals("Admins")){
                                Toast.makeText(LoginActivity.this, "Logged as Admin succesfully", Toast.LENGTH_SHORT).show();
                                loadingbar.dismiss();

                                Intent intent = new Intent(LoginActivity.this, AdminCategoryActivity.class);
                                startActivity(intent);
                            }
                        }
                        else {
                            loadingbar.dismiss();
                            Toast.makeText(LoginActivity.this, "Username or password is incorrect", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Account with this: "+username+" didn't exist", Toast.LENGTH_SHORT).show();
                    loadingbar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}