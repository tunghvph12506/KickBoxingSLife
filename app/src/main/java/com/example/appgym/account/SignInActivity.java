package com.example.appgym.account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.appgym.MainActivity;
import com.example.appgym.R;
import com.example.appgym.admin.AdminActivity;
import com.example.appgym.model.Account;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SignInActivity extends AppCompatActivity {
    TextInputLayout edUserName,edPassword;
    List<Account> list=new ArrayList<>();
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        setTitle(R.string.sign_in_title);
        edUserName=findViewById(R.id.ed_username_sign_in);
        edPassword=findViewById(R.id.ed_password_sign_in);
    }
    public void Sign_in(View view) {
        String username=edUserName.getEditText().getText().toString();
        String password=edPassword.getEditText().getText().toString();
        DatabaseReference mdata=database.getReference("Account");
        if(username.length()==0){
            edUserName.setError(getResources().getString(R.string.sign_in_error_nousername));
        }else if(password.length()==0){
            edPassword.setError(getResources().getString(R.string.sign_up_error_nopassword));
        }else if(username.length() <6){
            edUserName.setError(getResources().getString(R.string.sign_up_error_wrongusername));
        }else if(password.length() <6){
            edPassword.setError(getResources().getString(R.string.sign_up_error_wrongpassword));
        }else {
            mdata.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot data:snapshot.getChildren() ){
                        Account account=data.getValue(Account.class);
                        list.add(account);
                    }
                    for (int i = 0; i <list.size() ; i++) {
                        if(username.equals(list.get(i).getUsername()) && password.equals(list.get(i).getPassword())) {
                            if(username.equals("admin999")&&password.equals("admin999")){
                                Toast.makeText(SignInActivity.this, R.string.sign_in_toast_welcomeadmin, Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(SignInActivity.this, AdminActivity.class);
                                startActivity(intent);
                                break;
                            }else {
                                ;      Log.d("TAG", "onDataChange: "+list);
                                Toast.makeText(SignInActivity.this, R.string.sign_in_toast_success, Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(SignInActivity.this, MainActivity.class);
                                Bundle b=new Bundle();
                                b.putString("username",list.get(i).getUsername());
                                intent.putExtras(b);
                                startActivity(intent);
                                break;
                            }
                        }else {
                            edPassword.setError(getResources().getString(R.string.sign_in_error_notcorrectpassword));
                            edUserName.setError(getResources().getString(R.string.sign_in_error_notcorrectusername));
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(SignInActivity.this, R.string.sign_in_toast_failed, Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    public void ForgetPassword(View view) {
        Intent i=new Intent(SignInActivity.this,ForgetPasswordActivity.class);
        startActivity(i);
    }

    public void clickSignUp(View view) {
        Intent i=new Intent(SignInActivity.this,SignUpActivity.class);
        startActivity(i);
    }
}