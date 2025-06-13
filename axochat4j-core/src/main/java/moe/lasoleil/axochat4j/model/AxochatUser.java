package moe.lasoleil.axochat4j.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Value;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Value
public class AxochatUser {

    @SerializedName("name")
    @JsonProperty("name")
    @NotNull String name;

    @SerializedName("uuid")
    @JsonProperty("uuid")
    @NotNull UUID uuid;
}
