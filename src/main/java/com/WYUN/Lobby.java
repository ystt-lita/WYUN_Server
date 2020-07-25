package com.WYUN;

import java.util.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Lobby implements IReceivedMessageEventListener {
    long appID;
    List<ILobbyParticipant> participants;
    List<Room> rooms;
    RoomList rList;

    Lobby(long id) {
        appID = id;
        participants = new LinkedList<ILobbyParticipant>();
        rooms = new ArrayList<Room>();
        rList = new RoomList();
    }

    public void Dispatch(ReceivedMessageEvent event) {
        String m = event.GetMessage();
        ILobbyParticipant p = (ILobbyParticipant) event.GetSource();
        System.out.println("[Lobby/" + appID + "]:ReceivedMessage:" + m);
        if (m.equals("exit")) {
            p.ExitLobby(this);
            participants.remove(p);
            // TODO: ルームが空になったら削除
            // Loby Member Changed!!!
        } else if (m.substring(0, 6).equals("create")) {
            Room r = new Room(m.substring(7), this);
            rooms.add(r);
            // Room List Changed!!!
            rList.rooms.clear();
            for (Room re : rooms) {
                rList.withElement(re.roomOption);
            }
            try {
                for (ILobbyParticipant part : participants) {
                    if (part != p) {
                        part.UpdateRoomList(new ObjectMapper().writeValueAsString(rList));
                    }
                }
            } catch (JsonProcessingException jpException) {
                jpException.printStackTrace();
            }
            p.LeaveLobby(this);
            r.Add(p.GetClient());
            participants.remove(p);
            // Lobby Member Changed!!!
        } else if (m.substring(0, 4).equals("join")) {
            int i = Integer.parseInt(m.substring(5));
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
        try {
            cl.UpdateRoomList(new ObjectMapper().writeValueAsString(rList));
        } catch (JsonProcessingException jpException) {
            jpException.printStackTrace();
        }
        // Joined Lobby!!!
        // Lobby Member Changed!!!
    }
}