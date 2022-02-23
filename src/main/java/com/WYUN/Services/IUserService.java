package com.WYUN.Services;

import com.WYUN.Utils.LoginResult;
import com.WYUN.Utils.LogoutResult;

public interface IUserService {
    public LoginResult requestLogin(String name);

    public LogoutResult requestLogout(int UserID);
}
