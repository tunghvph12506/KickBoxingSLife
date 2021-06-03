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
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ForgetPasswordActivity extends AppCompatActivity {
    TextInputLayout ed_username,ed_reply;
    List<Account> list=new ArrayList<>();
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        ed_reply = findViewById(R.id.ed_reply_for_get_pas);
        ed_username = findViewById(R.id.ed_username_for_get_pas);
        resetError();
    }


    public void ForgetPassword(View view) {
        resetError();
        String username = ed_username.getEditText().getText().toString();
        String reply = ed_reply.getEditText().getText().toString();
        if(username.length()==0){
            ed_username.setError(getResources().getString(R.string.forget_password_error_require));
        }else if(username.length()<6){
            ed_username.setError(getResources().getString(R.string.forget_password_error_wrongusername));
        }else if(reply.length() ==0){
            ed_reply.setError(getResources().getString(R.string.forget_password_error_require));
        }
        DatabaseReference mdata = database.getReference("Account");
        mdata.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data:snapshot.getChildren() ){
                    Account account=data.getValue(Account.class);
                    list.add(account);
                }
                for (int i=0;i<list.size();i++)
                {
                    if(username.equals(list.get(i).getUsername())){
                        {
                            ed_username.setError("");
                            if(reply.equals(list.get(i).getQuestion()))
                            {
                                Log.d("TAG", "onDataChange: "+list.get(i).getQuestion());
                                Intent intent =new Intent(ForgetPasswordActivity.this,ChangePasswordActivity.class);
                                Bundle b=new Bundle();
                                b.putString("tentk",list.get(i).getUsername());
                                intent.putExtras(b);
                                startActivity(intent);
                                break;
                            }
                            else
                            {
                                ed_reply.setError(getResources().getString(R.string.forget_password_error_notcorrectreply));
                                break;
                            }
                        }
                    }
                    else {
                        ed_username.setError(getResources().getString(R.string.forget_password_error_notcorrectusername));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ForgetPasswordActivity.this, R.string.forget_password_toast_failed, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void resetError()
    {
        ed_username.setError("");
        ed_reply.setError("");
    }
}
