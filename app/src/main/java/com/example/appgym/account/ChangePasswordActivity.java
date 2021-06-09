package com.example.appgym.account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appgym.R;
import com.example.appgym.model.Account;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChangePasswordActivity extends AppCompatActivity {
    TextInputLayout password, passwordcheck;
    String tentk1="";
    DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("Account");
    TextView tv_tk;
    List<Account> list = new ArrayList<>();
    ActionBar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        toolbar = getSupportActionBar();
        toolbar.setTitle(R.string.change_password_title);
        toolbar.setDisplayHomeAsUpEnabled(true);

        password=findViewById(R.id.ed_change_password);
        passwordcheck=findViewById(R.id.ed_change_password_check);
        Intent i=getIntent();
        Bundle b=i.getExtras();
        tentk1=b.getString("tentk");
        tv_tk=findViewById(R.id.tv_username);
        tv_tk.setText(tentk1);
        resetError();
    }
    private void GetData() {
        resetError();
        String password12=password.getEditText().getText().toString();
        String passlai12=passwordcheck.getEditText().getText().toString();
        if(password12.length()==0){
            password.setError(getResources().getString(R.string.change_password_error_nopassword));
        }else if(password12.length()<6){
            password.setError(getResources().getString(R.string.change_password_error_wrongpassword));
        }else if(passlai12.length()==0){
            passwordcheck.setError(getResources().getString(R.string.change_password_error_nopassword));
        }
        else if(!password12.equals(passlai12)){
            passwordcheck.setError(getResources().getString(R.string.change_password_error_notcorrectpassword));
        }
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {

                    Account account = new Account(data.child("auth").getValue().toString(),
                            data.child("username").getValue().toString(),
                            data.child("password").getValue().toString(),
                            data.child("phone").getValue().toString(),
                            data.child("question").getValue().toString());
                    list.add(account);
                }
                for (int i = 0; i < list.size(); i++) {

                    if (list.get(i).getUsername().equals(tentk1)) {
                        String passlai12=passwordcheck.getEditText().getText().toString();

                        mRef.child(tentk1).child("password").setValue(passlai12);
                        String auth = tentk1 + "-" + passlai12;
                        mRef.child(tentk1).child("auth").setValue(auth);
                        Toast.makeText(ChangePasswordActivity.this,R.string.change_password_toast_success, Toast.LENGTH_SHORT).show();
                        Intent in=new Intent(ChangePasswordActivity.this,SignInActivity.class);
                        startActivity(in);
                        finish();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void SavePassword(View view) {
        GetData();
    }

    private void resetError()
    {
        password.setError("");
        passwordcheck.setError("");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                return true;

            default: return super.onOptionsItemSelected(item);
        }
    }
}