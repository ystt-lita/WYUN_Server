/***************************************************
 * Copyright 2020, 2021 EwdErna
 * LICENSE: ../../../../../LICENSE
 * This file is part of WYUN Server.
 * 
 *     WYUN Server is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     WYUN Server is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with WYUN Server.  If not, see <https://www.gnu.org/licenses/>.
 ***************************************************/
package com.WYUN;

import java.io.*;
import java.net.ServerSocket;
import java.util.*;

public class Server {
    ServerSocket s;
    HashMap<Long, Lobby> lobbies;

    // public static void main(String[] args) {
    // new Server();
    // }

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