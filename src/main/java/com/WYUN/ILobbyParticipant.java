package com.WYUN;

public interface ILobbyParticipant {
    public void ExitLobby(IReceivedMessageEventListener l);

    public void LeaveLobby(IReceivedMessageEventListener l);

    public void JoinLobby(IReceivedMessageEventListener l);

    public void UpdateRoomList(String list);

    public void UpdateLobbyMember(String list);

    public Client GetClient();
}