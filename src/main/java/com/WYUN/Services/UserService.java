package com.WYUN.Services;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

import com.WYUN.Utils.LoginResult;
import com.WYUN.Utils.LogoutResult;
import com.WYUN.Utils.LoginResult.ResultCode;
import com.WYUN.Workers.IUserWorker;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysql.cj.log.Log;

//
/**
 * WYUN User Service
 * 
 * login
 * 
 * logout
 * 
 * あとなに
 * 
 */
class UserQuery {
    public String type;
    public String name;
    public int userID;
}

class UserQueryResult {
    public boolean result;
    public int userID;
    public String cause;
}

public class UserService implements IUserService {
    class UserWorkerCommunicater implements IUserWorker {
        Socket s;
        BufferedWriter writer;
        BufferedReader reader;

        public UserWorkerCommunicater(String host) {
            try {
                s = new Socket(host, 8940);
                writer = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
                reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        public LoginResult login(String name) {

            try {
                writer.write("{\"type\":\"login\",\"name\":\"" + name + "\"}");
                writer.newLine();
                writer.flush();
                UserQueryResult result = new ObjectMapper().readValue(reader.readLine(), UserQueryResult.class);
                LoginResult r = new LoginResult().Succeed(result.result);
                if (result.result) {
                    return r.WithID(result.userID);
                }
                return r;
            } catch (JsonMappingException e) {// 送られてきたレスポンスが変
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (JsonProcessingException e) {// 送られてきたレスポンスが変
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {// socket closed?
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            // TODO call api/user/login
            // MEMO Publish by push
            return new LoginResult().Succeed(false);
        }

        public LogoutResult logout(int userID) {
            try {
                writer.write("{\"type\":\"logout\",\"userID\":" + userID + "}");
                writer.newLine();
                writer.flush();
                UserQueryResult result;
                result = new ObjectMapper().readValue(reader.readLine(), UserQueryResult.class);
                return new LogoutResult().Succeed(result.result);

            } catch (JsonMappingException e) {
                // TODO: handle exception
            } catch (JsonProcessingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            // TODO call api/user/logout
            return new LogoutResult().Succeed(false);
        }
    }

    // TODO Workerを直接持たない
    UserWorkerCommunicater currentWorker;

    public UserWorkerCommunicater getWorker() {
        // TODO ロードバランサから割り当てを受ける
        if (currentWorker != null) {
            return currentWorker;
        }
        return currentWorker = new UserWorkerCommunicater("localhost");
    }

    public UserWorkerCommunicater getWorker(int userID) {
        // TODO Userの所属するWorkerを探す
        // MEMO クライアントが自分でEndpointを持つ実装ならここがいらない(かも)
        return currentWorker;
    }

    public static void main(String[] args) {
        if (args.length == 0) {// if(args.length==1){
            UserService service = new UserService("localhost");
            // UserService service=new UserService(args[0]);

            try (ServerSocket s = new ServerSocket(8939)) {
                Socket c = s.accept();
                BufferedReader reader = new BufferedReader(new InputStreamReader(c.getInputStream()));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(c.getOutputStream()));
                String read;
                try {
                    while ((read = reader.readLine()) != null) {
                        UserQuery query = new ObjectMapper().readValue(read, UserQuery.class);
                        System.out.println("received query type:" + query.type);
                        switch (query.type) {
                            case "login":
                                LoginResult ir = service.requestLogin(query.name);
                                try {
                                    writer.write(String.format("{\"result\":%s,\"userID\":\"%s\"}",
                                            ir.getCode() == ResultCode.Success, ir.getID()));
                                } catch (NoSuchElementException e) {
                                    writer.write("{\"result\":false}");
                                } finally {
                                    writer.newLine();
                                    writer.flush();
                                }
                                break;

                            case "logout":
                                LogoutResult or = service.requestLogout(query.userID);
                                writer.write(
                                        String.format("{\"result\":%s}",
                                                or.getCode() == LogoutResult.ResultCode.Success));
                                writer.newLine();
                                writer.flush();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            /*
             * //////////// vvvv M O C K vvvv////////////
             * Scanner s = new Scanner(System.in);
             * LoginResult ir;
             * LogoutResult or;
             * String input;
             * System.out.println("login or logout");
             * while ((input = s.nextLine()) != null) {
             * switch (input) {
             * case "login":
             * System.out.println("user name?");
             * input = s.nextLine();
             * ir = service.requestLogin(input);
             * if (ir.getCode() == LoginResult.ResultCode.Success) {
             * System.out.println("login succeed. userID : " + ir.getID());
             * } else {
             * System.out.println("login failed.");
             * }
             * break;
             * case "logout":
             * System.out.println("user id?");
             * input = s.nextLine();
             * or = service.requestLogout(Integer.parseInt(input));
             * if (or.getCode() == LogoutResult.ResultCode.Success) {
             * System.out.println("logout succeed.");
             * } else {
             * System.out.println("logout failed.");
             * }
             * break;
             * default:
             * break;
             * }
             * System.out.println("login or logout");
             * }
             */
        }
    }

    UserService(String workerEndpointURL) {
        currentWorker = new UserWorkerCommunicater(workerEndpointURL);
    }

    /**
     * login to WYUN by name
     * <h4>Endpoint</h4>
     * <div>/api/user/login</div>
     * <p/>
     * <h4>Query Parameters</h4>
     * <div>name: {@code string}</div>
     * <div>specifies user name</div>
     * <p/>
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
    public LoginResult requestLogin(String name) {
        return getWorker().login(name);
    }

    /**
     * logout from WYUN by userID
     * <h4>Endpoint</h4>
     * <div>/api/user/logout</div>
     * <p/>
     * <h4>Query Parameters</h4>
     * <div>userID: {@code number}</div>
     * <div>specifies user to logout</div>
     * <p/>
     * 
     * @param userID
     * @return
     *         <p>
     *         {@code Result.Success} if user with the ID logged in.
     *         </p>
     *         <p>
     *         {@code Result.Failed} in other cases.
     *         </p>
     */
    public LogoutResult requestLogout(int userID) {
        return getWorker(userID).logout(userID);

    }
}

// MEMO Pub/Subでいけないか？