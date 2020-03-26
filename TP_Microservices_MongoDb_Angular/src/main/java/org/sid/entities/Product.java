package org.sid.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Document
@Data @AllArgsConstructor @NoArgsConstructor @ToString //LAMBOOK
public class Product {
	@Id
	private String id;
	private String name;
	private double price;
	@DBRef @ToString.Exclude
	//eviter stackoverflow cause par infinite loop de toString, 
	//comme on peut la redefinier et enlever celle de lambook car il autilise toutes les attrbiuts
	private Category category;
}
