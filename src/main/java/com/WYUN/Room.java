package com.WYUN;

import java.util.LinkedList;
import java.util.List;

public class Room implements IReceivedMessageEventListener {
    // RoomOptions options;
    List<IRoomParticipant> participants;
    Lobby parentLobby;

    Room(String o, Lobby p) {
        parentLobby = p;
        // options=DeserializeOptions(o);
        participants = new LinkedList<IRoomParticipant>();
    }

    public void Dispatch(ReceivedMessageEvent event) {
        String m = event.GetMessage();
        IRoomParticipant p = (IRoomParticipant) event.GetSource();
        System.out.println("[Room]message received: " + m);
        if (m.equals("leave")) {
            p.LeaveRoom(this);
            parentLobby.Add(p.GetClient());
            participants.remove(p);
        }
    }

    public void Add(IRoomParticipant p) {
        System.out.println("adding client");
        p.JoinRoom(this);
        participants.add(p);
        // Room Member Changed!!!
    }
}