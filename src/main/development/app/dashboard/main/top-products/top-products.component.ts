import { Component, OnInit } from '@angular/core';
import {ProductsService} from 'app/dashboard/main/products.service';

@Component({
  selector: 'eh-top-products',
  templateUrl: './top-products.component.html',
  styles: []
})
export class TopProductsComponent implements OnInit {

  products: any;

  constructor(private productsService: ProductsService) {
  }

  ngOnInit() {
    this.productsService.getTopProducts().subscribe(response => {
      this.products = response;
    });
  }

}
