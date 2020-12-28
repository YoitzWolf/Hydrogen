package com.example.hydro.request;

import com.example.hydro.request.models.Connection;
import com.example.hydro.request.models.Game;
import com.example.hydro.request.models.HUBBODY;
import com.example.hydro.request.models.Hub;
import com.example.hydro.request.models.LOGINBODY;
import com.example.hydro.request.models.REGISTERBODY;
import com.example.hydro.request.models.SIMPLEREQUEST;
import com.example.hydro.request.models.TOKENBODY;
import com.example.hydro.request.models.USER;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface NETWORK {

    @GET("/test")
    Call<SIMPLEREQUEST<String>> connectionTest();
    /*
    @GET("/usr/api/is_auntethicated")
    Call<SIMPLEREQUEST<String>> is_auntethicated(@Body TOKEN token);
     */

    @POST("/api/users/register")
    Call<SIMPLEREQUEST<TOKENBODY>> register(@Body REGISTERBODY form);

    @POST("/api/users/login")
    Call<SIMPLEREQUEST<TOKENBODY>> login(@Body LOGINBODY form);

    @POST("/api/users/refresh")
    Call<SIMPLEREQUEST<TOKENBODY>> refresh(@Body TOKENBODY refresh_token, @Query("out") String out);

    @GET("/api/users/getme")
    Call<SIMPLEREQUEST<USER>> getme(@Query("token") String token);

    @GET("/api/hubs/all")
    Call<SIMPLEREQUEST<List<Hub>>> getAllHubs();

    @GET("/api/hubs/filter")
    Call<SIMPLEREQUEST<List<Hub>>> getFilteredHubs(@Query("options") Map<String, String> options);

    @GET("/api/hubs/connections")
    Call<SIMPLEREQUEST<List<Connection>>> getConnections(@Query("token") String token);

    @GET("/api/hubs/get_by_id")
    Call<SIMPLEREQUEST<List<Connection>>> getCurrentHub(@Query("id") String id, @Query("token") String token);

    @GET("/api/hubs/get_task")
    Call<SIMPLEREQUEST<List<Task>>> getTask(@Query("id") String id, @Query("token") String token);

    @GET("/api/games/all")
    Call<SIMPLEREQUEST<List<Game>>> getAllGames();

    @POST("/api/hubs/new")
    Call<SIMPLEREQUEST<TOKENBODY>> newHub(@Body HUBBODY form);

}
