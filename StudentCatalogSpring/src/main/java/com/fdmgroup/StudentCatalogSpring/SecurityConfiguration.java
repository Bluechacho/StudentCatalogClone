package com.fdmgroup.StudentCatalogSpring;

import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.authentication.builders.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.crypto.bcrypt.*;
import org.springframework.security.crypto.password.*;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //Student role account
		auth.inMemoryAuthentication()
            .passwordEncoder(passwordEncoder())
            .withUser("ima.student")
            .password(passwordEncoder().encode("password"))
            .roles("STUDENT");

        //Professor role account
		auth.inMemoryAuthentication()
            .passwordEncoder(passwordEncoder())
            .withUser("ima.professsor")
            .password(passwordEncoder().encode("password"))
            .roles("PROFESSOR");

        //Admin role account
		auth.inMemoryAuthentication()
            .passwordEncoder(passwordEncoder())
            .withUser("ima.admin")
            .password(passwordEncoder().encode("password"))
            .roles("ADMIN");
    }
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return passwordEncoder;
	}
}