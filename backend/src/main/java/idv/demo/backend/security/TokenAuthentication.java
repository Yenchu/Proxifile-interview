package idv.demo.backend.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;

public class TokenAuthentication extends AbstractAuthenticationToken {

    private String token;

    public TokenAuthentication(String token) {
        super(null);
        this.token = token;
    }

    public String getToken() {

        return token;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }
}
