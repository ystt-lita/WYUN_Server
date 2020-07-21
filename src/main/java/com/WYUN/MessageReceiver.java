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
            buff = new BufferedReader(new InputStreamReader(s.getInputStream()));
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

    public void run() {
        while (true) {
            try {
                e = new ReceivedMessageEvent(source, buff.readLine());
                System.out.println("received message: " + e.GetMessage());
                System.out.println("firing event.");
                synchronized (listeners) {
                    for (IReceivedMessageEventListener listener : listeners) {
                        new Thread(() -> {
                            listener.Dispatch(e);
                        }).start();
                    }
                }
                if (e.GetMessage().equals("exit")) {
                    System.out.println("exit receiver loop.");
                    break;
                }

            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }

    }

    public String GetMessage() {
        if (this.isAlive())
            return null;
        try {
            return buff.readLine();
        } catch (IOException ioException) {
            ioException.printStackTrace();
            // System.exit(-1);
            return null;
        }
    }
}