package com.f_crm.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.f_crm.service.AuthorityService;

@Configuration
@EnableWebSecurity
public class AuthorityConfig extends WebSecurityConfigurerAdapter{

	@Bean
	public BCryptPasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Autowired
	AuthorityService authorityService;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService((authorityService));
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception{
		http.csrf().disable().cors().disable();
		
		http.authorizeRequests()
			.antMatchers("/authorities", "/role","/user/**","/doctor/**", "/method/**", "/crmentry/**").hasRole("MANAGER")
			.antMatchers("/admin", "/customers","/customerslist/**","/change/**",
					"/userform/**", "/customersform/**", "/doctorform/**", "/methodform/**",
					"/edit/**", "/register/**").hasAnyRole("MANAGER", "STAFF","DOCTOR")
			.antMatchers("/admin").authenticated()
			.anyRequest().permitAll();
			
		http.exceptionHandling()
			.accessDeniedPage("/auth/access/denied");
		
		http.formLogin()
			.loginPage("/signin")
			.loginProcessingUrl("/sigin/save")
			.defaultSuccessUrl("/home", false)
			.failureUrl("/sigin?error=true");
		
		http.logout()
			.logoutUrl("/auth/logoff")
			.logoutSuccessUrl("/logout");
	}
}
