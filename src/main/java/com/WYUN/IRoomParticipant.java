package com.WYUN;

public interface IRoomParticipant {
    public void JoinRoom(IReceivedMessageEventListener l);

    public void LeaveRoom(IReceivedMessageEventListener listener);

    public void TellMessage(String source, String message);

    public Client GetClient();
}