import {Component, OnInit} from '@angular/core';
import {FormBuilder, Validators} from '@angular/forms';
import {BrandsService} from 'app/dashboard/main/brands.service';

@Component({
    selector: 'eh-add-product-purchase',
    templateUrl: './add-product-purchase.component.html',
    styles: []
})
export class AddProductPurchaseComponent implements OnInit {
    brands: any;
    products: any;
    productCategory = '';
    isSaving: boolean;

    editForm = this.fb.group({
        code: [null],
        brand: ['', [Validators.required]],
        product: ['', [Validators.required]],
        productCategory: ['', [Validators.required]],
        userFirstName: ['', [Validators.required]],
        userLastName: ['', [Validators.required]],
        userEmail: ['', [Validators.required]],
        userMobileNo: ['', [Validators.required]],
        userAddress: ['', [Validators.maxLength(1000)]]
    });

    constructor(private fb: FormBuilder
        , private brandsService: BrandsService) {
    }

    ngOnInit() {
        this.brandsService.getBrands().subscribe(response => {
            this.brands = response;
        });
    }

    populateProducts() {
        if (this.editForm.get('brand') && this.editForm.get('brand').value) {
            this.products = this.brands.filter(brand => brand.id === parseInt(this.editForm.get('brand').value, 10))[0].products;
        }
    }

    selectProductCategory() {
        if (this.editForm.get('product') && this.editForm.get('product').value) {
            this.productCategory = this.products.filter(product => product.id === parseInt(this.editForm.get('product').value, 10))[0].productCategory.categoryName;
        }
    }

    save() {

    }

    private onSaveSuccess(result) {
        this.isSaving = false;
        this.previousState();
    }

    previousState() {
        window.history.back();
    }

    private onSaveError() {
        this.isSaving = false;
    }
}
