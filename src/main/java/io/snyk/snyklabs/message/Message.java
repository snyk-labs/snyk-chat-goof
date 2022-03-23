package io.snyk.snyklabs.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Message {

    public final MessageTypes type;
    public final String userName;
    public final String message;

    @JsonCreator
    public Message(@JsonProperty("type") MessageTypes type,
                   @JsonProperty("userName") String userName,
                   @JsonProperty("message") String message) {
        this.type = type;
        this.userName = userName;
        this.message = message;
    }

    @Override
    public String toString() {
        return "Message{" +
                "type=" + type +
                ", userName='" + userName + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
