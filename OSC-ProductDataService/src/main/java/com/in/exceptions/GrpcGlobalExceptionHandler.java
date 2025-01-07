package com.in.exceptions;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import net.devh.boot.grpc.server.advice.GrpcAdvice;
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
@GrpcAdvice
public class GrpcGlobalExceptionHandler {
    private final Logger log = LogManager.getLogger(GrpcGlobalExceptionHandler.class);

    @GrpcExceptionHandler(NullPointerException.class)
    public StatusRuntimeException resourceNotFoundException(NullPointerException ex) {
        log.error("Resource not found: {}", ex.getMessage());
        return Status.NOT_FOUND.withDescription(ex.getMessage())
                .withCause(ex)
                .asRuntimeException();
    }
    // Handle All Exception
    @GrpcExceptionHandler(Exception.class)
    public StatusRuntimeException handleGenericException(Exception ex) {
        log.error("Unexpected grpc error: {}", ex.getMessage());
        return Status.INTERNAL
                .withDescription(ex.getMessage())
                .withCause(ex)
                .asRuntimeException();
    }
}
