package com.WYUN;

public class ReceivedMessageEvent {
    private String message;
    private Client source;

    ReceivedMessageEvent(Client s, String m) {
        message = m;
        source = s;
    }

    Client GetSource() {
        return source;
    }

    String GetMessage() {
        return message;
    }
}