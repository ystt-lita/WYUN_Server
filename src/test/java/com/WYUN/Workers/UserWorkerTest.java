package com.WYUN.Workers;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.WYUN.Utils.LoginResult;
import com.WYUN.Utils.LogoutResult;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserWorkerTest {
    static UserWorker testWorker;
    static List<LoginResult> results;

    @BeforeClass
    public static void init() {
        try (Connection p = DriverManager.getConnection("jdbc:mysql://wyun_server_db_1:3306/userservice", "root",
                "root")) {
            p.createStatement().executeUpdate("Delete From users;");
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        testWorker = new UserWorker();
        results = new ArrayList<>();
    }

    @Test
    public void Test1_LogoutWithoutLoggingIN() {
        assertEquals(LogoutResult.ResultCode.Failed, testWorker.logout(444).getCode());
    }

    @Test
    public void Test2_Login() {
        LoginResult tmp = testWorker.login("user");
        results.add(tmp);
        assertEquals(LoginResult.ResultCode.Success, tmp.getCode());
        tmp = testWorker.login("aaa");
        results.add(tmp);
        assertEquals(LoginResult.ResultCode.Success, tmp.getCode());
        tmp = testWorker.login("momo");
        results.add(tmp);
        assertEquals(LoginResult.ResultCode.Success, tmp.getCode());
        tmp = testWorker.login("yt");
        results.add(tmp);
        assertEquals(LoginResult.ResultCode.Success, tmp.getCode());
    }

    @Test
    public void Test3_MultipleLogin() {
        assertEquals(LoginResult.ResultCode.Failed, testWorker.login("user").getCode());
    }

    @Test
    public void Test4_Logout() {
        LoginResult tmp = results.get(results.size() - 1);
        results.remove(tmp);
        assertEquals(LogoutResult.ResultCode.Success, testWorker.logout(tmp.getID()).getCode());
        assertEquals(LogoutResult.ResultCode.Failed, testWorker.logout(tmp.getID()).getCode());
        tmp = results.get(results.size() - 1);
        results.remove(tmp);
        assertEquals(LogoutResult.ResultCode.Success, testWorker.logout(tmp.getID()).getCode());
        tmp = results.get(results.size() - 1);
        results.remove(tmp);
        assertEquals(LogoutResult.ResultCode.Success, testWorker.logout(tmp.getID()).getCode());
        tmp = results.get(results.size() - 1);
        results.remove(tmp);
        assertEquals(LogoutResult.ResultCode.Success, testWorker.logout(tmp.getID()).getCode());

    }
}
