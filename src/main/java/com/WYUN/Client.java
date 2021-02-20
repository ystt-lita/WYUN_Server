/***************************************************
 * Copyright 2020, 2021 EwdErna
 * LICENSE: ../../../../../LICENSE
 ***************************************************/
package com.WYUN;

import java.io.*;
import java.net.Socket;

public class Client implements ILobbyParticipant, IRoomParticipant {
    Socket client;
    BufferedWriter wBuff;
    MessageReceiver receiver;

    public String name;
    public Long appID;

    public Client(Socket s) {
        client = s;
        receiver = new MessageReceiver(this, s);
        try {
            wBuff = new BufferedWriter(new OutputStreamWriter(client.getOutputStream(), "UTF-8"));
            SendMessage("connected");
            name = receiver.GetMessage();
            appID = Long.parseLong(receiver.GetMessage());
            System.out.println("client name:" + name);
        } catch (IOException ioException) {
            System.out.println("error occured on Setting up client.");
            ioException.printStackTrace();
        }
    }

    public void SendMessage(String message) {
        try {
            wBuff.write(message + "\r\n");
            wBuff.flush();
            System.out.println("sent message: " + message);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void TellMessage(String source, String message) {
        SendMessage(String.join(",", "tell", source, message));
    }

    public void JoinLobby(IReceivedMessageEventListener l) {
        SendMessage("joinedLobby");
        receiver.AddEventListener(l);
        if (!receiver.isAlive()) {
            receiver.start();
        }
    }

    public void UpdateRoomList(String list) {
        SendMessage("roomList," + list);
    }

    public void UpdateLobbyMember(String list) {
        SendMessage("lobbyMember," + list);
    }

    public void LeaveLobby(IReceivedMessageEventListener l) {
        SendMessage("leftLobby");
        receiver.RemoveEventListener(l);
    }

    public void NotifyError(String errorMessage) {
        SendMessage("error," + errorMessage);
    }

    public void close() {
        try {
            receiver.close();
            System.out.println("receiver joined.");
            client.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public Client GetClient() {
        return this;
    }

    public void JoinRoom(IReceivedMessageEventListener room) {
        SendMessage("joinedRoom");
        receiver.AddEventListener(room);
    }

    public void UpdateRoomOption(String option) {
        SendMessage("roomOption," + option);
    }

    public void UpdateRoomMember(String list) {
        SendMessage("roomMember," + list);
    }

    public void LeaveRoom(IReceivedMessageEventListener l) {
        SendMessage("leftRoom");
        receiver.RemoveEventListener(l);
    }

    public void Exit(IReceivedMessageEventListener l) {
        SendMessage("exit");
        receiver.RemoveEventListener(l);
        close();
    }
}