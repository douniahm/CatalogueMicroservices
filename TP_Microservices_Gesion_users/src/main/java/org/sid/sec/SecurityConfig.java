package org.sid.sec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration //indiquer que c'est une classe de config
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
        //en cas ou un user veut s'itentifier, spring appele auto cette implementation de userDetails (userDEtailsImpl),
        //cad on utilise l'impl userDetailsService qu on a define (et qui impl UserDetailsService) pour gerer les users avec le mdp encrypte par la classe bCryptPasswordEncoder
        //qui cherchera le user, recupper son username, password et roles, puis
        //il encode le mdp retourné par userDetailsImpl et le verifie
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable(); //car en stateless c est insuffisant pr la securite
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); //desactiver les sessions, on utilise jwt cad stateless
        http.authorizeRequests().antMatchers("/login/**","/register/**").permitAll(); //sans etre autentifié
        http.authorizeRequests().antMatchers("/appUsers/**","/appRoles/**").hasAuthority("ADMIN");
        http.authorizeRequests().anyRequest().authenticated();
        http.addFilter(new JWTAuthenticationFilter(authenticationManager()));//pour generer le token, la premiere classe a appeler qd l'user clique sur login
        http.addFilterBefore(new JWTAuthorizationFiler(), UsernamePasswordAuthenticationFilter.class); //le filter JWTAutho.. est de type UserPass.....
    }
}
