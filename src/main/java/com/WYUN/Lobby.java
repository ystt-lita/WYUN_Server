package com.WYUN;

import java.util.*;

public class Lobby implements IReceivedMessageEventListener {
    long appID;
    List<ILobbyParticipant> participants;
    List<Room> rooms;

    Lobby(long id) {
        appID = id;
        participants = new LinkedList<ILobbyParticipant>();
        rooms = new ArrayList<Room>();
    }

    public void Dispatch(ReceivedMessageEvent event) {
        String[] m = event.GetMessage().split(",");
        ILobbyParticipant p = (ILobbyParticipant) event.GetSource();
        System.out.println("[Lobby/" + appID + "]:ReceivedMessage:" + m);
        if (m[0].equals("exit")) {
            p.ExitLobby(this);
            participants.remove(p);
            // Loby Member Changed!!!
        } else if (m[0].equals("create")) {
            Room r = new Room(String.join(",", Arrays.asList(m).subList(1, m.length)), this);// この書き方やだ
            rooms.add(r);
            // Room List Changed!!!
            p.LeaveLobby(this);
            r.Add(p.GetClient());
            participants.remove(p);
            // Lobby Member Changed!!!
        } else if (m[0].equals("join")) {
            int i = Integer.parseInt(m[1]);
            // TODO: Roomのバージョン管理
            // 現状の実装では、クライアントからjoinの送信->ルームリストの更新->joinの受信、の可能性がある
            // この場合異なる部屋に入る可能性・OutOfBoundsの可能性がある
            p.LeaveLobby(this);
            rooms.get(i).Add(p.GetClient());
            participants.remove(p);
        }
    }

    public void Add(ILobbyParticipant cl) {
        cl.JoinLobby(this);
        participants.add(cl);
        // Joined Lobby!!!
        // Lobby Member Changed!!!
    }
}