package org.sid;

import java.util.ArrayList;
import java.util.stream.Stream;

import org.sid.dao.CategoryRepository;
import org.sid.dao.ProductRepository;
import org.sid.entities.Category;
import org.sid.entities.Product;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TpMicroservicesMongoDbAngularApplication {

	public static void main(String[] args) {
		SpringApplication.run(TpMicroservicesMongoDbAngularApplication.class, args);
	}
	@Bean
	CommandLineRunner start(CategoryRepository catRepo, ProductRepository prodRepo) {
		return args->{
			catRepo.deleteAll();
			Stream.of("C1:Ordinateurs", "C2:Imprimantes").forEach(c->{
				catRepo.save(new Category(c.split(":")[0],c, new ArrayList<>())); //null = id va etre generer automatiquement
			});
			catRepo.findAll().forEach(System.out::println);
			
			prodRepo.deleteAll();
			Category c1 = catRepo.findById("C1").get();
			Stream.of("P1", "P2","P3","P4").forEach(name->{
				Product p = prodRepo.save(new Product(null, name, Math.random()*1000,c1)); //null = id va etre generer automatiquement
				c1.getProducts().add(p);
				catRepo.save(c1);//update c1
			});
			
			Category c2 = catRepo.findById("C2").get();
			Stream.of("P5", "P6").forEach(name->{
				Product p = prodRepo.save(new Product(null, name, Math.random()*1000,c2)); //null = id va etre generer automatiquement
				c2.getProducts().add(p);
				catRepo.save(c2);//update c2
			});
			
			prodRepo.findAll().forEach(p->{
				System.out.println(p.toString());
			});
			
		};
	}
}
