package org.sid.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
@Entity
@Data @AllArgsConstructor @NoArgsConstructor @ToString
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)//mdp unique
    private String username;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) //write: stocker le mdp mais ne l envoyer pas (pa de read)
    private String password;
    private boolean actived;
    @ManyToMany(fetch = FetchType.EAGER)//Eager = automatiquement on charge les role de user
    private Collection<AppRole> roles=new ArrayList<>();
}
