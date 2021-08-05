package idv.demo.backend.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import idv.demo.backend.web.RestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

public class UnAuthEntryPoint implements AuthenticationEntryPoint {

    private static final Logger log = LoggerFactory.getLogger(UnAuthEntryPoint.class);

    private ObjectMapper objectMapper;

    public UnAuthEntryPoint(ObjectMapper objectMapper) {

        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(
            HttpServletRequest request, HttpServletResponse response, AuthenticationException e)
            throws IOException, ServletException {

        log.error(e.getMessage(), e);
        HttpStatus status = HttpStatus.UNAUTHORIZED;

        RestResponse body = RestResponse.builder().code(status.value()).msg(status.getReasonPhrase()).build();
        String json = objectMapper.writeValueAsString(body);

        PrintWriter out = response.getWriter();
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setStatus(status.value());

        out.print(json);
        out.flush();
    }
}
