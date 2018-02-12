package com.hsp.realtimechatapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class RegisterActivity extends AppCompatActivity {

    public static final String CHAT_PREFS = "ChatPrefs";
    public static final String DISPLAY_NAME_KEY = "username";

    private AutoCompleteTextView emailView;
    private AutoCompleteTextView usernameView;
    private EditText passwordView;
    private EditText confirmPwdView;

    private FirebaseAuth firebaseAuthentication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailView = (AutoCompleteTextView) findViewById(R.id.register_email);
        passwordView = (EditText) findViewById(R.id.register_password);
        confirmPwdView = (EditText) findViewById(R.id.register_confirm_password);
        usernameView = (AutoCompleteTextView) findViewById(R.id.register_username);

        // Keyboard sign in action
        confirmPwdView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.register_form_finished || id == EditorInfo.IME_NULL) {
                    attemptRegistration();
                    return true;
                }
                return false;
            }
        });

        firebaseAuthentication= FirebaseAuth.getInstance();

    }

    // Executed when Sign Up button is pressed.
    public void signUp(View v) {
        attemptRegistration();
    }

    private void attemptRegistration() {

        // Reset errors displayed in the form.
        emailView.setError(null);
        passwordView.setError(null);

        // Store values at the time of the login attempt.
        String email = emailView.getText().toString();
        String password = passwordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) == false && isPasswordValid(password) == false) {
            passwordView.setError(getString(R.string.error_invalid_password));
            focusView = passwordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            emailView.setError(getString(R.string.error_field_required));
            focusView = emailView;
            cancel = true;
        } else if (isEmailValid(email) == false) {
            emailView.setError(getString(R.string.error_invalid_email));
            focusView = emailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first form field with an error.
            focusView.requestFocus();
        } else {
            createFirebaseUser();
        }
    }

    private boolean isEmailValid(String email) {
        // ADD MORE CHECKING LOGIC HERE!!!
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        String confirmPassword= confirmPwdView.getText().toString();
        return password.equals(confirmPassword) && password.length() > 6;
    }

    private void createFirebaseUser() {
        String email = emailView.getText().toString();
        String password= passwordView.getText().toString();
        //use the Task variable to check if creating the user was successful
        Task<AuthResult> createdUser = firebaseAuthentication.createUserWithEmailAndPassword(email, password);
        createdUser.addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d("Chat", "created user onComplete: "+ task.isComplete());
                if(task.isSuccessful() == false) {
                    Log.d("Chat", "user creation failed!!");
                    ActivityUtil.showErrorDialog("Registration attempt failed!", RegisterActivity.this);
                } else {
                    //registered user and saved the display name
                    saveDisplayName();
                    //go back to login activity
                    Intent intent= new Intent(RegisterActivity.this, LoginActivity.class);
                    finish();
                    startActivity(intent);
                }
            }
        });
    }

    private void saveDisplayName() {
        String displayName = usernameView.getText().toString();
        SharedPreferences preferences = getSharedPreferences(CHAT_PREFS, 0);
        preferences.edit().putString(DISPLAY_NAME_KEY, displayName).apply();
    }
}
