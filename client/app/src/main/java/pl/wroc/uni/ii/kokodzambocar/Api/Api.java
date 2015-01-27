package pl.wroc.uni.ii.kokodzambocar.Api;


import pl.wroc.uni.ii.kokodzambocar.Constants;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;

/**
 * Created by michal on 25.01.15.
 */
public class Api {
    public static class ApiImpl {
        public final SessionApi session;

        public ApiImpl(String endpoint, final Token token) {
            RestAdapter restAdapter = new RestAdapter
                    .Builder()
                    .setEndpoint(endpoint)
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .setRequestInterceptor(new RequestInterceptor() {
                        @Override
                        public void intercept(RequestFacade request) {
                            request.addQueryParam("access_token", token.accessToken);
                            request.addHeader("Accept", "application/json");
                            request.addHeader("Content-Type", "application/json");
                        }
                    })
                    .build();
            session = restAdapter.create(SessionApi.class);
        }
    }


    private Api() {}
    private static ApiImpl instance;
    public static Token token;
    public static ApiImpl getInstance() {
        if (instance == null) {
            if (token == null)
                throw new RuntimeException("token is null");
            instance = new ApiImpl(Constants.apiEndPoint, token);
        }
        return instance;
    }
}
