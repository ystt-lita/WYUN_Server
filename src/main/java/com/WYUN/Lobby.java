package com.WYUN;

import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;

public class Lobby implements IReceivedMessageEventListener {
    long appID;
    List<ILobbyParticipant> participants;
    List<Room> rooms;
    Map<String, Room> roomsMap;
    Members mList;
    RoomList rList;

    Lobby(long id) {
        appID = id;
        participants = new LinkedList<ILobbyParticipant>();
        rooms = new ArrayList<Room>();
        roomsMap = new HashMap<String, Room>();
        rList = new RoomList();
        mList = new Members();
    }

    public void Dispatch(ReceivedMessageEvent event) {
        System.out.println("[Lobby/" + appID + "]:Dispatching Message ");
        String m = event.GetMessage();
        if (m == null) {
            Client p = event.GetSource();
            participants.remove(p);
            MemberUpdate();
            p.close();
            return;
        }
        try {
            System.out.println("[Lobby/" + appID + "]:ReceivedMessage:" + m);
            Query query = new ObjectMapper().readValue(m, Query.class);
            ILobbyParticipant p = (ILobbyParticipant) event.GetSource();
            if (query.query.equals("exit")) {
                p.Exit(this);
                participants.remove(p);
                // TODO: ルームが空になったら削除
                // Loby Member Changed!!!
                MemberUpdate();
            } else if (query.query.equals("create")) {
                try {
                    CreateQuery q = new ObjectMapper().readValue(m, CreateQuery.class);
                    if (roomsMap.containsKey(q.name)) {
                        p.NotifyError("Already exist room with this name");
                        return;
                    }
                    Room r = new Room(q, this);
                    roomsMap.put(q.name, r);
                    rooms.add(r);
                    // Room List Changed!!!
                    RoomReflesh();
                    NotifyRoomUpdate(p);
                    p.LeaveLobby(this);
                    r.Add(p.GetClient());
                    participants.remove(p);
                } catch (JsonMappingException jmException) {
                } catch (JsonParseException jpException) {
                } catch (IOException ioException) {
                }
                // Lobby Member Changed!!!
                MemberUpdate();
            } else if (query.query.equals("join")) {
                try {
                    JoinQuery q = new ObjectMapper().readValue(m, JoinQuery.class);
                    // TODO: Roomのバージョン管理
                    // 現状の実装では、クライアントからjoinの送信->ルームリストの更新->joinの受信、の可能性がある
                    // この場合異なる部屋に入る可能性・OutOfBoundsの可能性がある
                    if (!roomsMap.containsKey(q.name)) {
                        p.NotifyError("No room with this name");
                        return;
                    }
                    p.LeaveLobby(this);
                    roomsMap.get(q.name).Add(p.GetClient());
                    participants.remove(p);
                } catch (JsonMappingException jmException) {
                } catch (JsonParseException jsException) {
                } catch (IOException ioException) {
                }
                MemberUpdate();
            } else if (query.query.equals("roomList")) {
                RoomReflesh();
                p.UpdateRoomList(new ObjectMapper().writeValueAsString(rList));
            }
        } catch (JsonMappingException jmException) {
        } catch (JsonParseException jpException) {
        } catch (IOException ioException) {
        }
    }

    public void Add(ILobbyParticipant cl) {
        cl.JoinLobby(this);
        participants.add(cl);
        try {
            cl.UpdateRoomList(new ObjectMapper().writeValueAsString(rList));
        } catch (JsonParseException jpException) {
            jpException.printStackTrace();
        } catch (JsonMappingException jmException) {
            jmException.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        // Joined Lobby!!!
        // Lobby Member Changed!!!
        MemberUpdate();
    }

    void RoomReflesh() {
        rList.rooms.clear();
        roomsMap.forEach((k, v) -> {
            rList.rooms.add(v.roomOption);
        });
    }

    void NotifyRoomUpdate(ILobbyParticipant except) {
        try {
            for (ILobbyParticipant part : participants) {
                if (except != part) {
                    part.UpdateRoomList(new ObjectMapper().writeValueAsString(rList));
                }
            }
        } catch (JsonProcessingException jpException) {
            jpException.printStackTrace();
        }
    }

    void MemberUpdate() {
        mList.members.clear();
        for (ILobbyParticipant part : participants) {
            mList.members.add(new Member().withName(part.GetClient().name));
        }
        try {
            for (ILobbyParticipant part : participants) {
                part.UpdateLobbyMember(new ObjectMapper().writeValueAsString(mList));
            }
        } catch (JsonProcessingException jpException) {
            jpException.printStackTrace();
        }
    }
}