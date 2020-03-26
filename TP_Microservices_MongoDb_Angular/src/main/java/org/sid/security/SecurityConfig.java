package org.sid.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	//a mettre dans une autre classe
	/*@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		BCryptPasswordEncoder bcpe= getBCPE();

		//inMemory user
		auth.inMemoryAuthentication().withUser("admin").password(bcpe.encode("1234")).roles("ADMIN","USER"); //noop: ne pas encoder le mdp
		auth.inMemoryAuthentication().withUser("user1").password(bcpe.encode("1234")).roles("USER");
	}*/
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//super.configure(http); //pour l'auto login
		//http.authorizeRequests().anyRequest().permitAll(); //par defaut permit all meme si on met pas cette ligne
		http.csrf().disable(); //le désactiver pour tester avec le client Advanced Rest Client, en plus c est insuffisant dans le stateless
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);//desactiver les sessions cad auth statless avec un token qu on va definir
		http.authorizeRequests().antMatchers(HttpMethod.GET,"/categories/**").permitAll();//on peut consulter (GET req) les produits et categories sans authentification
		http.authorizeRequests().antMatchers(HttpMethod.GET,"/products/**").permitAll();
		http.authorizeRequests().antMatchers("/categories/**").hasAnyAuthority("ADMIN");
		http.authorizeRequests().antMatchers("/products/**").hasAnyAuthority("USER");//seulement les user peuvent acceder à localhost/products/**
		http.authorizeRequests().anyRequest().authenticated();//pr chaque url faut etre authentifie
		//le premier filtre a faire avant de verifier le role.. = lire la req http et voir si il ya jwt par la classe JWT.. qu on cree
		http.addFilterBefore(new JWTAutorizationFilter(), UsernamePasswordAuthenticationFilter.class);
	}
}
