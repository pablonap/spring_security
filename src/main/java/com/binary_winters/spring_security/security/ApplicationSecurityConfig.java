package com.binary_winters.spring_security.security;

import static com.binary_winters.spring_security.security.ApplicationUserRole.*;
import static com.binary_winters.spring_security.security.ApplicationUserPermission.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
        		.csrf().disable()
                .authorizeRequests()
                .antMatchers("/", "index", "/css/*", "/js/*").permitAll()
                .antMatchers("/api/**").hasRole(STUDENT.name())
                .antMatchers(HttpMethod.DELETE, "/management/api/**").hasAuthority(COURSE_WRITE.getPermission())
                .antMatchers(HttpMethod.POST, "/management/api/**").hasAuthority(COURSE_WRITE.getPermission())
                .antMatchers(HttpMethod.PUT, "/management/api/**").hasAuthority(COURSE_WRITE.getPermission())
                .antMatchers("/management/api/**").hasAnyRole(ADMIN.name(), ADMINTRAINEE.name()) // The order matters here cause I'm not specifying the type of request.
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic();
    }
    
    @Override
    @Bean
    // This method indicates how I do retrieve the user from the db.
    protected UserDetailsService userDetailsService() {
    	/*
    	 * In UserDetails there is no concept of role or permissions and that is bundle inside the collection 
    	 * getAuthorities which returns any type wich extends GrantedAuthority.

		 * A User (from Spring of type UserDetails) has a list of Roles of type GrantedAuthority and each of 
		 * its roles is an implementation of that interface of type SimpleGrantedAuthority (public UserBuilder 
		 * roles(String... roles) method).
    	 */
    	UserDetails annaSmithUser = User.builder()
    		.username("annasmith")
    		.password(passwordEncoder.encode("annasmith123"))
//			.roles(STUDENT.name()) // ROLE_STUDENT
    		.authorities(STUDENT.getGrantedAuthorities())
    		.build();
    	
		UserDetails lindaUser = User.builder()
                .username("linda")
                .password(passwordEncoder.encode("linda123"))
//                .roles(ADMIN.name()) // ROLE_ADMIN
                .authorities(ADMIN.getGrantedAuthorities())
                .build();

        UserDetails tomUser = User.builder()
                .username("tom")
                .password(passwordEncoder.encode("tom123"))
//                .roles(ADMINTRAINEE.name()) // ROLE_ADMINTRAINEE
                .authorities(ADMINTRAINEE.getGrantedAuthorities())
                .build();

        return new InMemoryUserDetailsManager(
                annaSmithUser,
                lindaUser,
                tomUser
        );
    	 
    }

}