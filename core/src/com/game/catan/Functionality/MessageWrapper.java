package com.game.catan.Functionality;

import java.io.Serializable;

public class MessageWrapper<T> implements Serializable {
    MessageType type;
    T message;

    public MessageWrapper(MessageType type, T message) {
        this.type = type;
        this.message = message;
    }

    public MessageType getType() {
        return type;
    }

    public T getMessage() {
        return message;
    }
}
