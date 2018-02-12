package com.hsp.realtimechatapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private AutoCompleteTextView emailView;
    private EditText passwordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailView = (AutoCompleteTextView) findViewById(R.id.login_email);
        passwordView = (EditText) findViewById(R.id.login_password);

        //attempt login when enter is pressed on the passwordTextView
        passwordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        firebaseAuth= FirebaseAuth.getInstance();

    }

    //executed when Sign in button pressed
    public void signInExistingUser(View v)   {
        attemptLogin();
    }

    //executed when Register button pressed
    public void registerNewUser(View v) {
        Intent intent = new Intent(this, com.hsp.realtimechatapplication.RegisterActivity.class);
        finish();
        startActivity(intent);
    }

    private void attemptLogin() {
        String email = emailView.getText().toString();
        String password= passwordView.getText().toString();

        if(checkIfEmpty(email, password)) {
            return;
        }

        Toast.makeText(this, "Logging in...", Toast.LENGTH_SHORT).show();

        //add a listener to receive a message from Firebase when the user has signed in
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d("Chat", "signInWithEmail() onComplete: " + task.isSuccessful());
                if(task.isSuccessful() == false) {
                    Log.d("Chat", "There was a problem during signing in!!" + task.getException());
                    ActivityUtil.showErrorDialog("There was a problem during signing in!", LoginActivity.this);
                } else {
                    Intent intent= new Intent(LoginActivity.this, MainChatActivity.class);
                    finish();
                    startActivity(intent);
                }
            }
        });
    }

    private boolean checkIfEmpty(String email, String password) {
        return email.isEmpty() && password.isEmpty();
    }
}