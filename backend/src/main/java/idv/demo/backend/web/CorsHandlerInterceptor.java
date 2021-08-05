package idv.demo.backend.web;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CorsHandlerInterceptor implements HandlerInterceptor {

    public static final String ACCESS_CONTROL_AllOWED_ORIGINS = "Access-Control-Allow-Origin";
    public static final String ACCESS_CONTROL_AllOWED_METHODS = "Access-Control-Allow-Methods";
    public static final String ACCESS_CONTROL_AllOWED_HEADERS = "Access-Control-Allow-Headers";
    public static final String ACCESS_CONTROL_MAX_AGE = "Access-Control-Max-Age";

    @Override
    public boolean preHandle(
            HttpServletRequest request, HttpServletResponse response, Object handler
    )
            throws
            Exception {

        response.setHeader(ACCESS_CONTROL_AllOWED_ORIGINS, "*");
        response.setHeader(ACCESS_CONTROL_AllOWED_METHODS, "GET,POST,PUT,DELETE,OPTIONS");
        response.setHeader(ACCESS_CONTROL_AllOWED_HEADERS, "Content-Type,Content-Length,Authorization,Accept,X-Requested-With");
        response.setHeader(ACCESS_CONTROL_MAX_AGE, "86400");
        return true;
    }
}
