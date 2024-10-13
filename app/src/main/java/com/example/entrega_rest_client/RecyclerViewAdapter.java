package com.example.entrega_rest_client;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private List<Track> trackList;
    private LayoutInflater mInflater;

    private ItemClickListener mClickListener;
    public RecyclerViewAdapter(Context context, List<Track> tracks) {
        this.mInflater = LayoutInflater.from(context);
        this.trackList = tracks;
        Log.d("JULIA", "Constructor RecyclerViewAdapter");

    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_row, parent, false);
        Log.d("JULIA", "CreateViewHolder adapter");
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        Track track = trackList.get(position);
        //holder.txtView_id.setText(track.getId());
        holder.txtView_title.setText(track.getTitle());
        holder.txtView_singer.setText(track.getSinger());

        Log.d("JULIA", "BindViewHolder: " + track.getTitle());
    }

    @Override
    public int getItemCount() {
        return trackList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView txtView_title;
        TextView txtView_singer;

        ViewHolder(View itemView) {
            super(itemView);
            txtView_title = itemView.findViewById(R.id.txt_title);
            txtView_singer = itemView.findViewById(R.id.txt_singer);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mClickListener != null) mClickListener.onItemClick(v, getAdapterPosition());
        }
    }

    public Track getTrack(int id) {
        return trackList.get(id);
    }

    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public void addTrack(Track track){
        trackList.add(track);
        notifyItemInserted(trackList.size()-1);
    }

    public void editTrack(Track track, int position){
        trackList.get(position).setTitle(track.getTitle());
        trackList.get(position).setSinger(track.getSinger());
        notifyItemChanged(position);
    }

    public void deleteTrack(int position){
        trackList.remove(position);
        notifyItemRemoved(position);
    }

    public void clear(){
        trackList.clear();
        notifyDataSetChanged();
    }
}
