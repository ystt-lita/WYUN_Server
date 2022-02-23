package com.WYUN.Utils;

import java.util.NoSuchElementException;
import java.util.Optional;

public class LoginResult {
    public enum ResultCode {
        Failed,
        Success;
    }

    private Optional<Integer> ID;

    private ResultCode code;

    public LoginResult WithCode(ResultCode c) {
        code = c;
        return this;
    }

    public LoginResult WithID(int id) {
        ID = Optional.of(id);
        return this;
    }

    public ResultCode getCode() {
        return code;
    }

    public int getID() throws NoSuchElementException {
        try {
            return ID.get();
        } catch (NoSuchElementException e) {
            // TODO ユーザー側の責任でよい気がするが一応
            throw e;
        }
    }
}