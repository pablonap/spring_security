package com.binary_winters.spring_security.jwt;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

// The job of this class is to verify the credentials. Spring Security does it by default but we can 
// have our own implementation.
public class JwtUsernameAndPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    public JwtUsernameAndPasswordAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

	@Override
	// Take client's credentials and validate them.
	public Authentication attemptAuthentication(
			HttpServletRequest request, 
			HttpServletResponse response)
			throws AuthenticationException {
		
        try {
			// Grab username and password sent by the client.
            UsernameAndPasswordAuthenticationRequest authenticationRequest = new ObjectMapper()
                    .readValue(request.getInputStream(), UsernameAndPasswordAuthenticationRequest.class);
            
            Authentication authentication = 
            		new UsernamePasswordAuthenticationToken(
            				authenticationRequest.getUsername(), 
            				authenticationRequest.getPassword());

            // authenticationManager assures that username exists and then check the password.
            Authentication authenticate = authenticationManager.authenticate(authentication);
            
            return authenticate;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
		
	}
	
}