package com.WYUN;

import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Room implements IReceivedMessageEventListener {
    // RoomOptions options;
    List<IRoomParticipant> participants;
    Lobby parentLobby;
    Option roomOption;

    Room(Option o, Lobby p) {
        System.out.println("optionJson: " + o);
        roomOption = o;
        parentLobby = p;
        // options=DeserializeOptions(o);
        participants = new LinkedList<IRoomParticipant>();
    }

    public int GetLimit() {
        return roomOption.limit;
    }

    public void Dispatch(ReceivedMessageEvent event) {
        String m = event.GetMessage();
        IRoomParticipant p = (IRoomParticipant) event.GetSource();
        System.out.println("[Room]message received: " + m);
        try {
            Query q = new ObjectMapper().readValue(m, Query.class);
            if (q.query.equals("leave")) {
                p.LeaveRoom(this);
                parentLobby.Add(p.GetClient());
                participants.remove(p);
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
                        if (participant.GetClient().name.equals(query.to)) {
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
            }
        } catch (JsonMappingException jmException) {
            jmException.printStackTrace();
        } catch (JsonParseException jpException) {
            jpException.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void Add(IRoomParticipant p) {
        System.out.println("adding client");
        p.JoinRoom(this);
        participants.add(p);
        // Room Member Changed!!!
    }
}