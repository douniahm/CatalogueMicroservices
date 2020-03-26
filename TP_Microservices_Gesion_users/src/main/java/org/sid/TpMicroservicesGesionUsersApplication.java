package org.sid;

import java.util.stream.Stream;

import org.sid.dao.AppUserRepository;
import org.sid.entities.AppRole;
import org.sid.service.AccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class TpMicroservicesGesionUsersApplication {

	public static void main(String[] args) {
		SpringApplication.run(TpMicroservicesGesionUsersApplication.class, args);
	}
	  @Bean
	    CommandLineRunner start(AccountService accountService){
	        return args->{
	            accountService.save(new AppRole(null,"USER"));
	            accountService.save(new AppRole(null,"ADMIN"));
	            Stream.of("user1","user2","user3","admin").forEach(un->{
	                accountService.saveUser(un,"1234","1234");
	            });
	            accountService.addRoleToUser("admin","ADMIN");
	        };
	        
	    }
		//bean = ajouter dans la memoire pour l'injecter par defaut 
	    @Bean
	    BCryptPasswordEncoder getBCPE(){
	        return new BCryptPasswordEncoder();
	    }
}
