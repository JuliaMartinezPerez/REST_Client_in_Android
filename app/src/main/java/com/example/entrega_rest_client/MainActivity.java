package com.example.entrega_rest_client;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements RecyclerViewAdapter.ItemClickListener {
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private EditText title;
    private EditText singer;
    private Track selectedTrack;
    int position;
    private List<Track> tracks = new ArrayList<>();
    private final int REQUEST_CODE = 1;

    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/dsaApp/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    private TrackManager trackManager = retrofit.create(TrackManager.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buscarTracks();

        title = findViewById(R.id.editTxt_title);
        singer = findViewById(R.id.editTxt_singer);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerViewAdapter(this, tracks);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }


    //MANAGE TRACKS
    public void buscarTracks() {
        Call<List<Track>> call = trackManager.getTracks();
        call.enqueue(new Callback<List<Track>>() {
            @Override
            public void onResponse(Call<List<Track>> call, Response<List<Track>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    tracks = response.body();
                    for (Track track : tracks) {
                        adapter.addTrack(track);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Track>> call, Throwable throwable) {
                Log.d("JULIA", "Network failure: " + throwable.getMessage());
            }
        });
    }

    public void editTrack(Track editedTrack, int positionTrack) {
        Call<Void> call = trackManager.updatedTrack(editedTrack);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                    adapter.editTrack(editedTrack, positionTrack);
                    tracks.remove(positionTrack);
                    tracks.add(positionTrack, editedTrack);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                Log.d("JULIA", "onFailure: "+ throwable.getMessage());
            }
        });
    }

    public void addTrack(Track newTrack) {
        Call<Track> call = trackManager.newTrack(newTrack);
        call.enqueue(new Callback<Track>() {
            @Override
            public void onResponse(Call<Track> call, Response<Track> response) {
                Track trackAdded = response.body();
                adapter.addTrack(trackAdded);
                tracks.add(trackAdded);
            }

            @Override
            public void onFailure(Call<Track> call, Throwable throwable) {
                Log.d("JULIA", throwable.getMessage());
            }
        });
    }

    public void deleteTrack(Track deletedTrack){
        Call<Void> call = trackManager.deleteTrack(deletedTrack.getId());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                adapter.deleteTrack(position);
                tracks.remove(deletedTrack);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                Log.d("JULIA", throwable.getMessage());
            }
        });
    }


    //BUTTONS
    public void click_addTrack(View v) {
        String titleTxt = title.getText().toString();
        String singerTxt = singer.getText().toString();
        if (titleTxt.trim().isEmpty() || singerTxt.trim().isEmpty()) {
            Toast.makeText(this, "Please insert the Title and Singer to create a new song",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        for (Track track : tracks) {
            if (Objects.equals(track.getTitle(), titleTxt)
                    && Objects.equals(track.getSinger(), singerTxt)) {
                Toast.makeText(this, "Song already exists",
                        Toast.LENGTH_SHORT).show();
                return;
            }
        }
        addTrack(new Track(titleTxt, singerTxt));
        title.getText().clear();
        singer.getText().clear();
    }

    public void click_refresh(View v){
        adapter.clear();
        tracks.clear();
        buscarTracks();
    }


    //NEW ACTIVITY (EDIT/DELETE TRACK)
    @Override
    public void onItemClick(View view, int position) {
        this.position = position;
        selectedTrack = adapter.getTrack(position);
        showAlertDialog();
    }

    public void showAlertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select an option")
                .setMessage("DELETE the song or EDIT the song")
                .setPositiveButton("EDIT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent myIntent = new Intent(MainActivity.this, MainActivity2.class);
                        myIntent.putExtra("position", position);
                        myIntent.putExtra("track_id", selectedTrack.getId());
                        myIntent.putExtra("track_title", selectedTrack.getTitle());
                        myIntent.putExtra("track_singer", selectedTrack.getSinger());
                        MainActivity.this.startActivityForResult(myIntent, REQUEST_CODE);
                    }
                })
                .setNegativeButton("DELETE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteTrack(selectedTrack);
                        dialog.dismiss();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent dataTrack) {
        super.onActivityResult(requestCode, resultCode, dataTrack);
        if(requestCode == REQUEST_CODE){
            if(resultCode == Activity.RESULT_OK){
                Track editedTrack = new Track(dataTrack.getStringExtra("track_id"),
                        dataTrack.getStringExtra("track_title"), dataTrack.getStringExtra("track_singer"));
                editTrack(editedTrack, dataTrack.getIntExtra("position", 0));
            }
        }
    }
}