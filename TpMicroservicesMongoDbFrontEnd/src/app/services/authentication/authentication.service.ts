import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { JwtHelperService } from '@auth0/angular-jwt';


@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {
  public host:string="http://localhost:8080";
  jwt: string;
  username: string;
  roles: Array<String>;
  constructor(private http:HttpClient) { }

  login(data){
    //console.log(data)
    //response pour ne pas convertir la rep en json pour pouvoir y acceder et recupperer les entete
    return this.http.post(this.host+"/login",data,{observe:'response'});
  }
  saveToken(jwt: string){
      //stock data in localstorage
    localStorage.setItem('token',jwt);
    this.jwt = jwt;
    this.parseJWT();
  }
   //get user data by auth0 library
  parseJWT(){
    let jwtHelper = new JwtHelperService();
    this.username=jwtHelper.decodeToken(this.jwt).obj;
    this.roles=jwtHelper.decodeToken(this.jwt).roles;
  }
  isAdmin(){
    return this.roles.indexOf('ADMIN')>=0;
  }
  isUser(){
    return this.roles.indexOf('USER')>=0;
  }
  isAuthenticated(){
    return this.roles!=undefined;
  }
  loadToken(){
    this.jwt = localStorage.getItem('token');
    this.parseJWT();
  }
  logout(){
    localStorage.removeItem('token');
    this.initializeParams();
  }
  initializeParams(){
    this.jwt = undefined;
    this.username = undefined;
    this.roles = undefined;
  }
}
