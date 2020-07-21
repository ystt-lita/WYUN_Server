package com.WYUN;

import java.util.LinkedList;
import java.util.List;

public class Lobby implements IReceivedMessageEventListener {
    long appID;
    List<ILobbyParticipant> participants;
    List<Room> rooms;

    Lobby(long id) {
        appID = id;
        participants = new LinkedList<ILobbyParticipant>();
        rooms = new LinkedList<Room>();
    }

    public void Dispatch(ReceivedMessageEvent event) {
        String m = event.GetMessage();
        ILobbyParticipant p = (ILobbyParticipant) event.GetSource();
        System.out.println("[Lobby/" + appID + "]:ReceivedMessage:" + m);
        if (m.equals("exit")) {
            p.ExitLobby(this);
            participants.remove(p);
            // Loby Member Changed!!!
        } else if (m.substring(0, 6).equals("create")) {
            Room r = new Room(m.substring(7));
            rooms.add(r);
            // Room List Changed!!!
            p.LeaveLobby(this);
            r.Add(p.GetClient());
            participants.remove(p);
            // Lobby Member Changed!!!
        }
    }

    public void Add(ILobbyParticipant cl) {
        cl.JoinLobby(this);
        participants.add(cl);
        // Joined Lobby!!!
        // Lobby Member Changed!!!
    }
}