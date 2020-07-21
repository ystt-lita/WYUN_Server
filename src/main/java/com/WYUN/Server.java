package com.WYUN;

import java.io.*;
import java.net.ServerSocket;
import java.util.*;

public class Server {
    ServerSocket s;
    HashMap<Long, Lobby> lobbies;

    public static void main(String[] args) {
        new Server();
    }

    Server() {
        lobbies = new HashMap<Long, Lobby>();
        run();
    }

    void run() {
        try {
            s = new ServerSocket(8929);
            System.out.println("[Server]started server on port 8929.");
        } catch (IOException ioException) {
            System.out.println("[Server]cannot start server.");
            ioException.printStackTrace();
        }
        while (true) {
            try {
                Client cl = new Client(s.accept());
                System.out.println("[Server]accepted new client.");
                if (!lobbies.containsKey(cl.appID)) {
                    lobbies.put(cl.appID, new Lobby(cl.appID));
                    System.out.println("[Server]created new lobby: " + cl.appID);
                }
                lobbies.get(cl.appID).Add(cl);
            } catch (IOException ioException) {
                System.out.println("[Server]error occured on connecting to client.");
                ioException.printStackTrace();
            }
        }
    }
}