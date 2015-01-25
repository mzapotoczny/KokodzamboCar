package pl.wroc.uni.ii.kokodzambocar.Api;

import pl.wroc.uni.ii.kokodzambocar.Constants;
import retrofit.RestAdapter;

/**
 * Created by michal on 25.01.15.
 */
public class Api {
    public static class ApiImpl {
        public final SessionApi session;

        public ApiImpl(String endpoint) {
            RestAdapter restAdapter = new RestAdapter
                    .Builder()
                    .setEndpoint(endpoint)
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .build();
            session = restAdapter.create(SessionApi.class);
        }
    }
    private Api() {}
    private static ApiImpl instance;
    public static ApiImpl getInstance() {
        if (instance == null)
            instance = new ApiImpl(Constants.apiEndPoint);
        return instance;
    }
}
