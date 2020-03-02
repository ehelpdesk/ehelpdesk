import {Component, OnInit} from '@angular/core';
import {FormBuilder, Validators} from '@angular/forms';
import {BrandsService} from 'app/dashboard/main/brands.service';
import {AddProductPurchaseService} from 'app/dashboard/main/add-product-purchase/add-product-purchase.service';
import {EventManager} from 'app/shared';

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
        userAddress: ['', [Validators.maxLength(1000)]],
        userCity: ['', [Validators.required]]
    });
    cities = [
        'Tirupati',
        'Eluru',
        'Ongole',
        'Visakhapatnam',
        'Vijayawada',
        'Guntur',
        'Rajahmundry',
        'Nellore',
        'Kakinada',
        'Kurnool',
        'Kadapa',
        'Anantapur',
        'Tenali',
        'Amaravati',
        'Kavali'
    ];

    constructor(private fb: FormBuilder
        , private brandsService: BrandsService
        , private addProductPurchaseService: AddProductPurchaseService
    , private eventManager: EventManager) {
    }

    ngOnInit() {
        this.brandsService.getBrands().subscribe(response => {
            this.brands = response;
        });
        this.cities = this.cities.sort((n1, n2) => {
            if (n1 > n2) {
                return 1;
            }
            if (n1 < n2) {
                return -1;
            }
            return 0;
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
            this.editForm.patchValue({
                productCategory: this.productCategory
            });
        }
    }

    save() {
        this.isSaving = true;
        this.addProductPurchaseService.saveProductPurchase(this.getProductPurchaseDetails()).subscribe(() => {
            this.isSaving = false;
            this.resetForm();
            this.eventManager.broadcast({
                name: 'Ehelpdesk_httpSuccess',
                content: 'A email has been sent to customer with details.'
            });
            window.scroll(0, 0);
        });
    }

    private getProductPurchaseDetails() {
        const productPurchase: any = {};
        productPurchase.productDTO = {};
        productPurchase.productDTO.id = this.editForm.get(['product']).value;
        productPurchase.userFirstName = this.editForm.get(['userFirstName']).value;
        productPurchase.userLastName = this.editForm.get(['userLastName']).value;
        productPurchase.userEmail = this.editForm.get(['userEmail']).value;
        productPurchase.userMobileNo = this.editForm.get(['userMobileNo']).value;
        productPurchase.userAddress = this.editForm.get(['userAddress']).value;
        productPurchase.userCity = this.editForm.get(['userCity']).value;
        return productPurchase;
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

    private resetForm() {
        this.editForm.patchValue({
            brand: '',
            product: '',
            productCategory: '',
            userFirstName: '',
            userLastName: '',
            userEmail: '',
            userMobileNo: '',
            userAddress: '',
            userCity: ''
        });
    }
}
