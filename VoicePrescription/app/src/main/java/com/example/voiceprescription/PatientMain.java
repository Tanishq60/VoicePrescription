package com.example.voiceprescription;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.appcompat.app.AppCompatActivity;

public class PatientMain extends AppCompatActivity {
    private static final String TAG = "PatientMain";
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_main);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        Button signOutBtn = findViewById(R.id.signOutBtn);
        Button chatBtn = findViewById(R.id.pChatBtn);
        TextView nameTV = findViewById(R.id.uidTextView);
        final Button reqDocBtn = findViewById(R.id.reqDocBtn);

        final Intent intent = getIntent();

        if (intent.hasExtra("com.example.voiceprescription.REQFLAG")) {
            reqDocBtn.setEnabled(false);
            reqDocBtn.setText("Application Under Review");
        }

        if (user.getDisplayName() == null) nameTV.setText(user.getEmail());
        else nameTV.setText(user.getDisplayName());

        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Log.d(TAG, "onClick: signed Out");
                finish();
            }
        });

        chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PatientMain.this, ChatMain.class);
                intent.putExtra("CURR_USER_CAN_CHAT_TYPE", "doctor");
                startActivity(intent);
            }
        });

        reqDocBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(PatientMain.this,ApplyForDoctor.class);
                startActivity(intent);
//                Log.d(TAG, "onClick: trying request!");
//                Map<String, Object> request = new HashMap<>();
//                Map<String, Object> reqFlag = new HashMap<>();
//                FirebaseUser user = mAuth.getCurrentUser();
//                request.put("from", user.getUid());
//                request.put("docUrl", "TBD");
//                request.put("timeStamp", new Timestamp(new Date()));
//                reqFlag.put("requestForDoctor", true);
//                Log.d(TAG, "onClick: objects created.");
//                db.collection("requests").document()
//                        .set(request)
//                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        Log.d(TAG, "onSuccess: request added!");
//                        reqDocBtn.setEnabled(false);
//                    }
//                })
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Log.d(TAG, "onFailure: request failed!");
//                    }
//                });
//                db.collection("users")
//                        .document(user.getUid())
//                        .set(reqFlag, SetOptions.merge());
            }
        });

    }


}
