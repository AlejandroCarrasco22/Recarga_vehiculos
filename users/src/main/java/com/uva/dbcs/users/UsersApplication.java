package com.uva.dbcs.users;

import com.uva.dbcs.users.Security.JWTAuthorizationFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StringUtils;

@SpringBootApplication
@ComponentScan(basePackages = {"com.uva.dbcs.sharedlibrary", "com.uva.dbcs.users"})
public class UsersApplication {

	public static void main(String[] args) {
		SpringApplication.run(UsersApplication.class, args);
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
					.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
					// Dejamos desprotegido el endpoint que devuelve un usuario en función de su email,
					// necesario para que el login haga la comprobación de credenciales.
					.requestMatchers(request -> {
						String uri = request.getRequestURI();
						String emailParam = request.getParameter("email");
						return StringUtils.hasText(emailParam) && uri.startsWith("/paginaPrincipal/users");
					}).permitAll()
                    .anyRequest().authenticated();
		}
	}

}
