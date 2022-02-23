package com.WYUN.Services;

import com.WYUN.Utils.LoginResult;
import com.WYUN.Utils.LogoutResult;
import com.WYUN.Workers.IUserWorker;

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

public class UserService implements IUserService {
    class UserWorkerGateway implements IUserWorker {

        public LoginResult login(String name) {
            // TODO call api/user/login
            // MEMO Publish by push
            return new LoginResult().WithCode(LoginResult.ResultCode.Failed);
        }

        public LogoutResult logout(int UserID) {
            // TODO call api/user/logout
            return new LogoutResult().WithCode(LogoutResult.ResultCode.Failed);
        }
    }

    // TODO Workerを直接持たない
    UserWorkerGateway currentWorker;

    public UserWorkerGateway getWorker() {
        // TODO ラウンドロビンで割り当て
        if (currentWorker != null) {
            return currentWorker;
        }
        return currentWorker = new UserWorkerGateway();
    }

    public UserWorkerGateway getWorker(int userID) {
        // TODO Userの所属するWorkerを探す(なければラウンドロビンで割り当て。ないことある？)
        // MEMO クライアントが自分でEndpointを持つ実装ならここがいらない(かも)
        return currentWorker;
    }

    public LoginResult requestLogin(String name) {
        return getWorker().login(name);
    }

    public LogoutResult requestLogout(int UserID) {
        return getWorker(UserID).logout(UserID);

    }
}
