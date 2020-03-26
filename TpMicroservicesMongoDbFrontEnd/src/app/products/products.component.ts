import { Component } from '@angular/core';
import { CategoriesService } from '../services/categories/categories.service';
import { ActivatedRoute, Router, NavigationEnd } from '@angular/router';

@Component({
  selector: 'app-products',
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.css']
})
export class ProductsComponent {
  products;

  constructor(private catalogueService:CategoriesService,private route:ActivatedRoute,
    private router:Router) {
      //souscription aux evenement de routeur, car on est tjrs ds le meme component
      //mais on change le param, et ce param est le lien de prod de la cat
    router.events.subscribe(event=>{
      if(event instanceof NavigationEnd){
        //inverse de btoa() pour décoder
        let url = atob(route.snapshot.params.urlProds);
        console.log(url);
        this.getProducts(url);
      }
    })

   }

  getProducts(url){
    this.catalogueService.getRessource(url).subscribe(
      data => this.products=data,
      error => console.log(error)
      );
  }

}
