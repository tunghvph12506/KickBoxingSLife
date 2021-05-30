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
import android.util.Log;
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
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.appgym.R;
import com.example.appgym.model.Exercise;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class EditActivity extends AppCompatActivity {
    static final int PICK_VIDEO = 1;
    static final int PICK_IMAGE = 2;
    VideoView videoView;
    Button btnUpload;
    ProgressBar progressBar;
    TextView tvChallenge;
    EditText edtName,edtCalo;
    Uri videoUri,imageUri;
    MediaController mediaController;
    FirebaseStorage firebaseStorage;
    StorageReference storageReferenceVideo,storageReferenceImage;
    DatabaseReference databaseReference;
    Exercise exercise;
    UploadTask uploadTaskVideo,uploadTaskImage;
    Button btnChoose;
    ActionBar toolbar;
    ImageView imageView;
    Bitmap bitmap;
    String dataPath,group,name,videoUrl,imageUrl,search,calo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        Bundle gets = getIntent().getExtras();
        dataPath = gets.getString("dataPath");
        group = gets.getString("group");
        name = gets.getString("name");
        videoUrl = gets.getString("videoUrl");
        imageUrl = gets.getString("imageUrl");
        search = gets.getString("search");
        calo = gets.getString("calo");

        exercise = new Exercise();
        storageReferenceVideo = FirebaseStorage.getInstance().getReference("Videos");
        storageReferenceImage = FirebaseStorage.getInstance().getReference("Images");
        btnUpload = findViewById(R.id.button_upload_edit);
        progressBar = findViewById(R.id.progressBar_edit);
        edtName = findViewById(R.id.edt_videoname_edit);
        edtCalo = findViewById(R.id.edt_calo_edit);
        btnChoose = findViewById(R.id.btn_chooseVideo_edit);
        mediaController = new MediaController(this);
        tvChallenge = findViewById(R.id.tv_challenge_edit);
        imageView = findViewById(R.id.imgview_edit);
        videoView = findViewById(R.id.videoview_edit);

        edtName.setText(name);
        edtCalo.setText(calo);
        tvChallenge.setText(group+"Challenge");
        videoUri = Uri.parse(videoUrl);
        imageUri = Uri.parse(imageUrl);
        databaseReference = FirebaseDatabase.getInstance().getReference(dataPath);


        Picasso.get().load(imageUrl).into(imageView);
        videoView.setVideoPath(videoUrl);
        videoView.start();

        toolbar = getSupportActionBar();
        toolbar.setTitle(name);
        toolbar.setDisplayHomeAsUpEnabled(true);

        firebaseStorage = FirebaseStorage.getInstance();

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

    private void UploadVideo()
    {
        String videoName = edtName.getText().toString();
        String calo = edtCalo.getText().toString();
        if(videoUri != null && imageUri != null && !TextUtils.isEmpty(videoName) && !TextUtils.isEmpty(calo))
        {
            StorageReference imageRef = firebaseStorage.getReferenceFromUrl(imageUrl);
            StorageReference videoRef = firebaseStorage.getReferenceFromUrl(videoUrl);
            imageRef.delete();
            videoRef.delete();
            progressBar.setVisibility(View.VISIBLE);
            final StorageReference referenceVideo = storageReferenceVideo.child(search +"_"+group+ "." + getExt(videoUri));
            final StorageReference referenceImage = storageReferenceImage.child(search +"_"+group+ "." + getExt(imageUri));
            uploadTaskVideo = referenceVideo.putFile(videoUri);
            uploadTaskImage = referenceImage.putFile(imageUri);

            Task<Uri> uriTask = uploadTaskVideo.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful())
                    {
                        throw task.getException();
                    }

                    return referenceVideo.getDownloadUrl();
                }
            })
                    .addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if(task.isSuccessful())
                            {
                                Uri downloadUrl = task.getResult();
                                exercise.setVideoUrl(downloadUrl.toString());
                                Task<Uri> uriTask1 = uploadTaskImage.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                    @Override
                                    public Task<Uri> then(Task<UploadTask.TaskSnapshot> task) throws Exception {
                                        if(!task.isSuccessful())
                                        {
                                            throw task.getException();
                                        }

                                        return referenceImage.getDownloadUrl();
                                    }
                                })
                                        .addOnCompleteListener(new OnCompleteListener<Uri>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Uri> task) {
                                                if(task.isSuccessful())
                                                {
                                                    Uri downloadUrl = task.getResult();
                                                    exercise.setName(videoName);
                                                    exercise.setCalo(calo);
                                                    exercise.setSearch(search);
                                                    exercise.setImageUrl(downloadUrl.toString());
                                                    databaseReference.child(search).setValue(exercise);
                                                    progressBar.setVisibility(View.INVISIBLE);
                                                    Toast.makeText(EditActivity.this,"Data updated", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                            }else
                            {
                                Toast.makeText(EditActivity.this,"Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

        else
        {
            Toast.makeText(EditActivity.this,"All field are required", Toast.LENGTH_SHORT).show();
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

}