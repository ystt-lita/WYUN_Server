package com.WYUN;

public interface ILobbyParticipant {
    public void ExitLobby(IReceivedMessageEventListener l);

    public void LeaveLobby(IReceivedMessageEventListener l);

    public void JoinLobby(IReceivedMessageEventListener l);

    public Client GetClient();
}