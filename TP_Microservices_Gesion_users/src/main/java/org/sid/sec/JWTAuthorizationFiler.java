package org.sid.sec;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
//onceperRequet==appeler cette fonction pour chaque requette
public class JWTAuthorizationFiler extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        response.addHeader("Access-Control-Allow-Origin", "*");//permet des req de n'importe quelle app
        response.addHeader("Access-Control-Allow-Headers", "Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers,authorization");//permettre aux app d'envoyer ces headers
        response.addHeader("Access-Control-Expose-Headers", "Access-Control-Allow-Origin, Access-Control-Allow-Credentials, authorization");//permet aux app de lire ces headers dans mes reponses
        //aprs avoir ajouter les headers dans la reponses, on l'envoie a la req OPTIONS en repondant par ok
        //la req options précédent la req originale (get/post..) pour que le browser sache ce que le serveur lui permet comme autorisations
        //allow les requettes sinon ils seront refusees par CORS
        response.addHeader("Access-Control-Allow-Methods","GET,POST,PUT,DELETE,PATCH");

        if(request.getMethod().equals("OPTIONS")){
            response.setStatus(HttpServletResponse.SC_OK);
        }
        //login, ya pas de jwt
        else if(request.getRequestURI().equals("/login")) {
            filterChain.doFilter(request, response);
            return;
        }//cas d'une autre req que le login, on require le jwt, donc on verifie jwt..
        else {
            String jwtToken = request.getHeader(SecurityParams.JWT_HEADER_NAME); //recuppere le jwt de la requette
            System.out.println("Token="+jwtToken);
            if (jwtToken == null || !jwtToken.startsWith(SecurityParams.HEADER_PREFIX)) {
            	//ds ce cas on authentifie pas l user, on lui passe au filtre suivant
                filterChain.doFilter(request, response);
                return;
            }
            //verifier la signature
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SecurityParams.SECRET)).build();
            String jwt = jwtToken.substring(SecurityParams.HEADER_PREFIX.length());//enlever le header de jwt
            DecodedJWT decodedJWT = verifier.verify(jwt);
            //get user data from jwt
            System.out.println("JWT="+jwt);
            String username = decodedJWT.getSubject();
            List<String> roles = decodedJWT.getClaims().get("roles").asList(String.class); //les roles qu ona ajouter dans le jwt comme claim dans le payload dans la methode success.. de JWTAuthentication aprzs l authentification de user
            System.out.println("username="+username);
            System.out.println("roles="+roles);
            Collection<GrantedAuthority> authorities = new ArrayList<>(); //creer une list d'authorités
            roles.forEach(rn -> {
                authorities.add(new SimpleGrantedAuthority(rn));
            });
            //authentifier l'user
            UsernamePasswordAuthenticationToken user =
                    new UsernamePasswordAuthenticationToken(username, null, authorities);//null pour le mdp
            SecurityContextHolder.getContext().setAuthentication(user);
            filterChain.doFilter(request, response);
        }

    }
}
