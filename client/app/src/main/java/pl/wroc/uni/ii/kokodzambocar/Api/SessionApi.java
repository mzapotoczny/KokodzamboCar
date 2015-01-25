package pl.wroc.uni.ii.kokodzambocar.Api;

import java.util.List;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Created by michal on 13.12.14.
 */
public interface SessionApi {
    @GET("/sessions")
    void getSessions(Callback<List<Session>> callback);

    @POST("/sessions")
    void addSession(@Body SessionName sessionName, Callback<Session> callback);

    @POST("/sessions/{id}")
    Response addMeasurements(@Path("id") Integer sessionId, @Body List<Measurement> measurements);
}
