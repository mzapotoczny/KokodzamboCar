package pl.wroc.uni.ii.kokodzambocar.Api;

import com.google.gson.annotations.SerializedName;

/**
 * Created by michal on 27.01.15.
 */
public class Token {
    @SerializedName("access_token")
    public String accessToken;

    @SerializedName("token_type")
    public String tokenType;

    @SerializedName("expires_in")
    public String expiresIn;

    @SerializedName("refresh_token")
    public String refreshToken;
}
