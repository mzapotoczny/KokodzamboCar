package pl.wroc.uni.ii.kokodzambocar.Api;

import pl.wroc.uni.ii.kokodzambocar.Constants;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by michal on 27.01.15.
 */
public class Authentication {
    private final OAuth2Password oauthApi;

    public Authentication() {
        RestAdapter restAdapter = new RestAdapter
                .Builder()
                .setEndpoint(Constants.apiEndPoint)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        oauthApi = restAdapter.create(OAuth2Password.class);
    }

    public void authenticate(String username, String password, Callback<Token> callback){
        oauthApi.getToken(username,
                password,
                Constants.appKey,
                Constants.appSecret,
                "password",
                callback);
    }

}
