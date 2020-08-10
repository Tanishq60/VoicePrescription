package com.example.voiceprescription;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.voiceprescription.models.Chat;

import java.text.DateFormat;
import java.util.ArrayList;

public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.ViewHolder> {

    private static final String TAG = "ChatMessageAdapter";
    private ArrayList<Chat> mChats = new ArrayList<>();

    public ChatMessageAdapter(ArrayList<Chat> mChats) {
        this.mChats = mChats;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.message.setText(mChats.get(position).getMessage());
        holder.time.setText(DateFormat.getInstance().format(mChats.get(position).getTime()));
    }

    @Override
    public int getItemCount() {
        return mChats.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView message, time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.messageTV);
            time = itemView.findViewById(R.id.timeTV);
        }
    }
}
