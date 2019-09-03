package com.ecommerce.spring.common.exception;

import com.ecommerce.shared.exception.AppException;
import com.ecommerce.shared.exception.ErrorCode;

import java.time.Instant;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;
import static org.apache.commons.collections4.MapUtils.isEmpty;

class ErrorRepresentation {
    private final ErrorDetail error;

    ErrorRepresentation(AppException ex, String path) {
        ErrorCode error = ex.getError();
        this.error = new ErrorDetail(error.getCode(), error.getStatus(), error.getMessage(), path, ex.getData());
    }

    ErrorRepresentation(ErrorDetail error) {
        this.error = error;
    }

    ErrorDetail getError() {
        return error;
    }

    static class ErrorDetail {
        private final String code;
        private final int status;
        private final String message;
        private final String path;
        private final Instant timestamp;
        private final Map<String, Object> data = newHashMap();

        public ErrorDetail(String code, int status, String message, String path, Map<String, Object> data) {
            this.code = code;
            this.status = status;
            this.message = message;
            this.path = path;
            this.timestamp = Instant.now();
            if (!isEmpty(data)) {
                this.data.putAll(data);
            }
        }

        public int getStatus() {
            return status;
        }
    }
}
