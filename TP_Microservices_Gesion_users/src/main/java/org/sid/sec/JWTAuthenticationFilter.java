package org.sid.sec;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.sid.entities.AppUser;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

//premiere classe a appeler qd l'user essai de s'identifie
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
        	//lire le corps de la requette et le déserialisé dans un objest AppUser = json devient java
            AppUser appUser= new ObjectMapper().readValue(request.getInputStream(),AppUser.class);
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(appUser.getUsername(),appUser.getPassword()));
            //ici on appelle la fonction config auth de SecurityConf, qui, elle-meme, appelle loadUserBy.. de UserDEtails Impl
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
  //si tout marcha bien on appelle la methode succes...
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException, ServletException {
        User user=(User)authResult.getPrincipal(); //get authenticated user
        List<String> roles=new ArrayList<>();
        //get user roles
        authResult.getAuthorities().forEach(a->{
            roles.add(a.getAuthority());
        });
       
        String jwt= JWT.create()
        		//2eme partie de la jwt: les elements de payload a encoder baseURL64 s'ajoute a la 1er partie de jwt
                .withIssuer(request.getRequestURI()) //url de l'app qui generer le jwt, /login par ex.. , pas important
                .withSubject(user.getUsername())
                .withArrayClaim("roles",roles.toArray(new String[roles.size()])) //tab de string de dimension roles.size
                .withExpiresAt(new Date(System.currentTimeMillis()+SecurityParams.EXPIRATION)) //date d'expiration = 10j
                .sign(Algorithm.HMAC256(SecurityParams.SECRET)); //3eme partie de jwt: algo de la signature, s'ajoute a 1er et deuxieme partie de jwt
        //Secret seulement celui qui le genere le connait, il sert a verifier le token, pour savoir qu il s agit d'un token que moi qui a créé
        response.addHeader(SecurityParams.JWT_HEADER_NAME,jwt); //1ere partie de jwt: header
    }

}
