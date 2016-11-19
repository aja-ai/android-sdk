package ai.aja.sdk.dialogue.model;

import com.google.gson.annotations.SerializedName;

public class Location {

    @SerializedName("lat")
    public double latitude;

    @SerializedName("lng")
    public double longitude;

}
