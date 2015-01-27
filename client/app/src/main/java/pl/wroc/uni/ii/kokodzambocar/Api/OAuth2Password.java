package pl.wroc.uni.ii.kokodzambocar.Api;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by michal on 27.01.15.
 */
public interface OAuth2Password {
    @FormUrlEncoded
    @POST("/oauth/token")
    void getToken(@Field("username") String username,
                  @Field("password") String password,
                  @Field("client_id") String clientId,
                  @Field("client_secret") String clientSecret,
                  @Field("grant_type") String grant_type,
                  Callback<Token> result);
}
