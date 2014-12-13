package pl.wroc.uni.ii.kokodzambocar.Api;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by michal on 13.12.14.
 */
public interface SessionApi {
    @GET("/sessions/{owner}")
    List<Session> getSessions(
            @Path("owner") Integer userId
    );
}
