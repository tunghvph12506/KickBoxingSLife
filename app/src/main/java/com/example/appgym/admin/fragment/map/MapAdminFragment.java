package com.example.appgym.admin.fragment.map;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.appgym.R;
import com.example.appgym.admin.AddActivity;
import com.example.appgym.model.Advertisement;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class MapAdminFragment extends Fragment {
    static final int PICK_IMAGE = 2;
    Button btnUpload,btnChooseImage;
    Uri imageUri;
    EditText edt_room, edt_boss, edt_phone, edt_address;
    ImageView imageView;
    Bitmap bitmap;
    Advertisement advertisement;
    DatabaseReference databaseReference;
    StorageReference storageReferenceImage;
    UploadTask uploadTask;

    public MapAdminFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_admin_map, container, false);
        advertisement = new Advertisement();
        databaseReference = FirebaseDatabase.getInstance().getReference("Advertisement");
        storageReferenceImage = FirebaseStorage.getInstance().getReference("Images");

        edt_room = view.findViewById(R.id.edt_roomname_mapadd);
        edt_boss = view.findViewById(R.id.edt_bossname_mapadd);
        edt_phone = view.findViewById(R.id.edt_sdt_mapadd);
        edt_address = view.findViewById(R.id.edt_address_mapadd);

        imageView = view.findViewById(R.id.imgview_mapadd);

        btnUpload = view.findViewById(R.id.btn_upload_mapadd);
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadAdvertisement();
            }
        });

        btnChooseImage = view.findViewById(R.id.btn_chooseImage_mapadd);
        btnChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseImage();
            }
        });
        return view;


    }

    private void UploadAdvertisement()
    {
        String roomname = edt_room.getText().toString();
        String bossname = edt_boss.getText().toString();
        String numberphone = edt_phone.getText().toString();
        String address = edt_address.getText().toString();

        final StorageReference referenceImage = storageReferenceImage.child("advertisement_image");
        uploadTask = referenceImage.putFile(imageUri);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                referenceImage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String urlimage = uri.toString();
                        advertisement.setImageUrl(urlimage);
                        advertisement.setRoomname(roomname);
                        advertisement.setBossname(bossname);
                        advertisement.setNumberphone(numberphone);
                        advertisement.setAddress(address);
                        databaseReference.setValue(advertisement);
                        Toast.makeText(getActivity(), R.string.mapadd_toast_added, Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), R.string.mapadd_toast_imageurl_failed, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), R.string.mapadd_toast_uploadimage_failed, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void ChooseImage()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE || requestCode == Activity.RESULT_OK)
        {
            try {
                if(data != null)
                {
                    imageUri = data.getData();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(getActivity().getContentResolver(), imageUri));
                    }else {
                        bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                    }
                    imageView.setImageBitmap(bitmap);

                }
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}