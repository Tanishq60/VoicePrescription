package com.example.voiceprescription;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class ApplyForDoctor extends AppCompatActivity {

    private FirebaseFirestore db;
    FirebaseStorage storage;
    private FirebaseAuth mAuth;
    StorageReference storageReference;
    private Uri url;

    private static final String TAG = "ApplyForDoctor";

    private Button upload;
//    private RelativeLayout relativeLayout;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 2212;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_for_doctor);


//        relativeLayout=findViewById(R.id.relativeLayout);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
    }

    public void ChooseFile(View view){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image from here..."), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            // Get the Uri of data
            filePath = data.getData();

            if (filePath != null) {

                // Code for showing progressDialog while uploading
                final ProgressDialog progressDialog= new ProgressDialog(this);
                progressDialog.setTitle("Uploading...");
                progressDialog.show();

                final FirebaseUser user = mAuth.getCurrentUser();
//                final HashMap<String, Object> docData=new HashMap<>();

                // Defining the child of storageReference
//                final String random=UUID.randomUUID().toString();
                final StorageReference ref = storageReference.child(filePath.getLastPathSegment());
                Log.d(TAG," image name "+ref);

//                docData.put("Images", Arrays.asList(random));

                // adding listeners on upload
                // or failure of image
                ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                        @Override
                        public void onSuccess(
                                UploadTask.TaskSnapshot taskSnapshot)
                        {

                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    url = (uri);
                                    Log.d(TAG, " image ka url " + String.valueOf(url));


//
                                    progressDialog.dismiss();
                                    Toast.makeText(ApplyForDoctor.this, "Image Uploaded!!", Toast.LENGTH_SHORT).show();
                                    Log.d(String.valueOf(url), "Inside data bases reference");


                                    Log.d(TAG, "onClick: trying request!");
                                    Map<String, Object> request = new HashMap<>();
                                    Map<String, Object> reqFlag = new HashMap<>();
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    request.put("from", user.getUid());
                                    Log.d(TAG, "Inside ref above request.put docurl " + String.valueOf(url));
                                    request.put("docUrl", String.valueOf(url));
                                    request.put("timeStamp", new Timestamp(new Date()));
                                    reqFlag.put("requestForDoctor", true);
//                            request.put("filrUrl",random);
                                    Log.d(TAG, "onClick: objects created.");
                                    db.collection("requests").document()
                                            .set(request)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.d(TAG, "onSuccess: request added!");
//                                            reqDocBtn.setEnabled(false);
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.d(TAG, "onFailure: request failed!");
                                                }
                                            });
                                    db.collection("users")
                                            .document(user.getUid())
                                            .set(reqFlag, SetOptions.merge());

                                    Intent intent = new Intent(ApplyForDoctor.this, PatientMain.class);
                                    intent.putExtra("com.example.voiceprescription.REQFLAG", true);
                                    startActivity(intent);
                                }
                            });


                        }
                    })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {

                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast.makeText(ApplyForDoctor.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                // Progress Listener for loading
                                // percentage on the dialog box
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {
                                    double progress
                                            = (100.0
                                            * taskSnapshot.getBytesTransferred()
                                            / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage(
                                            "Uploaded "
                                                    + (int)progress + "%");
                                }
                            });
            }


        }
    }


}
