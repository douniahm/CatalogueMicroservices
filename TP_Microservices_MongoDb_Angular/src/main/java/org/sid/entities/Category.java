package org.sid.entities;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Document //MONGO
@Data @AllArgsConstructor @NoArgsConstructor @ToString //LAMBOOK
public class Category {
	@Id
	private String id;
	private String name;
	@DBRef //MAPPING OBJET DOCUMENT: on stock seulement l'id des produits dans categorie, les produits seront stockés dans un document appart
			//Sans @DEBRef produit seront stockes directement dans le document de category => Embeded Documents
	private Collection<Product> products = new ArrayList<>(); 
	
}
