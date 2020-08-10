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
import android.widget.TextView;

import com.example.voiceprescription.models.Chat;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class ChatWindow extends AppCompatActivity {
    private static final String TAG = "ChatWindow";
    
    private FirebaseAuth mAuth;
    private FirebaseDatabase db;
    private ArrayList<Chat> mChats;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        mChats = new ArrayList<>();

        Button sendBtn = findViewById(R.id.sendBtn);
        TextView messageTV = findViewById(R.id.messageEditText);

        final String uid = mAuth.getUid();
        Intent intent = getIntent();
        final String rid = intent.getExtras().get("CURR_CHAT_USER-ID").toString();
        String remail = intent.getExtras().get("CURR_CHAT_USER-EMAIL").toString();
        Log.d(TAG, "onCreate: " + rid + " " + remail);

        final String sPath = "chats/" + uid + "/" + rid;
        final String rPath = "chats/" + rid + "/" + uid;

        db.getReference("chats/" + uid + "/" + rid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        mChats.clear();
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            String key = data.getKey();
                            Log.d(TAG, "onDataChange: data => " + data.getValue());
                            mChats.add(data.getValue(Chat.class));
                            Log.d(TAG, "onDataChange: " + key);
                        }
                        initChatMessageRecycler();
                        Log.d(TAG, "onDataChange: " + dataSnapshot.getChildrenCount());
                        RecyclerView recycler = findViewById(R.id.chatWindowRecycler);
                        recycler.scrollToPosition(mChats.size() - 1);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.w(TAG, "Failed to read value.", databaseError.toException());
                    }
                });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText messageET = findViewById(R.id.messageEditText);
                String message = messageET.getText().toString();
                Chat chat = new Chat(true, new Date(), message);
                db.getReference(sPath).push().setValue(chat);
                chat.setSender(false);
                db.getReference(rPath).push().setValue(chat);
                messageET.setText("");
            }
        });

        messageTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecyclerView recycler = findViewById(R.id.chatWindowRecycler);
                recycler.scrollToPosition(mChats.size() - 1);
            }
        });
    }

    private void initChatMessageRecycler() {
        Log.d(TAG, "initChatMessageRecycler: starting");
        RecyclerView recycler = findViewById(R.id.chatWindowRecycler);
        ChatMessageAdapter adapter = new ChatMessageAdapter(mChats);
        recycler.setAdapter(adapter);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setStackFromEnd(true);
        recycler.setLayoutManager(manager);
    }

}
