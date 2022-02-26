package com.WYUN.Utils;

public class LogoutResult {
    public enum ResultCode {
        Failed,
        Success;

        public static ResultCode toCode(boolean c) {
            return c ? Success : Failed;
        }
    }

    private ResultCode code;

    public LogoutResult Succeed(boolean c) {
        code = ResultCode.toCode(c);
        return this;
    }

    public ResultCode getCode() {
        return code;
    }
}