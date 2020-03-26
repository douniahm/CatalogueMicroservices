import { Component, OnInit } from '@angular/core';
import { CategoriesService } from '../services/categories/categories.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-categories',
  templateUrl: './categories.component.html',
  styleUrls: ['./categories.component.css']
})
export class CategoriesComponent implements OnInit {
  catagories;
  currentCategory;

  constructor(private catService:CategoriesService,private router:Router) { }

  ngOnInit() {
    this.catService.getCategories().subscribe(
      data => {this.catagories = data; console.log(data)},
      err => console.log(err)
      )
    }

  onGetprodutcs(categorie){
    this.currentCategory=categorie;
    let url = categorie._links.products.href;
    //btoa = typescript method for converting to string base64url
    this.router.navigateByUrl("/products/"+btoa(url));
  }
}
