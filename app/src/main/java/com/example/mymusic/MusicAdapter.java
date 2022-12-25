package com.example.mymusic;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public
class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.ViewHolder>{
ArrayList<AudioModel>songsList;
Context context;

    public
    MusicAdapter(ArrayList<AudioModel> songsList, Context context) {
        this.songsList = songsList;
        this.context = context;
    }

    @Override
    public
    ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycle_open,parent,false);
        return new MusicAdapter.ViewHolder(view);
    }

    @Override
    public
    void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        AudioModel songData = songsList.get(position);
        holder.titleTextView.setText(songData.getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View v) {
                // navigate to new activity
                Log.e(TAG, "onCreate: pass 3");

                My.getInstance().reset();
                My.currentindex=position;
                Intent intent = new Intent(context,MusicPlayerActivity.class);
                intent.putExtra("List",songsList);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });

    }

    @Override
    public
    int getItemCount() {
        return songsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView titleTextView;
        ImageView iconImageView;
        //create the constructer

        public
        ViewHolder( View itemView) {
            super(itemView);

            // for itemview you have to create the resore file in layout    name == recycle_open
            titleTextView =itemView.findViewById(R.id.music_title_text);
            iconImageView =itemView.findViewById(R.id.icom_view);




        }

    }
}
