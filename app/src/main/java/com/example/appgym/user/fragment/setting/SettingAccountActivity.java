package com.example.appgym.user.fragment.setting;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appgym.R;
import com.example.appgym.UserActivity;
import com.example.appgym.account.ChangePasswordActivity;
import com.example.appgym.account.SignInActivity;
import com.example.appgym.model.Information;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingAccountActivity extends AppCompatActivity {
    RadioGroup group;
    RadioButton radioButton1,radioButton2;
    String sex,imageUrl;
    TextView tv_username;
    boolean checkrb = false;
    TextInputLayout edName,edPhone,edHeight,edWeight,edAddress,edAge;
    DatabaseReference databaseReferenceAccount, databaseReferenceCustomer,nowRef;
    StorageReference storageReferenceImage;
    FirebaseStorage firebaseStorage;
    UploadTask uploadTask;
    ActionBar toolbar;
    CircleImageView imageView;
    Uri imageUri;
    Bitmap bitmap;
    Information information;

    static final int PICK_IMAGE = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_account);

        toolbar = getSupportActionBar();
        toolbar.setTitle(R.string.settingaccount_title);
        toolbar.setDisplayHomeAsUpEnabled(true);

        firebaseStorage = FirebaseStorage.getInstance();
        information = new Information();

        tv_username=findViewById(R.id.tv_username_settingaccount);
        databaseReferenceAccount = FirebaseDatabase.getInstance().getReference("Account");
        databaseReferenceCustomer = FirebaseDatabase.getInstance().getReference("Customer_information");
        storageReferenceImage = FirebaseStorage.getInstance().getReference("Images");
        Intent i=getIntent();
        Bundle b=i.getExtras();
        tv_username.setText(b.getString("tentk"));
        group=findViewById(R.id.RadioGroup1);
        radioButton1 = findViewById(R.id.rb_nam);
        radioButton2 = findViewById(R.id.rb_nu);
        edAge=findViewById(R.id.ed_age_settingaccount);
        edName=findViewById(R.id.ed_name_settingaccount);
        edPhone=findViewById(R.id.ed_phone_settingaccount);
        edHeight=findViewById(R.id.ed_height_settingaccount);
        edWeight=findViewById(R.id.ed_weight_settingaccount);
        edAddress=findViewById(R.id.ed_address_settingaccount);
        imageView = findViewById(R.id.circleImageView);

        nowRef = databaseReferenceCustomer.child(UserActivity.usernameAll);
        nowRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Information information = snapshot.getValue(Information.class);
                edName.getEditText().setText(information.getName());
                edAge.getEditText().setText(information.getAge());
                edPhone.getEditText().setText(information.getPhone());
                edWeight.getEditText().setText(information.getWeight());
                edHeight.getEditText().setText(information.getHeight());
                edAddress.getEditText().setText(information.getAddress());
                imageUrl = information.getImageUrl();
                if(imageUrl != null)
                {
                    Picasso.get().load(imageUrl).into(imageView);
                }
                if(information.getGender().equals("Nam"))
                {
                    radioButton1.setChecked(true);
                }
                else
                {
                    radioButton2.setChecked(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void onClickSave(View view) {
        resetError();
        getGender();
        String name=edName.getEditText().getText().toString();
        String phone=edPhone.getEditText().getText().toString();
        String age=edAge.getEditText().getText().toString();
        String height=edHeight.getEditText().getText().toString();
        String weight=edWeight.getEditText().getText().toString();
        String address=edAddress.getEditText().getText().toString();
        if(name.length() == 0) {
            edName.setError(getResources().getString(R.string.settingaccount_error_noname));
        }else if(age.length() == 0) {
            edAge.setError(getResources().getString(R.string.settingaccount_error_noage));
        }else if(phone.equals("")){
            edPhone.setError(getResources().getString(R.string.settingaccount_error_nophone));
        }else if(phone.length()<10||phone.length()>11){
            edPhone.setError(getResources().getString(R.string.sign_up_error_wrongphone));
        }else if(weight.length() == 0) {
            edWeight.setError(getResources().getString(R.string.settingaccount_error_noweight));
        }else if(height.length() == 0) {
            edHeight.setError(getResources().getString(R.string.settingaccount_error_noheight));
        }
        else if(address.length() == 0) {
            edAddress.setError(getResources().getString(R.string.settingaccount_error_noaddress));
        }
        else if(checkrb == false)
        {
            Toast.makeText(SettingAccountActivity.this,R.string.settingaccount_error_nogender,Toast.LENGTH_SHORT).show();
        }
        else if(imageUri == null && imageUrl == null)
        {
            Toast.makeText(SettingAccountActivity.this,R.string.settingaccount_error_noavatar,Toast.LENGTH_SHORT).show();
        }
        else {
            DatabaseReference accountRef = databaseReferenceAccount.child(UserActivity.usernameAll);
            accountRef.child("phone").setValue(phone);

            DatabaseReference customerRef = databaseReferenceCustomer.child(UserActivity.usernameAll);
            information.setName(name);
            information.setPhone(phone);
            information.setGender(sex);
            information.setWeight(weight);
            information.setHeight(height);
            information.setAddress(address);
            information.setAge(age);
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    customerRef.setValue(information);
                    Toast.makeText(SettingAccountActivity.this, R.string.settingaccount_toast_success, Toast.LENGTH_SHORT).show();
                }
            }, 3 * 1000);

        }
    }

    private void getGender() {
        if(!radioButton1.isChecked() && !radioButton2.isChecked())
        {
            checkrb = false;
        }
        else if(radioButton1.isChecked())
        {
            sex = radioButton1.getText().toString();
            checkrb = true;
        }
        else if(radioButton2.isChecked())
        {
            sex = radioButton2.getText().toString();
            checkrb = true;
        }
    }

    public void ChooseImageSetting(View view)
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE || requestCode == Activity.RESULT_OK)
        {
            try {
                if(data != null)
                {
                    imageUri = data.getData();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(getContentResolver(), imageUri));
                    }else {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                    }
                    Picasso.get().load(imageUri).into(imageView);
                    changeImage();

                }
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    private void changeImage()
    {
        StorageReference refImg = storageReferenceImage.child(UserActivity.usernameAll);
        uploadTask = refImg.putFile(imageUri);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                refImg.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String urlimage = uri.toString();
                        information.setImageUrl(urlimage);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        e.getMessage();
                        Toast.makeText(SettingAccountActivity.this, R.string.settingaccount_toast_imageurl_failed, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                e.getMessage();
                Toast.makeText(SettingAccountActivity.this, R.string.settingaccount_toast_uploadimage_failed, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void resetError()
    {
        edName.setError("");
        edPhone.setError("");
        edAge.setError("");
        edHeight.setError("");
        edWeight.setError("");
        edAddress.setError("");
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