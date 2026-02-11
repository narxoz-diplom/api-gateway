package com.microservices.apigateway.config;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.net.ConnectException;
import java.util.Map;

/**
 * Maps connection failures (e.g. downstream service not running) to 503 Service Unavailable
 * instead of 500, so the client gets a clear "service unavailable" response.
 */
@Component
public class GatewayErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        Map<String, Object> map = super.getErrorAttributes(request, options);
        Throwable error = getError(request);
        if (isConnectionFailure(error)) {
            map.put("status", 503);
            map.put("error", "Service Unavailable");
            map.put("message", "Auth or other backend service is not available. Ensure the service is running.");
        }
        return map;
    }

    private static boolean isConnectionFailure(Throwable t) {
        while (t != null) {
            if (t instanceof ConnectException) {
                return true;
            }
            if (t.getClass().getName().contains("ConnectException")
                    || (t.getMessage() != null && t.getMessage().contains("Connection refused"))) {
                return true;
            }
            t = t.getCause();
        }
        return false;
    }
}
