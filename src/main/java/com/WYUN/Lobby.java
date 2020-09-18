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
    RoomList rList;

    Lobby(long id) {
        appID = id;
        participants = new LinkedList<ILobbyParticipant>();
        rooms = new ArrayList<Room>();
        roomsMap = new HashMap<String, Room>();
        rList = new RoomList();
    }

    public void Dispatch(ReceivedMessageEvent event) {
        String m = event.GetMessage();
        try{
        Query query = new ObjectMapper().readValue(m, Query.class);
        ILobbyParticipant p = (ILobbyParticipant) event.GetSource();
        System.out.println("[Lobby/" + appID + "]:ReceivedMessage:" + m);
        if (query.query.equals("exit")) {
            p.ExitLobby(this);
            participants.remove(p);
            // TODO: ルームが空になったら削除
            // Loby Member Changed!!!
        } else if (query.query.equals("create")) {
            try {
                CreateQuery q = new ObjectMapper().readValue(m.substring(7), CreateQuery.class);
                Room r = new Room(q.option, this);
                roomsMap.put(q.name, r);
                rooms.add(r);
                // Room List Changed!!!
                rList.rooms.clear();
                roomsMap.forEach((k, v) -> {
                    rList.rooms.add(new CreateQuery().withName(k).withOption(v.roomOption));
                });
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
            } catch (JsonMappingException jmException) {
            } catch (JsonParseException jpException) {
            } catch (IOException ioException) {
            }
            // Lobby Member Changed!!!
        } else if (query.query.equals("join")) {
            try {
                JoinQuery q = new ObjectMapper().readValue(m, JoinQuery.class);
                // TODO: Roomのバージョン管理
                // 現状の実装では、クライアントからjoinの送信->ルームリストの更新->joinの受信、の可能性がある
                // この場合異なる部屋に入る可能性・OutOfBoundsの可能性がある
                p.LeaveLobby(this);
                roomsMap.get(q.name).Add(p.GetClient());
                participants.remove(p);
            } catch (JsonMappingException jmException) {
            } catch (JsonParseException jsException) {
            } catch (IOException ioException) {
            }
        }}
        catch(JsonMappingException jmException){}
        catch(JsonParseException jpException){}
        catch(IOException ioException){} 
    }

    public void Add(ILobbyParticipant cl) {
        cl.JoinLobby(this);
        participants.add(cl);
        try {
            cl.UpdateRoomList(new ObjectMapper().writeValueAsString(rList));
        } catch (JsonParseException jpException) {
            jpException.printStackTrace();
        }catch(JsonMappingException jmException){
            jmException.printStackTrace();
        }catch(IOException ioException){
            ioException.printStackTrace();
        }
        // Joined Lobby!!!
        // Lobby Member Changed!!!
    }
}