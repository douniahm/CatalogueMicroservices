import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { AuthenticationService } from '../authentication/authentication.service';

@Injectable({
  providedIn: 'root'
})
export class CategoriesService {
  public host: string = "http://localhost:8081";
  constructor(private http: HttpClient, private authService: AuthenticationService) { }
  getCategories(){
    return this.http.get(this.host+"/categories");
  }

  getRessource(url){
    console.log(url);
    return this.http.get(url);
  }
  deleteRessource(url){
    console.log(url);
    let headers = new HttpHeaders({'authorization': "Bearer "+this.authService.jwt});
    return this.http.delete(url, {headers: headers});
  }
  postRessource(url, data){
    console.log(url);
    let headers = new HttpHeaders({'authorization': "Bearer "+this.authService.jwt});
    return this.http.post(url,data,{headers: headers});
  }
  putRessource(url, data){
    console.log(data);
    let headers = new HttpHeaders({'authorization': "Bearer "+this.authService.jwt});
    return this.http.put(url,data,{headers: headers});
  }

}
