package com.WYUN;

import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;

public class Room implements IReceivedMessageEventListener {
    // RoomOptions options;
    List<IRoomParticipant> participants;
    Lobby parentLobby;
    CreateQuery roomOption;
    Members mList;

    Room(CreateQuery o, Lobby p) {
        try {
            System.out.println("optionJson: " + new ObjectMapper().writeValueAsString(o));
        } catch (JsonProcessingException jpe) {
        }
        roomOption = o;
        parentLobby = p;
        // options=DeserializeOptions(o);
        participants = new LinkedList<IRoomParticipant>();
        mList = new Members();
    }

    public int GetLimit() {
        return roomOption.limit;
    }

    public void Dispatch(ReceivedMessageEvent event) {
        String m = event.GetMessage();
        IRoomParticipant p = (IRoomParticipant) event.GetSource();
        System.out.println("[Room/" + roomOption.name + "]message received: " + m);
        if (m == null) {
            participants.remove(event.GetSource());
            RefleshMember();
            MemberUpdate();
            event.GetSource().close();
            return;
        }
        try {
            Query q = new ObjectMapper().readValue(m, Query.class);
            if (q.query.equals("leave")) {
                p.LeaveRoom(this);
                participants.remove(p);
                RefleshMember();
                MemberUpdate();
                parentLobby.Add(p.GetClient());
            } else if (q.query.equals("exit")) {
                p.Exit(this);
                participants.remove(p);
                RefleshMember();
                MemberUpdate();
            } else if (q.query.equals("broad")) {
                try {
                    BroadQuery query = new ObjectMapper().readValue(m, BroadQuery.class);
                    for (IRoomParticipant participant : participants) {
                        participant.TellMessage(p.GetClient().name, query.body);
                    }
                } catch (JsonMappingException jmException) {
                    jmException.printStackTrace();
                } catch (JsonParseException jpException) {
                    jpException.printStackTrace();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            } else if (q.query.equals("tell")) {
                try {
                    TellQuery query = new ObjectMapper().readValue(m, TellQuery.class);
                    for (IRoomParticipant participant : participants) {
                        if (participant.GetClient().name.equals(query.dest)) {
                            participant.TellMessage(p.GetClient().name, query.body);
                            break;
                        }
                    }
                } catch (JsonMappingException jmException) {
                    jmException.printStackTrace();

                } catch (JsonParseException jpException) {
                    jpException.printStackTrace();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            } else if (q.query.equals("roomOption")) {
                try {
                    for (IRoomParticipant participant : participants) {
                        participant.UpdateRoomOption(new ObjectMapper().writeValueAsString(roomOption));
                    }
                } catch (JsonProcessingException jpException) {
                    // TODO: handle exception
                }
            } else if (q.query.equals("roomMember")) {
                RefleshMember();
                p.UpdateRoomMember(new ObjectMapper().writeValueAsString(mList));
            }
        } catch (JsonMappingException jmException) {
            jmException.printStackTrace();
        } catch (JsonParseException jpException) {
            jpException.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    void RefleshMember() {
        mList.members.clear();
        for (IRoomParticipant p : participants) {
            mList.members.add(new Member().withName(p.GetClient().name));
        }
    }

    void MemberUpdate() {
        try {
            for (IRoomParticipant p : participants) {
                p.UpdateRoomMember(new ObjectMapper().writeValueAsString(mList));
            }
        } catch (JsonProcessingException jpException) {
            jpException.printStackTrace();
        }
    }

    public void Add(IRoomParticipant p) {
        System.out.println("[Room/" + roomOption.name + "]adding client");
        p.JoinRoom(this);
        participants.add(p);
        // Room Member Changed!!!
        RefleshMember();
        MemberUpdate();
    }
}