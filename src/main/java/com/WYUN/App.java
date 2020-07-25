package com.WYUN;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.net.Socket;
import java.util.Scanner;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Hello world!
 *
 */
public class App extends Thread {
    Socket c;
    BufferedReader r;
    BufferedWriter w;
    Scanner s;

    public static void main(String[] args) {
        if (args[0].equals("App")) {
            new App().start();
        } else if (args[0].equals("Server")) {
            new Server();
        }
    }

    App() {
        try {
            s = new Scanner(System.in);
            c = new Socket("wyun_wyun_1", 8929);
            r = new BufferedReader(new InputStreamReader(c.getInputStream(), "UTF-8"));
            w = new BufferedWriter(new OutputStreamWriter(c.getOutputStream(), "UTF-8"));
            new Thread(new Runnable() {
                public void run() {
                    while (true) {
                        try {
                            if (s.hasNext()) {
                                w.write(s.nextLine());
                                w.newLine();
                                w.flush();
                            } else {
                                break;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

            }).start();
        } catch (

        Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
        String m;
        try {
            while (true) {
                m = r.readLine();
                System.out.println("message received: " + m);
                if (m.equals("exit")) {
                    c.close();
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
