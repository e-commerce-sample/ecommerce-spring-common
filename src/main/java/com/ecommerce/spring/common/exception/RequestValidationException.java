package com.ecommerce.spring.common.exception;

import com.ecommerce.shared.exception.AppException;

import java.util.Map;

public class RequestValidationException extends AppException {
    public RequestValidationException(Map<String, Object> data) {
        super(SpringCommonErrorCode.REQUEST_VALIDATION_FAILED, data);
    }
}
