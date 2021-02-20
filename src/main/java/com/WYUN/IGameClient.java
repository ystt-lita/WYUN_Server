/***************************************************
 * Copyright 2020, 2021 EwdErna
 * LICENSE: ../../../../../LICENSE
 ***************************************************/
package com.WYUN;

public interface IGameClient {
    public void Exit(IReceivedMessageEventListener l);

    public void NotifyError(String errorMessage);
}
