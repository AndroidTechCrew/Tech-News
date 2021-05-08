package com.AndroidTechCrew.technews;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {
    private static final String TAG = "EditProfileActivity";
    private static final int CAMERA_PERM_CODE = 101;

    private Button buttonLogout;
    private FirebaseAuth mAuth;
    private TextView username;
    private TextView firstName;
    private TextView lastName;
    private EditText usernameEdit;
    private EditText lastNameEdit;
    private EditText firstNameEdit;
    private TextView changeProilePic;
    private ImageView profileImage;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private Button btnTakePhoto;
    private Button updateUser;
    private Button btnLibraryPhoto;
    public Uri imageUri;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private FirebaseFirestore fstore;
    private Button btnCloseProfile;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    String currentPhotoPath;
    private String basicPriofilePic = "https://cdn.business2community.com/wp-content/uploads/2017/08/blank-profile-picture-973460_640.png";
    private Uri myUri;
    private String submitCheck = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_picture);
        mAuth = FirebaseAuth.getInstance();
        username = findViewById(R.id.usernameTag);
        lastName = findViewById(R.id.lastNameTag);
        firstName = findViewById(R.id.firstNameTag);
        usernameEdit = findViewById(R.id.usernameEdit);
        lastNameEdit = findViewById(R.id.lNameEdit);
        firstNameEdit = findViewById(R.id.fNameEdit);
        profileImage = findViewById(R.id.ivProfilePic);
        changeProilePic = findViewById(R.id.changeProfilePic);
        updateUser = findViewById(R.id.updateUser);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        fstore = FirebaseFirestore.getInstance();
        btnCloseProfile = findViewById(R.id.btnCloseProfile);
        FirebaseUser currentUser = mAuth.getCurrentUser();
        loaduserinfo(currentUser);
        StorageReference profileRef = storageReference.child("users/" + currentUser.getUid() + "/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(EditProfileActivity.this).load(uri).override(500, 500).circleCrop().into(profileImage);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
//                myUri = Uri.parse(basicPriofilePic);
                Glide.with(EditProfileActivity.this).load(basicPriofilePic).override(500, 500).circleCrop().into(profileImage);
            }
        });

        changeProilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDiaglog();
            }
        });
        updateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String firstname = firstNameEdit.getText().toString();
                String lastname = lastNameEdit.getText().toString();
                String username = usernameEdit.getText().toString();
                if (firstname.isEmpty()) {
                    firstNameEdit.setError("First Name is required");
                    firstNameEdit.requestFocus();
                    return;
                }
                if (lastname.isEmpty()) {
                    lastNameEdit.setError("Last Name is required");
                    lastNameEdit.requestFocus();
                    return;
                }
                if (username.isEmpty()) {
                    usernameEdit.setError("Username is required");
                    usernameEdit.requestFocus();
                    return;
                }
                if (!firstname.isEmpty() && !username.isEmpty() && !username.isEmpty()) {
                    saveUser(currentUser);
                }
            }
        });
        btnCloseProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void loaduserinfo(FirebaseUser user) {
        db.collection("users").document(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    // Document found in the offline cache
                    DocumentSnapshot document = task.getResult();
                    firstNameEdit.setText(document.getData().get("firstName").toString());
                    lastNameEdit.setText(document.getData().get("lastName").toString());
                    usernameEdit.setText(document.getData().get("Username").toString());
//                    profilePic = document.getData().get("profileImage").toString();

                } else {
                    Log.d(TAG, "Cached get failed: ", task.getException());
                }
            }
        });
    }


    public void createDiaglog(){
        dialogBuilder = new AlertDialog.Builder(this);
        final View accountPopup = getLayoutInflater().inflate(R.layout.dialog_profile_pic,null);
        btnTakePhoto = (Button) accountPopup.findViewById(R.id.btnTakePhoto);
        btnLibraryPhoto = (Button) accountPopup.findViewById(R.id.btnPhotoLibrary);
        dialogBuilder.setView(accountPopup);
        btnLibraryPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePicture();
            }
        });
        btnTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                launchCamera();
//                dispatchTakePictureIntent();
                askCameraPermissions();
            }
        });
        dialogBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (submitCheck != null) {
                    uploadPicture(imageUri);
                }
                else {
                    Toast.makeText(EditProfileActivity.this, "No Image Added", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
                Toast.makeText(EditProfileActivity.this, "Canceled", Toast.LENGTH_SHORT).show();
            }
        });

        dialog = dialogBuilder.create();
        dialog.show();
//        if (submitCheck != null) {
//            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
//        }

    }

    private void askCameraPermissions() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.CAMERA}, CAMERA_PERM_CODE);
        }else {
            dispatchTakePictureIntent();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == CAMERA_PERM_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                dispatchTakePictureIntent();
            }else {
                Toast.makeText(this, "Camera Permission is Required to Use camera.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveUser(FirebaseUser currentUser) {
//        url = imageUri;
        Map<String, String> userDoc = new HashMap<>();
        userDoc.put("email", currentUser.getEmail());
        userDoc.put("firstName",firstNameEdit.getText().toString());
        userDoc.put("lastName",lastNameEdit.getText().toString());
        userDoc.put("Username",usernameEdit.getText().toString());
//        if (!imageUri.toString().equals("users/" + Objects.requireNonNull(mAuth.getCurrentUser()).getUid() + "/profile.jpg")) {
//            imageUri = myUri;
//            userDoc.put("profileImage", imageUri.toString());
//        }
//        else {
//        userDoc.put("profileImage", imageUri.toString());

        userDoc.put("uid", currentUser.getUid());
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(currentUser.getUid())
                .set(userDoc)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                        Snackbar.make(findViewById(android.R.id.content), "User Updated", Snackbar.LENGTH_LONG).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }

    private void choosePicture() {

        Intent galleryIntent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        intent.setType("image/*");
////        intent.setAction(Intent.ACTION_GET_CONTENT);
//        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        startActivityForResult(galleryIntent,1);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                imageUri = data.getData();
//                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//                String imageFileName = "JPEG_" + timeStamp +"."+ getFileExt(contentUri);
//                Log.d("tag", "onActivityResult: Gallery Image Uri:  " +  imageFileName);
                submitCheck = "Yes";
                Toast.makeText(this, "Picture Choosen", Toast.LENGTH_SHORT).show();


            }
        }
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if(resultCode == Activity.RESULT_OK) {
//            Bitmap photo = (Bitmap)data.getExtras().get("data");
//            profileImage.setImageBitmap(photo);
                submitCheck = "Yes";
                File file = new File(currentPhotoPath);
                imageUri = Uri.fromFile(file);
                Toast.makeText(this, "Picture Taken", Toast.LENGTH_SHORT).show();
//                Log.d(TAG, "ABsolute Url of Image is " + Uri.fromFile(file));
//                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
//                profileImage.setImageBitmap(takenImage);

            }
        }
    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.AndroidTechCrew.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent,CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
            }
        }
    }



    private void uploadPicture(Uri imageUri) {
        StorageReference photoStorage = storageReference.child("users/" + mAuth.getCurrentUser().getUid() + "/profile.jpg");
        photoStorage.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Snackbar.make(findViewById(android.R.id.content), "Image Uploaded", Snackbar.LENGTH_LONG).show();
                photoStorage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(EditProfileActivity.this).load(uri).circleCrop().into(profileImage);
//                        Glide.with(getContext()).load(uri)..into(profileImage);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toast.makeText(EditProfileActivity.this, "Failed to Upload", Toast.LENGTH_SHORT).show();

            }
        });

    }
}
