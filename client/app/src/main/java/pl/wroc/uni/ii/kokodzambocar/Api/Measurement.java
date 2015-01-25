package pl.wroc.uni.ii.kokodzambocar.Api;

import com.google.gson.annotations.SerializedName;

/**
 * Created by michal on 25.01.15.
 */
public class Measurement {
    @SerializedName("pid_id")
    public Integer pid;
    public String  value;

    public Measurement(Integer pid, String value){
        this.pid = pid;
        this.value = value;
    }
}
