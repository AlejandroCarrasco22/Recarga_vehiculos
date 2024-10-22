package com.uva.dbcs.login;

import com.uva.dbcs.login.Security.JWTAuthorizationFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@SpringBootApplication
@ComponentScan(basePackages = {"com.uva.dbcs.sharedlibrary", "com.uva.dbcs.login"})
public class LoginApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoginApplication.class, args);
	}

	
	@EnableWebSecurity
	@Configuration
	class WebSecurityConfig extends WebSecurityConfigurerAdapter {

		@Override
		protected void configure(HttpSecurity http) throws Exception {
            http.cors()
				.and()
					.csrf().disable()
			        .addFilterAfter(new JWTAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
                    .authorizeRequests()
					.antMatchers(HttpMethod.OPTIONS, "/**").permitAll() // Permitir solicitudes OPTIONS sin autenticación
					.antMatchers(HttpMethod.POST, "/paginaPrincipal/login").permitAll()
                    .anyRequest().authenticated();
		}
	}

}
