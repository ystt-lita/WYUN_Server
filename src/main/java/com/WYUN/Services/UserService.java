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
