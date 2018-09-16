package com.example.harshit.medio;

/**
 * Created by Harshit on 30-03-2018.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StudentLoginActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextPassword;
    private Button buttonLogin;
    private ProgressBar progressBar;
    private DatabaseReference userRef;
    public static String sec,email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // set the view now
        setContentView(R.layout.student_login);

        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = editTextEmail.getText().toString();
                final String password = editTextPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                //authenticate user
                userRef= FirebaseDatabase.getInstance().getReference("users");
                userRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        sec="";
                        for (DataSnapshot section:dataSnapshot.getChildren()){
                            if(section.child(email).exists()){
                                sec=section.getKey();
                                break;
                            }
                        }
                        if (!sec.equals("")) {
                            startActivity(new Intent(StudentLoginActivity.this, StudentInterface.class));

                            String pass1 = dataSnapshot.child(sec).child(email).child("password").getValue().toString();
                            String name = dataSnapshot.child(sec).child(email).child("name").getValue().toString();
                            if (password.equals(pass1)) {
                                Toast.makeText(StudentLoginActivity.this, "Welcome " + name, Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                startActivity(new Intent(StudentLoginActivity.this, StudentInterface.class));
                            }
                            else{
                                Toast.makeText(StudentLoginActivity.this,"Check your email and password!",Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                        else{
                            Toast.makeText(StudentLoginActivity.this,"Check your email and password!",Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(StudentLoginActivity.this,"Servers are currently offline",Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }
}
