package com.mahesh.demofirebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UserActivity extends AppCompatActivity {
    private EditText etTitle,etDescription;
    private ImageView imageView;
    private Uri imageUri;
    private String title,description;
    private StorageReference storageReference;
    private DatabaseReference reference;
    private String uid;
    String currentPicPath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        etTitle         = findViewById(R.id.et_title_user);
        etDescription   = findViewById(R.id.et_description_user);
        imageView = findViewById(R.id.imageView);
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        storageReference = FirebaseStorage.getInstance().getReference("uploads");
        reference = FirebaseDatabase.getInstance().getReference("feeds");
    }

    public void onUploadTapped(View view) {
        title       = etTitle.getText().toString().trim();
        description = etDescription.getText().toString().trim();
        uploadImage();
    }

    private void uploadImage() {
        final StorageReference fileRef = storageReference.child(System.currentTimeMillis()+"."+getFileExtension(imageUri));
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Feed feed = new Feed(title,description,uri.toString(),uid);
                        String key = reference.push().getKey();
                        reference.child(key).setValue(feed);

                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UserActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onImageViewTapped(View view) {
       // dispatchTakePictureIntent();
       //     pickImage();
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Action");
        String[] options = {"From Gallery", "From Camera","Cancel"};
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: pickImage();
                    break;
                    case 1: dispatchTakePictureIntent();
                    break;
                    case 2: return;
                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void pickImage() {
        Intent pickIntent = new Intent();
        pickIntent.setType("image/*");
        pickIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(pickIntent,100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100 && resultCode==RESULT_OK && data !=null) {
            imageUri = data.getData();
            imageView.setImageURI(imageUri);

        }
        else if (requestCode == 222 && resultCode == RESULT_OK){
            File file = new File(currentPicPath);
            imageUri = Uri.fromFile(file);
           imageView.setImageURI(imageUri);
        }
    }

    public void onViewFeedTapped(View view) {
        startActivity(new Intent(UserActivity.this,UserFeedActivity.class));
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return  mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private File createImageFile() throws IOException {
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(""+System.currentTimeMillis(),".jpg",storageDir);
        currentPicPath = image.getAbsolutePath();
        return  image;
    }

    private void dispatchTakePictureIntent() {
        Intent pickIntent = new Intent();
        pickIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        if (pickIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            }
            catch (IOException ex) {
                Toast.makeText(this, " creating file error", Toast.LENGTH_SHORT).show();
            }
            if (photoFile != null){
                Uri photoUri = FileProvider.getUriForFile(this,"com.mahesh.android.fileprovider",photoFile);
                pickIntent.putExtra(MediaStore.EXTRA_OUTPUT,photoUri);
            }
            startActivityForResult(pickIntent, 222);
        }
    }
}
