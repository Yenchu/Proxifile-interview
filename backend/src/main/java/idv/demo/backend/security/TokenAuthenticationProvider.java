package idv.demo.backend.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class TokenAuthenticationProvider implements AuthenticationProvider {

  @Override
  public boolean supports(Class<?> authentication) {

    return authentication.equals(TokenAuthentication.class);
  }

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {

    TokenAuthentication authToken = (TokenAuthentication) authentication;

    // TODO: replace the following test code with real authenticate logic
    if (!"faketoken".equals(authToken.getToken())) {
      throw new BadCredentialsException("Token is invalid");
    }
    return authToken;
  }
}
