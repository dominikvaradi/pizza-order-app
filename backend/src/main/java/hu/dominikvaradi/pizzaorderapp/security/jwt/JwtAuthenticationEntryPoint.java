package hu.dominikvaradi.pizzaorderapp.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.dominikvaradi.pizzaorderapp.data.dto.MessageResponseDTO;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        MessageResponseDTO responseObject = new MessageResponseDTO("Error: Unauthorized!");
        ObjectMapper mapper = new ObjectMapper();
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, mapper.writeValueAsString(responseObject));
    }
}
