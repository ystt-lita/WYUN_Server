/***************************************************
 * Copyright 2020, 2021 EwdErna
 * LICENSE: ../../../../../LICENSE
 ***************************************************/
package com.WYUN;

public interface ILobbyParticipant extends IGameClient {

    public void LeaveLobby(IReceivedMessageEventListener l);

    public void JoinLobby(IReceivedMessageEventListener l);

    public void UpdateRoomList(String list);

    public void UpdateLobbyMember(String list);

    public Client GetClient();
}