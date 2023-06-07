package ru.petrelevich.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MessageWithClient {
    private final String messageStr;
    private final String clientId;

    private final Boolean specialRoom;

    @JsonCreator
    public MessageWithClient(
            @JsonProperty("messageStr") String messageStr,
            @JsonProperty("clientId") String clientId,
            @JsonProperty("specialRoom") Boolean specialRoom
    ) {
        this.messageStr = messageStr;
        this.clientId = clientId;
        this.specialRoom = specialRoom;
    }

    public MessageWithClient(String messageStr, String clientId) {
        this(messageStr,clientId,false);
    }
    public MessageWithClient(
            String messageStr) {
        this(messageStr, "");
    }

    public String getMessageStr() {
        return messageStr;
    }
    public String getClientId() {
        return clientId;
    }

    public Boolean isSpecialRoom() {
        return specialRoom;
    }

    @Override
    public String toString() {
        return "MessageWithClient{" +
                "messageStr='" + messageStr + '\'' +
                ", clientId='" + clientId + '\'' +
                ", specialRoom=" + specialRoom +
                '}';
    }
}
