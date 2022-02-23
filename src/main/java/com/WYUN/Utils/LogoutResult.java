package com.WYUN.Utils;

public class LogoutResult {
    public enum ResultCode {
        Failed,
        Success;
    }

    private ResultCode code;

    public LogoutResult WithCode(ResultCode c) {
        code = c;
        return this;
    }

    public ResultCode getCode() {
        return code;
    }
}