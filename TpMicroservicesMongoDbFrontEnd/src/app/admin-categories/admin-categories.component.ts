import { Component, OnInit } from '@angular/core';
import { CategoriesService } from '../services/categories/categories.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-admin-categories',
  templateUrl: './admin-categories.component.html',
  styleUrls: ['./admin-categories.component.css']
})
export class AdminCategoriesComponent implements OnInit {
  catagories;
  mode;
  toEdit;
  constructor(private catService:CategoriesService,private router:Router) { }

  ngOnInit() {
    this.getCategories();
    }

    getCategories(){
      this.catService.getCategories().subscribe(
        data => {this.catagories = data; console.log(data)},
        err => console.log(err)
        )
    }
    onDelete(cat){
      let conf=confirm("Are you sure you want to delete this item?");
      if(!conf) return
      this.catService.deleteRessource(cat._links.self.href).subscribe(
        res => {
          this.getCategories();
        },
        err => console.log(err)
      );
    }
   /*onNewCat(){
      this.mode="new-cat";
    }*/
    onSaveCat(data){
      let url = this.catService.host+"/categories";
      this.catService.postRessource(url, data).subscribe(
        res =>{
          this.getCategories();
        },
        err => console.log(err)
      );
    }
    onEdit(data){
      this.toEdit = data;
      this.mode="edit";
    }
    editCat(data){
      console.log(data)
      console.log(this.toEdit)
      this.toEdit.name = data.name;
     this.catService.putRessource(this.toEdit._links.self.href, data).subscribe(
        res =>{
          this.getCategories();
          this.mode="";
          console.log(res);
        },
        err => console.log(err)
      );
    }
}
