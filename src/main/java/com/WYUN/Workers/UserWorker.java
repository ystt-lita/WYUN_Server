package com.WYUN.Workers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

// import com.WYUN.Services.IUserService;
import com.WYUN.Utils.LoginResult;
import com.WYUN.Utils.LogoutResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

class UserQuery {
    public String type;
    public String name;
    public int userID;
}

public class UserWorker implements IUserWorker/* , IUserService */ {

    PreparedStatement checkAlreadyLoggedIn;
    PreparedStatement findUserWithID;
    PreparedStatement insertNewUser;
    PreparedStatement removeExistingUser;
    Connection users;

    class Listener implements Runnable {
        Socket c;
        BufferedReader reader;
        BufferedWriter writer;

        /**
         * 
         * @param _c Socket for service input
         * @throws IOException if unable to get InputStream from Socket
         */
        public Listener(Socket _c) throws IOException {
            c = _c;
            writer = new BufferedWriter(new OutputStreamWriter(c.getOutputStream()));
            reader = new BufferedReader(new InputStreamReader(c.getInputStream()));
        }

        @Override
        public void run() {
            String read;
            try {
                while ((read = reader.readLine()) != null) {
                    UserQuery query = new ObjectMapper().readValue(read, UserQuery.class);
                    System.out.println("received query tipe:" + query.type);
                    switch (query.type) {
                        case "login":
                            LoginResult ir = login(query.name);
                            switch (ir.getCode()) {
                                case Success:
                                    writer.write(String.format("{\"result\":true,\"userID\":%d}", ir.getID()));
                                    break;
                                case Failed:
                                    writer.write("{\"result\":false,\"cause\":\"not provided\"}");
                                    // TODO provide cause of failer
                                    break;
                            }
                            break;
                        case "logout":
                            LogoutResult or = logout(query.userID);
                            switch (or.getCode()) {
                                case Success:
                                    writer.write("{\"result\":true}");
                                    break;

                                case Failed:
                                    writer.write("{\"result\":false,\"cause\":\"not provided\"}");
                                    // TODO provide cause of failer
                                    break;
                            }
                            break;
                    }
                    writer.newLine();
                    writer.flush();

                }
            } catch (JsonMappingException e) { // readValue failed
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (JsonProcessingException e) { // readValue failed
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) { // readLine failed
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        if (args.length == 0) {// if (args.length == 1) {
            UserWorker worker;
            try {
                worker = new UserWorker("wyun_server_db_1:3306");
                // new UserWorker(args[0]);
                try (ServerSocket s = new ServerSocket(8940)) {
                    while (true) {
                        System.out.println("waiting connection...");
                        new Thread(worker.new Listener(s.accept())).start();
                        System.out.println("accepted.");
                    }
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } catch (SQLException e1) {
                System.err.println("cannot connect db. exit");
                e1.printStackTrace();
                System.exit(-1);
                // TODO Auto-generated catch block
            }
            return;
        }
    }

    public UserWorker(String sqlURL) throws SQLException {
        users = DriverManager.getConnection("jdbc:mysql://" + sqlURL + "/userservice", "root", "root");
        // users = _users;
        checkAlreadyLoggedIn = users.prepareStatement("Select name From users Where name = ?;");// すでにログインしているか
        findUserWithID = users.prepareStatement("Select * From users Where userID = ?;");// userIDからログイン済みユーザーを検索
        insertNewUser = users.prepareStatement("Insert users Values (?, ?);");// userIDを振って追加
        removeExistingUser = users.prepareStatement("Delete From users Where userID = ?;");// userIDで指定したユーザーを削除

    }

    int genID() {
        // TODO Generate unique ID
        return new Random().nextInt();
    }

    /**
     * login to WYUN by name
     * 
     * @param name not {@code null}
     * @return
     *         <p>
     *         {@code Result.Success} if no user logged in with specified name.
     *         userID will be provided.
     *         </p>
     *         <p>
     *         {@code Result.Failed} in other cases. userID does not provided.
     *         </p>
     */
    public LoginResult login(String name) {
        try {
            checkAlreadyLoggedIn.setString(1, name);
            if (checkAlreadyLoggedIn.executeQuery().next()) {// すでにログインしているのでダメ

            } else {
                int id = genID();
                insertNewUser.setInt(1, id);
                insertNewUser.setString(2, name);
                insertNewUser.executeUpdate();
                // TODO response to API call
                return new LoginResult().Succeed(true).WithID(id);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // TODO response to API call
        return new LoginResult().Succeed(false);

    }

    /**
     * logout from WYUN by userID
     * 
     * @param userID provided via LoginResult
     * @return
     *         <p>
     *         {@code Result.Success} if exists user logged in with specified
     *         userID.
     *         </p>
     *         <p>
     *         {@code Result.Failed} in other cases.
     *         </p>
     */
    public LogoutResult logout(int userID) {
        try {
            findUserWithID.setInt(1, userID);
            if (findUserWithID.executeQuery().next()) {
                removeExistingUser.setInt(1, userID);
                removeExistingUser.executeUpdate();
                // TODO response to API call
                return new LogoutResult().Succeed(true);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // TODO response to API call
        return new LogoutResult().Succeed(false);
    }

    // public LoginResult requestLogin(String name) {
    // return login(name);
    // }

    // public LogoutResult requestLogout(int userID) {
    // return logout(userID);
    // }
}
