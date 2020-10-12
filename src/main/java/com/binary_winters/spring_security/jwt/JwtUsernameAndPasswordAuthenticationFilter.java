package com.binary_winters.spring_security.jwt;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

// The job of this class is to verify the credentials. Spring Security does it by default but we can 
// have our own implementation.
public class JwtUsernameAndPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtConfig jwtConfig;
    private final SecretKey secretKey;

    public JwtUsernameAndPasswordAuthenticationFilter(
    		AuthenticationManager authenticationManager, 
    		JwtConfig jwtConfig, 
    		SecretKey secretKey) {
        this.authenticationManager = authenticationManager;
        this.jwtConfig = jwtConfig;
        this.secretKey = secretKey;
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

	@Override
	// Will be invoked after attemptAuthentication is successful.
	// It creates the token and send it to the client.
	protected void successfulAuthentication(
			HttpServletRequest request, 
			HttpServletResponse response, 
			FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		
		// Generates the token
        String token = Jwts.builder()
                .setSubject(authResult.getName())
                .claim("authorities", authResult.getAuthorities()) // Specify the body
                .setIssuedAt(new Date())
                .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(jwtConfig.getTokenExpirationAfterDays())))
                .signWith(secretKey)
                .compact();

        // Sends the token to the client
        response.addHeader(jwtConfig.getAuthorizationHeader(), jwtConfig.getTokenPrefix() + token);
	}
	
}