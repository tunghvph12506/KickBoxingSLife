package com.example.appgym.admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.appgym.R;
import com.example.appgym.Video;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
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
    VideoView videoView;
    Button btnUpload;
    ProgressBar progressBar;
    EditText edtName;
    Uri videoUri;
    MediaController mediaController;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    Video video;
    UploadTask uploadTask;
    Button btnChoose;
    ActionBar toolbar;
    Spinner spinnerChallenge;
    List<String> list;
    String group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        video = new Video();
        storageReference = FirebaseStorage.getInstance().getReference("Video");
        btnUpload = findViewById(R.id.button_upload_main);
        progressBar = findViewById(R.id.progressBar_main);
        edtName = findViewById(R.id.et_video_name);
        btnChoose = findViewById(R.id.btn_chooseVideo);
        mediaController = new MediaController(this);
        spinnerChallenge = findViewById(R.id.spinner_challenge);
        group = "Chest";
        videoView = findViewById(R.id.videoview_main);
        videoView.start();

        toolbar = getSupportActionBar();
        toolbar.setTitle(R.string.title_add);
        toolbar.setDisplayHomeAsUpEnabled(true);

        list = new ArrayList<>();
        list.add("Chest Challenge");
        list.add("Stomach Challenge");
        list.add("Hand Challenge");
        list.add("Leg Challenge");
        ArrayAdapter spinnerAdapter = new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,list);
        spinnerChallenge.setAdapter(spinnerAdapter);

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

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadVideo();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_VIDEO || resultCode == Activity.RESULT_OK)
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

    }

    public void ChooseVideo(View view)
    {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_VIDEO);
    }

    private String getExt(Uri uri)
    {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void UploadVideo()
    {
        String videoName = edtName.getText().toString();
        String search = edtName.getText().toString().toLowerCase();
        if(videoUri != null || !TextUtils.isEmpty(videoName))
        {
            ChangePathFireBase();
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if(snapshot.exists())
                    {
                        Toast.makeText(AddActivity.this,"Existed", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        progressBar.setVisibility(View.VISIBLE);
                        final StorageReference reference = storageReference.child(System.currentTimeMillis() + "." + getExt(videoUri));
                        uploadTask = reference.putFile(videoUri);

                        Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                if(!task.isSuccessful())
                                {
                                    throw task.getException();
                                }
                                return reference.getDownloadUrl();
                            }
                        })
                                .addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        if(task.isSuccessful())
                                        {
                                            Uri downloadUrl = task.getResult();
                                            progressBar.setVisibility(View.INVISIBLE);
                                            Toast.makeText(AddActivity.this,"Data saved", Toast.LENGTH_SHORT).show();

                                            video.setName(videoName);
                                            video.setVideourl(downloadUrl.toString());
                                            video.setSearch(search);
                                            String i = databaseReference.push().getKey();
                                            databaseReference.child(i).setValue(video);
                                        }else
                                        {
                                            Toast.makeText(AddActivity.this,"Failed", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {

                }
            });

        }
        else
        {
            Toast.makeText(AddActivity.this,"All field are required", Toast.LENGTH_SHORT).show();
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
        databaseReference = FirebaseDatabase.getInstance().getReference("Data/"+group+"/"+name);
    }
}