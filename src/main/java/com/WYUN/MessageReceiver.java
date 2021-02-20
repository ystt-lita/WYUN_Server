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
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public class MessageReceiver extends Thread {
    List<IReceivedMessageEventListener> listeners;
    BufferedReader buff;
    Client source;
    ReceivedMessageEvent e;

    public void close() {
        try {
            buff.close();
            System.out.println("buff closed.");
            join();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        }
    }

    public MessageReceiver(Client c, Socket s) {
        source = c;
        listeners = new LinkedList<IReceivedMessageEventListener>();

        try {
            buff = new BufferedReader(new InputStreamReader(s.getInputStream(), "UTF-8"));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void AddEventListener(IReceivedMessageEventListener listener) {
        synchronized (listeners) {
            listeners.add(listener);
        }
    }

    public void RemoveEventListener(IReceivedMessageEventListener listener) {
        synchronized (listeners) {
            if (listeners.contains(listener)) {
                listeners.remove(listener);
            }
        }
    }

    class ListenerDispatcher extends Thread {
        ReceivedMessageEvent event;
        IReceivedMessageEventListener listener;

        public ListenerDispatcher(IReceivedMessageEventListener l, ReceivedMessageEvent e) {
            event = e;
            listener = l;
        }

        public void run() {
            listener.Dispatch(event);
        }
    }

    public void run() {
        while (true) {
            try {
                e = new ReceivedMessageEvent(source, buff.readLine().split("\r")[0]);
                System.out.println("received message: " + e.GetMessage());
                System.out.println("firing event.");
                synchronized (listeners) {
                    for (IReceivedMessageEventListener listener : listeners) {
                        new ListenerDispatcher(listener, e).start();
                    }
                }

            } catch (IOException ioException) {
                break;
            }
            if (e.GetMessage() == null || e.GetMessage().equals("exit")) {
                System.out.println("exit receiver loop.");
                break;
            }
        }

    }

    public String GetMessage() {
        if (this.isAlive())
            return null;
        try {
            return buff.readLine().split("\r")[0];

        } catch (IOException ioException) {
            ioException.printStackTrace();
            // System.exit(-1);
            return null;
        }
    }
}