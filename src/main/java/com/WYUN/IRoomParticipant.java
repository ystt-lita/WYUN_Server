package com.WYUN;

public interface IRoomParticipant {
    public void JoinRoom(IReceivedMessageEventListener l);

    public void LeaveRoom(IReceivedMessageEventListener listener);

    public void ExitRoom(IReceivedMessageEventListener listener);

    public void UpdateRoomOption(String option);

    public void TellMessage(String source, String message);

    public void UpdateRoomMember(String list);

    public Client GetClient();
}