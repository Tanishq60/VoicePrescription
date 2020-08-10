package com.example.voiceprescription;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ChatMain extends AppCompatActivity {
    private static final String TAG = "ChatMain";

    FirebaseDatabase mDatabase;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    ArrayList<String> chatList = new ArrayList<>();
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_main);

        Intent intent = getIntent();
        final String type = intent.getExtras().get("CURR_USER_CAN_CHAT_TYPE").toString();

        mDatabase = FirebaseDatabase.getInstance();
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        id = mAuth.getUid();
        DatabaseReference ref = mDatabase.getReference("chats/" + id);
        final CollectionReference userRef = db.collection("users");

        Button searchBtn = findViewById(R.id.searchBtn);


        userRef.whereEqualTo("email", "user@add.com")
                .whereEqualTo("type", type)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, document.getId() + " => " + document.getData());
                    }

                }
            }
        });

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatList.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    String key = data.getKey();
                    chatList.add(key);
                    Log.d(TAG, "onDataChange: " + key);
                }
                initChatRecycler();
                Log.d(TAG, "onDataChange: " + dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText searchET = findViewById(R.id.searchEditText);
                final String email = searchET.getText().toString();
                userRef.whereEqualTo("email", email)
                        .whereEqualTo("type", type)
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                Intent intent = new Intent(ChatMain.this, ChatWindow.class);
                                intent.putExtra("CURR_CHAT_USER-ID", document.getId());
                                intent.putExtra("CURR_CHAT_USER-EMAIL", email);
                                startActivity(intent);
                            }
                        }
                    }
                });
            }
        });

    }

    private void initChatRecycler() {
        Log.d(TAG, "initChatRecycler: starting");
        RecyclerView recyclerView = findViewById(R.id.chatRecycler);
        ChatListAdapter chatListAdapter = new ChatListAdapter(chatList);
        recyclerView.setAdapter(chatListAdapter);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setReverseLayout(true);
        recyclerView.setLayoutManager(manager);
    }
}
