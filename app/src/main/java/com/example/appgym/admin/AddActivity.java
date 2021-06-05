package com.example.appgym.admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.appgym.R;
import com.example.appgym.model.Exercise;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class AddActivity extends AppCompatActivity {
    static final int PICK_VIDEO = 1;
    static final int PICK_IMAGE = 2;
    VideoView videoView;
    Button btnUpload;
    ProgressBar progressBar;
    EditText edtName,edtCalo;
    Uri videoUri,imageUri;
    MediaController mediaController;
    StorageReference storageReferenceVideo,storageReferenceImage;
    DatabaseReference databaseReference, databaseReferenceCheck;
    Exercise exercise;
    UploadTask uploadTaskVideo,uploadTaskImage;
    Button btnChoose;
    ActionBar toolbar;
    Spinner spinnerChallenge,spinnerDay;
    ImageView imageView;
    List<String> listgroup,listday;
    Bitmap bitmap;
    String group,day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        exercise = new Exercise();
        storageReferenceVideo = FirebaseStorage.getInstance().getReference("Videos");
        storageReferenceImage = FirebaseStorage.getInstance().getReference("Images");
        btnUpload = findViewById(R.id.button_upload_add);
        progressBar = findViewById(R.id.progressBar_add);
        edtName = findViewById(R.id.edt_videoname_add);
        edtCalo = findViewById(R.id.edt_calo_add);
        btnChoose = findViewById(R.id.btn_chooseVideo_add);
        mediaController = new MediaController(this);
        spinnerChallenge = findViewById(R.id.spinner_challenge_add);
        spinnerDay = findViewById(R.id.spinner_day_add);
        imageView = findViewById(R.id.imgview_add);
        group = "Chest";
        day = "Day1";
        videoView = findViewById(R.id.videoview_add);
        videoView.start();

        toolbar = getSupportActionBar();
        toolbar.setTitle(R.string.add_title);
        toolbar.setDisplayHomeAsUpEnabled(true);

        listgroup = new ArrayList<>();
        listgroup.add(getResources().getString(R.string.add_spinner_chest));
        listgroup.add(getResources().getString(R.string.add_spinner_stomach));
        listgroup.add(getResources().getString(R.string.add_spinner_hand));
        listgroup.add(getResources().getString(R.string.add_spinner_leg));

        listday = new ArrayList<>();
        listday.add(getResources().getString(R.string.add_spinner_day1));
        listday.add(getResources().getString(R.string.add_spinner_day2));
        listday.add(getResources().getString(R.string.add_spinner_day3));
        listday.add(getResources().getString(R.string.add_spinner_day4));
        listday.add(getResources().getString(R.string.add_spinner_day5));
        listday.add(getResources().getString(R.string.add_spinner_day6));
        listday.add(getResources().getString(R.string.add_spinner_day7));
        ArrayAdapter spinnerChallengeAdapter = new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,listgroup);
        ArrayAdapter spinnerDayAdapter = new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,listday);
        spinnerChallenge.setAdapter(spinnerChallengeAdapter);
        spinnerDay.setAdapter(spinnerDayAdapter);

        spinnerChallenge.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (spinnerChallenge.getSelectedItemPosition())
                {
                    case 0: group = "Chest";
                        break;
                    case 1: group = "Stomach";
                        break;
                    case 2: group = "Hand";
                        break;
                    case 3: group = "Leg";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        spinnerDay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (spinnerDay.getSelectedItemPosition())
                {
                    case 0: day = "Day1";
                        break;
                    case 1: day = "Day2";
                        break;
                    case 2: day = "Day3";
                        break;
                    case 3: day = "Day4";
                        break;
                    case 4: day = "Day5";
                        break;
                    case 5: day = "Day6";
                        break;
                    case 6: day = "Day7";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadExercise();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_VIDEO)
        {
            try
            {
                if(data != null)
                {
                    videoUri = data.getData();
                    videoView.stopPlayback();
                    videoView.clearAnimation();
                    videoView.suspend();
                    videoView.setVideoURI(videoUri);
                    videoView.start();
                }
                else
                {

                }

            }catch (Exception e)
            {
                e.printStackTrace();
            }

        }
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
                    imageView.setImageBitmap(bitmap);

                }
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        if(videoUri != null)
        {
            videoView.setVideoURI(videoUri);
            videoView.start();
        }
    }

    public void ChooseVideo(View view)
    {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_VIDEO);
    }
    public void ChooseImage(View view)
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE);
    }

    private String getExt(Uri uri)
    {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void UploadExercise()
    {
        String videoName = edtName.getText().toString();
        String calo = edtCalo.getText().toString();
        String search = edtName.getText().toString().toLowerCase();
        if(videoUri != null && imageUri != null && !TextUtils.isEmpty(videoName) && !TextUtils.isEmpty(calo))
        {
            ChangePathFireBase();
            databaseReferenceCheck.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        Toast.makeText(AddActivity.this, R.string.add_toast_existed, Toast.LENGTH_SHORT).show();
                    } else {
                        progressBar.setVisibility(View.VISIBLE);
                        final StorageReference referenceVideo = storageReferenceVideo.child(search + "_" + group + "_" + day);
                        final StorageReference referenceImage = storageReferenceImage.child(search + "_" + group + "_" + day);
                        uploadTaskVideo = referenceVideo.putFile(videoUri);
                        uploadTaskImage = referenceImage.putFile(imageUri);

                        uploadTaskVideo.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                referenceVideo.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String urlvideo = uri.toString();
                                        exercise.setVideoUrl(urlvideo);
                                        uploadTaskImage.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                referenceImage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        String urlimage = uri.toString();
                                                        exercise.setImageUrl(urlimage);
                                                        exercise.setName(videoName);
                                                        exercise.setCalo(calo);
                                                        exercise.setSearch(search);
                                                        exercise.setDay(day);
                                                        databaseReference.child(search).setValue(exercise);
                                                        progressBar.setVisibility(View.INVISIBLE);
                                                        Toast.makeText(AddActivity.this, R.string.add_toast_added, Toast.LENGTH_SHORT).show();
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(Exception e) {
                                                        e.getMessage();
                                                        Toast.makeText(AddActivity.this, "get image download url failed", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(Exception e) {
                                                e.getMessage();
                                                Toast.makeText(AddActivity.this, "upload image task failed", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(Exception e) {
                                        e.getMessage();
                                        Toast.makeText(AddActivity.this, "get video download url failed", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(Exception e) {
                                e.getMessage();
                                Toast.makeText(AddActivity.this, "upload video task failed", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }

                }

                @Override
                public void onCancelled(DatabaseError error) {

                }
            });
        }
        else{
            Toast.makeText(AddActivity.this,R.string.add_toast_require,Toast.LENGTH_SHORT).show();
        }
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

    private void ChangePathFireBase()
    {
        String name = edtName.getText().toString().toLowerCase();
        databaseReference = FirebaseDatabase.getInstance().getReference("Exercise/"+group+"/"+day);
        databaseReferenceCheck = FirebaseDatabase.getInstance().getReference("Exercise/"+group+"/"+day+"/"+name);
    }
}