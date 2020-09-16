package com.upgrade.challenge.commonservicelibrary.interceptor;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.upgrade.challenge.commonservicelibrary.constant.CommonVariable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class RequestInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(RequestInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String trackingId = request.getHeader(CommonVariable.TRACKING_ID_HEADER);

        if (trackingId == null || trackingId.isEmpty()) {
            trackingId = UUID.randomUUID().toString();
        }
        request.setAttribute(CommonVariable.TRACKING_ID_HEADER, trackingId);
        response.setHeader(CommonVariable.TRACKING_ID_HEADER, trackingId);
        log.info("Request uri: {}, method: {}, trackingId: {}", request.getRequestURI(), request.getMethod(),
                response.getHeader(CommonVariable.TRACKING_ID_HEADER));

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object object, Exception arg3)
            throws Exception {
        log.info("Response uri: {}, method: {}, trackingId: {}, status: {}", request.getRequestURI(),
                request.getMethod(), response.getHeader(CommonVariable.TRACKING_ID_HEADER), response.getStatus());
    }
}
