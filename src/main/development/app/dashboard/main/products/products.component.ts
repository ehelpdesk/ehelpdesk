import {Component, OnInit} from '@angular/core';
import {ProductsService} from 'app/dashboard/main/products.service';

@Component({
    selector: 'eh-products',
    templateUrl: './products.component.html',
    styles: []
})
export class ProductsComponent implements OnInit {
    products: any;

    constructor(private productsService: ProductsService) {
    }

    ngOnInit() {
        this.productsService.getProducts().subscribe(response => {
            this.products = response;
        });
    }

}
