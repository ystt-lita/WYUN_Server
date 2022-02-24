package com.WYUN.Workers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

// import com.WYUN.Services.IUserService;
import com.WYUN.Utils.LoginResult;
import com.WYUN.Utils.LogoutResult;

public class UserWorker implements IUserWorker/* , IUserService */ {
    PreparedStatement checkAlreadyLoggedIn;
    PreparedStatement findUserWithID;
    PreparedStatement insertNewUser;
    PreparedStatement removeExistingUser;
    Connection users;

    public UserWorker() {
        try {
            users = DriverManager.getConnection("jdbc:mysql://wyun_server_db_1:3306/userservice", "root", "root");
            // users = _users;
            checkAlreadyLoggedIn = users.prepareStatement("Select name From users Where name = ?;");// すでにログインしているか
            findUserWithID = users.prepareStatement("Select * From users Where UserID = ?;");// UserIDからログイン済みユーザーを検索
            insertNewUser = users.prepareStatement("Insert users Values (?, ?);");// UserIDを振って追加
            removeExistingUser = users.prepareStatement("Delete From users Where UserID = ?;");// UserIDで指定したユーザーを削除
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    int genID() {
        // TODO Generate unique ID
        return new Random().nextInt();
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
     *         UserID will be provided.
     *         </p>
     *         <p>
     *         {@code Result.Failed} in other cases. UserID does not provided.
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
                return new LoginResult().WithCode(LoginResult.ResultCode.Success).WithID(id);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // TODO response to API call
        return new LoginResult().WithCode(LoginResult.ResultCode.Failed);

    }

    /**
     * logout from WYUN by UserID
     * <h4>Endpoint</h4>
     * <div>/api/user/logout</div>
     * <p/>
     * <h4>Query Parameters</h4>
     * <div>UserID: {@code number}</div>
     * <div>specifies user to logout</div>
     * <p/>
     * 
     * @param UserID provided via LoginResult
     * @return
     *         <p>
     *         {@code Result.Success} if exists user logged in with specified
     *         UserID.
     *         </p>
     *         <p>
     *         {@code Result.Failed} in other cases.
     *         </p>
     */
    public LogoutResult logout(int UserID) {
        try {
            findUserWithID.setInt(1, UserID);
            if (findUserWithID.executeQuery().next()) {
                removeExistingUser.setInt(1, UserID);
                removeExistingUser.executeUpdate();
                // TODO response to API call
                return new LogoutResult().WithCode(LogoutResult.ResultCode.Success);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // TODO response to API call
        return new LogoutResult().WithCode(LogoutResult.ResultCode.Failed);
    }

    // public LoginResult requestLogin(String name) {
    // return login(name);
    // }

    // public LogoutResult requestLogout(int UserID) {
    // return logout(UserID);
    // }
}
