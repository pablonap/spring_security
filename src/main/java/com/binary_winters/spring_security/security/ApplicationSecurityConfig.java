package com.binary_winters.spring_security.security;

import static com.binary_winters.spring_security.security.ApplicationUserRole.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {
	
	private final PasswordEncoder passwordEncoder;
	
	@Autowired
	// The passwordEncoder input parameter is the object generated in PasswordConfig class.
    public ApplicationSecurityConfig(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	@Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/", "index", "/css/*", "/js/*").permitAll()
                .antMatchers("/api/**").hasRole(STUDENT.name())
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic();
    }
    
    @Override
    @Bean
    // This method indicates how I do retrieve the user from the db.
    protected UserDetailsService userDetailsService() {
    	UserDetails lucaUser = User.builder()
    		.username("annasmith")
    		.password(passwordEncoder.encode("annasmith123"))
			.roles(STUDENT.name()) // ROLE_STUDENT
    		.build();
    	
		UserDetails lindaUser = User.builder()
                .username("linda")
                .password(passwordEncoder.encode("linda123"))
                .roles(ADMIN.name()) // ROLE_ADMIN
                .build();

        return new InMemoryUserDetailsManager(
                lucaUser,
                lindaUser
        ); 
    	 
    }

}