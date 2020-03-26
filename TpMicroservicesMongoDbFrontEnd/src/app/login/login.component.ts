import { Component } from '@angular/core';
import { AuthenticationService } from '../services/authentication/authentication.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {

  constructor(private authenticationService:AuthenticationService, private router: Router) { }

  onLogin(f){
    this.authenticationService.login(f).subscribe(
      resp=>{
        console.log(resp.headers.get('authorization'))
        //stock jwt
        this.authenticationService.saveToken(resp.headers.get('authorization'))
        this.router.navigateByUrl("/");
      },
        error=>{console.log(error)}
    );
  }
}
