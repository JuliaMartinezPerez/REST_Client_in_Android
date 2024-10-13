package com.example.entrega_rest_client;

import java.util.List;
import java.util.Vector;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface TrackManager {
    @GET("tracks/")
    Call<List<Track>> getTracks();

    @GET("tracks/{id}")
    Call<Track> getTrack(@Path("id")String id);

    @POST("tracks/")
    Call<Track> newTrack(@Body Track track);

    @PUT("tracks/")
    Call<Void> updatedTrack(@Body Track track);

    @DELETE("tracks/{id}")
    Call<Void> deleteTrack(@Path("id")String id);


}
