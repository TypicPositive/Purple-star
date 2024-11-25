package com.purpleWeb.config;
import java.io.IOException;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class SecurityConfig {

	@Autowired
	private AuthenticationSuccessHandler authenticationSuccessHandler;
	
	@Autowired
	@Lazy
	private AuthFailureHandlerImpl authenticationFailureHandler;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public UserDetailsService userDetailsService() {
		return new UserDetailsServiceImpl();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService());
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		return authenticationProvider;
	}

	@Bean
    public InMemoryUserDetailsManager userDetailsServices() {
        UserDetails user = User.builder()
                .username("user@gmail.com")
                .password(passwordEncoder().encode("password")) // Securely encode the password
                .roles("USER")
                .build();

        UserDetails admin = User.builder()
                .username("admin@gmail.com")
                .password(passwordEncoder().encode("admin123")) // Securely encode the password
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user, admin);
    }
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception
	{
		http.csrf(csrf->csrf.disable()).cors(cors->cors.disable())
				.authorizeHttpRequests(req->req.requestMatchers("/user/**").hasRole("USER")
				.requestMatchers("/admin/**").hasRole("ADMIN")
				.requestMatchers("/**").permitAll())
				.formLogin(form -> form
		                .loginPage("/login") // Custom login page
		                .successHandler(customAuthenticationSuccessHandler()) // Redirect after successful login
		                .failureUrl("/login?error=true") // Redirect on login failure
		                .permitAll()
		            )
				.logout(logout -> logout
		                .logoutUrl("/logout")
		                .logoutSuccessUrl("/") // Redirect after logout
		                .permitAll()
		            );
		
		return http.build();
	}
	 @Bean
	    public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
	        return new AuthenticationSuccessHandler() {
	            @Override
	            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
	                                                Authentication authentication) throws IOException, ServletException {
	                Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
	                if (roles.contains("ROLE_ADMIN")) {
	                    response.sendRedirect("/admin/index1"); // Admin page
	                } else if (roles.contains("ROLE_USER")) {
	                    response.sendRedirect("/"); // User page
	                } else {
	                    response.sendRedirect("/"); // Default fallback
	                }
	            }
	        };
	 }

}