package com.uva.dbcs.vehicles;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.uva.dbcs.vehicles.Security.JWTAuthorizationFilter;

@SpringBootApplication
@ComponentScan(basePackages = {"com.uva.dbcs.sharedlibrary", "com.uva.dbcs.vehicles"})
public class VehiclesApplication {

	public static void main(String[] args) {
		SpringApplication.run(VehiclesApplication.class, args);
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
                    .anyRequest().authenticated();}
	}

}
