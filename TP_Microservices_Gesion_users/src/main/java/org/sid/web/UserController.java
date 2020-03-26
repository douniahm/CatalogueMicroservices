package org.sid.web;

import lombok.Data;
import org.sid.entities.AppUser;
import org.sid.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    private AccountService accountService;
    @PostMapping("/register")
    public AppUser register(@RequestBody  UserForm userForm){
        return  accountService.saveUser(
                userForm.getUsername(),userForm.getPassword(),userForm.getConfirmedPassword());
    }
}
//on utilise userFoem qui contient les info necessaire pour enregistrer un user en format json, au lieu d ecrire chaque attribut..
//cela veut dire au niveau de Angular il faut envoyer un objet ayant 3 params qui ressemble a UserForm, ecrit comme ci-dessous: username, password, confirmedPassword
@Data
class UserForm{
    private String username;
    private String password;
    private String confirmedPassword;
}
