package com.example.voiceprescription;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.voiceprescription.models.Request;

import java.util.ArrayList;

public class RequestRecyclerAdapter extends RecyclerView.Adapter<RequestRecyclerAdapter.RequestViewHolder> {

    private static final String TAG = "RequestRecyclerAdapter";
    private ArrayList<Request> mRequests = new ArrayList<>();

    public RequestRecyclerAdapter(ArrayList<Request> mRequests) {
        this.mRequests = mRequests;
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_list_item, parent, false);
        return new RequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: called @ " + position);

        holder.nameTextView.setText(mRequests.get(position).getFrom());
    }

    @Override
    public int getItemCount() {
        return mRequests.size();
    }

    public class RequestViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;
        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
        }
    }

}
