package com.ecommerce.common.exception;

//This is to be subclassed by concrete enums
public interface ErrorCode {
    int getStatus();

    String getMessage();

    String getCode();
}
