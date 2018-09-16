package com.example.harshit.medio;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;
import android.util.Patterns;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    public static String email;
    private EditText editTextEmail,editTextPassword;
    private TextView textViewStudentLogin;
    private Button buttonLogin;
    private ProgressBar progressBar;
    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set the view now
        setContentView(R.layout.activity_main);

        editTextEmail= (EditText) findViewById(R.id.editTextEmail);
        editTextPassword= (EditText) findViewById(R.id.editTextPassword);
        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        textViewStudentLogin =(TextView) findViewById(R.id.textViewStudentLogin);

        textViewStudentLogin.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, StudentLoginActivity.class));
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(MainActivity.this, TeacherInterface.class));
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
                userRef=FirebaseDatabase.getInstance().getReference("users").child("teachers").child(email);
                userRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {

                            String pass1 = dataSnapshot.child("password").getValue().toString();
                            String name = dataSnapshot.child("name").getValue().toString();
                            if (password.equals(pass1)) {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(MainActivity.this, "Welcome " + name, Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(MainActivity.this, TeacherInterface.class));
                            }
                            else{
                                Toast.makeText(MainActivity.this,"Check your email and password!",Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                        else{
                            Toast.makeText(MainActivity.this,"Check your email and password!",Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(MainActivity.this,"Servers are currently offline",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}