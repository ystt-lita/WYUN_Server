package com.WYUN.Workers;

import com.WYUN.Utils.LoginResult;
import com.WYUN.Utils.LogoutResult;

public interface IUserWorker {
    public LoginResult login(String name);

    public LogoutResult logout(int UserID);
}