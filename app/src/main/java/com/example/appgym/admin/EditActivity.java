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
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
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
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class EditActivity extends AppCompatActivity {
    static final int PICK_VIDEO = 1;
    static final int PICK_IMAGE = 2;
    String a;
    VideoView videoView;
    Button btnUpload;
    ProgressBar progressBar;
    TextView tvChallenge,tvDay;
    EditText edtName,edtCalo;
    Uri videoUri,imageUri;
    MediaController mediaController;
    FirebaseStorage firebaseStorage;
    StorageReference storageReferenceVideo,storageReferenceImage;
    DatabaseReference databaseReference;
    UploadTask uploadTaskVideo,uploadTaskImage;
    Exercise exercise;
    Button btnChoose;
    ActionBar toolbar;
    ImageView imageView;
    Bitmap bitmap;
    String dataPath,group,groupVn,day,dayVn,name,videoUrl,imageUrl,search,calo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        Bundle gets = getIntent().getExtras();
        dataPath = gets.getString("dataPath");
        group = gets.getString("group");
        groupVn = gets.getString("groupVn");
        day = gets.getString("day");
        dayVn = gets.getString("dayVn");
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
        tvDay = findViewById(R.id.tv_day_edit);
        imageView = findViewById(R.id.imgview_edit);
        videoView = findViewById(R.id.videoview_edit);

        edtName.setText(name);
        edtCalo.setText(calo);
        tvChallenge.setText("Tập "+groupVn);
        tvDay.setText(dayVn);
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
                UploadExercise();
            }
        });

        exercise.setVideoUrl(videoUrl);
        exercise.setImageUrl(imageUrl);
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
                    changeVideo();
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
                    changeImage();
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
        int a = 0;
        if(!TextUtils.isEmpty(videoName) && !TextUtils.isEmpty(calo)) {
            progressBar.setVisibility(View.VISIBLE);
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    exercise.setName(videoName);
                    exercise.setCalo(calo);
                    exercise.setSearch(search);
                    exercise.setDay(day);
                    databaseReference.child(search).setValue(exercise);
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(EditActivity.this, R.string.edit_toast_updated, Toast.LENGTH_SHORT).show();
                }
            }, 3 * 1000);

        }
        else
        {
            Toast.makeText(EditActivity.this,R.string.edit_toast_require,Toast.LENGTH_SHORT).show();
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

    private void changeVideo()
    {
        StorageReference videoRef = firebaseStorage.getReferenceFromUrl(videoUrl);
        videoRef.delete();
        final StorageReference referenceVideo = storageReferenceVideo.child(search + "_" + group + "_" + day);
        uploadTaskVideo = referenceVideo.putFile(videoUri);
        uploadTaskVideo.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                referenceVideo.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String urlvideo = uri.toString();
                        exercise.setVideoUrl(urlvideo);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        e.getMessage();
                        Toast.makeText(EditActivity.this, R.string.edit_toast_videourl_failed, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                e.getMessage();
                Toast.makeText(EditActivity.this, R.string.edit_toast_uploadvideo_failed, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void changeImage()
    {
        StorageReference imageRef = firebaseStorage.getReferenceFromUrl(imageUrl);
        imageRef.delete();
        final StorageReference referenceImage = storageReferenceImage.child(search + "_" + group + "_" + day);
        uploadTaskImage = referenceImage.putFile(imageUri);
        uploadTaskImage.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                referenceImage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String urlimage = uri.toString();
                        exercise.setImageUrl(urlimage);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        e.getMessage();
                        Toast.makeText(EditActivity.this, R.string.edit_toast_imageurl_failed, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                e.getMessage();
                Toast.makeText(EditActivity.this, R.string.edit_toast_uploadimage_failed, Toast.LENGTH_SHORT).show();
            }
        });
    }
}