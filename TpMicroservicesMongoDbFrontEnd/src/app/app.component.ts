import { Component, OnInit } from '@angular/core';
import { AuthenticationService } from './services/authentication/authentication.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit{
  title = 'TpMicroservicesMongoDbFrontEnd';
  constructor(private authenticationService: AuthenticationService){}
  ngOnInit(){
    this.authenticationService.loadToken();
  }
  isAdmin(){
    return this.authenticationService.isAdmin();
  }
  isUser(){
    return this.authenticationService.isUser();
  }
  isAuthenticated(){
    return this.authenticationService.isAuthenticated();
  }
  onLogout(){
    this.authenticationService.logout();
  }
}
