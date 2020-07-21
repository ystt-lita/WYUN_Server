package com.WYUN;

public interface IRoomParticipant {
    public void JoinRoom(IReceivedMessageEventListener l);

    public void LeaveRoom(IReceivedMessageEventListener listener);

    public Client GetClient();
}