package com.WYUN;

import java.util.EventListener;

public interface IReceivedMessageEventListener extends EventListener {
    public void Dispatch(ReceivedMessageEvent event);
}