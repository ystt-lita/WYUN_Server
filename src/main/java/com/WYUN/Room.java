package com.WYUN;

import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;

public class Room implements IReceivedMessageEventListener {
    // RoomOptions options;
    List<IRoomParticipant> participants;
    Lobby parentLobby;
    RoomOption roomOption;

    Room(String o, Lobby p) {
        System.out.println("optionJson: "+o);
        try {
            roomOption = new ObjectMapper().readValue(o, com.WYUN.RoomOption.class);
        } catch (JsonParseException jpException) {
            jpException.printStackTrace();
        }catch(JsonMappingException jmException){
            jmException.printStackTrace();
        }catch(IOException ioException){
            ioException.printStackTrace();
        }
        parentLobby = p;
        // options=DeserializeOptions(o);
        participants = new LinkedList<IRoomParticipant>();
    }

    public String GetName() {
        return roomOption.option.name;
    }

    public int GetLimit() {
        return roomOption.option.limit;
    }

    public void Dispatch(ReceivedMessageEvent event) {
        String[] m = event.GetMessage().split(",");
        IRoomParticipant p = (IRoomParticipant) event.GetSource();
        System.out.println("[Room]message received: " + m);
        if (m[0].equals("leave")) {
            p.LeaveRoom(this);
            parentLobby.Add(p.GetClient());
            participants.remove(p);
        } else if (m[0].equals("broad")) {
            for (IRoomParticipant participant : participants) {
                participant.TellMessage(p.GetClient().name, String.join(",", Arrays.asList(m).subList(1, m.length)));
            }
        } else if (m[0].equals("tell")) {
            for (IRoomParticipant participant : participants) {
                if (participant.GetClient().name.equals(m[1])) {
                    participant.TellMessage(p.GetClient().name,
                            String.join(",", Arrays.asList(m).subList(2, m.length)));
                    break;
                }
            }
        }
    }

    public void Add(IRoomParticipant p) {
        System.out.println("adding client");
        p.JoinRoom(this);
        participants.add(p);
        // Room Member Changed!!!
    }
}