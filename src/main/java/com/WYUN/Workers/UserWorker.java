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
        try (Connection _users = DriverManager.getConnection("url")) {
            users = _users;
            checkAlreadyLoggedIn = users.prepareStatement("Select name From users Where name = ?");// すでにログインしているか
            findUserWithID = users.prepareStatement("Select * From users Where UserID = ?");// UserIDからログイン済みユーザーを検索
            insertNewUser = users.prepareStatement("Insert users Set UserID = ? name = ?");// UserIDを振って追加
            removeExistingUser = users.prepareStatement("Delete From users Where UserID = ?");// UserIDで指定したユーザーを削除
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    int genID() {
        // TODO Generate unique ID
        return new Random().nextInt();
    }

    public LoginResult login(String name) {
        try {
            checkAlreadyLoggedIn.setString(1, name);
            if (checkAlreadyLoggedIn.execute()) {// すでにログインしているのでダメ

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

    public LogoutResult logout(int UserID) {
        try {
            findUserWithID.setInt(1, UserID);
            if (findUserWithID.execute()) {
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
