package com.example.appgym.account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.appgym.R;
import com.example.appgym.model.Account;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignUpActivity extends AppCompatActivity {
    TextInputLayout edUserName, edPassword, edPasswordCheck, edPhone, edQuestion;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setTitle(R.string.sign_up_title);
        edUserName = findViewById(R.id.ed_username);
        edPassword = findViewById(R.id.ed_password);
        edPasswordCheck = findViewById(R.id.ed_password_check);
        edPhone = findViewById(R.id.ed_phone);
        edQuestion = findViewById(R.id.ed_question);
        database = FirebaseDatabase.getInstance();
        resetError();
    }

    public void Sign_up(View view) {
        resetError();
        String password = edPassword.getEditText().getText().toString();
        String password1 = edPasswordCheck.getEditText().getText().toString();
        String username = edUserName.getEditText().getText().toString();
        String phone = edPhone.getEditText().getText().toString();
        String question = edQuestion.getEditText().getText().toString();
        if (username.length() == 0) {
            edUserName.setError(getResources().getString(R.string.sign_up_error_nousername));
        } else if (username.length() < 6) {
            edUserName.setError(getResources().getString(R.string.sign_up_error_wrongusername));
        } else if (password.length() == 0) {
            edPassword.setError(getResources().getString(R.string.sign_up_error_nopassword));
        } else if (password.length() < 6) {
            edPassword.setError(getResources().getString(R.string.sign_up_error_nopassword));
        } else if (!password.equals(password1)) {
            edPasswordCheck.setError(getResources().getString(R.string.sign_up_error_wrongpassword));
        } else if (question.length() == 0) {
            edQuestion.setError(getResources().getString(R.string.sign_up_error_noquestion));
        } else if (phone.equals("")) {
            edPhone.setError(getResources().getString(R.string.sign_up_error_nophone));
        } else if (phone.length() < 10 || phone.length() > 11) {
            edPhone.setError(getResources().getString(R.string.sign_up_error_wrongphone));
        } else {
            String key = username + "-" + password;
            DatabaseReference myRef = database.getReference().child("Account").child(key);
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Account account = new Account(username, password, phone, question);
                    myRef.setValue(account).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(SignUpActivity.this, R.string.sign_up_toast_success, Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(SignUpActivity.this, SignInActivity.class);
                            startActivity(i);
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SignUpActivity.this, R.string.sign_up_toast_failed, Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }

    }

    private void resetError()
    {
        edUserName.setError("");
        edPassword.setError("");
        edPasswordCheck.setError("");
        edPhone.setError("");
        edQuestion.setError("");
    }
}