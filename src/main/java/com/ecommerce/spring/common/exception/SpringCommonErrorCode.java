package com.ecommerce.spring.common.exception;

import com.ecommerce.shared.exception.ErrorCode;

public enum SpringCommonErrorCode implements ErrorCode {
    REQUEST_VALIDATION_FAILED(400, "请求数据格式验证失败");
    private int status;
    private String message;

    SpringCommonErrorCode(int status, String message) {
        this.status = status;
        this.message = message;
    }

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getCode() {
        return this.name();
    }
}
