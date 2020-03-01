import {Component, OnInit} from '@angular/core';
import {FormBuilder, Validators} from '@angular/forms';
import {NewComplaintService} from 'app/dashboard/complaint/new-complaint/new-complaint.service';
import {BrandsService} from 'app/dashboard/main/brands.service';

@Component({
    selector: 'eh-new-complaint',
    templateUrl: './new-complaint.component.html',
    styles: []
})
export class NewComplaintComponent implements OnInit {
    complaint: any;
    editForm = this.fb.group({
        id: [null],
        brand: ['', [Validators.required]],
        product: ['', [Validators.required]],
        productCategory: ['', [Validators.required]],
        complaintType: ['', [Validators.required]],
        complaintDescription: ['', [Validators.maxLength(1000)]],
        activated: [true],
        authority: ['', Validators.required],
    });
    brands: any;
    products: any;
    productCategory = '';
    selectedProduct: any;
    complaintTypes: any;
    isSaving: boolean;

    constructor(private fb: FormBuilder
        , private newComplaintService: NewComplaintService
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

    save() {
        this.updateComplaint(this.complaint);
        if (this.complaint.id) {
            this.newComplaintService.update(this.complaint).subscribe(response => this.onSaveSuccess(response), () => this.onSaveError());
        } else {
            this.newComplaintService.create(this.complaint).subscribe(response => this.onSaveSuccess(response), () => this.onSaveError());
        }
    }

    selectProductCategory() {
        if (this.editForm.get('product') && this.editForm.get('product').value) {
            this.productCategory = this.products.filter(product => product.id === parseInt(this.editForm.get('product').value, 10))[0].productCategory.categoryName;
            this.complaintTypes = this.products.filter(product => product.id === parseInt(this.editForm.get('product').value, 10))[0].productCategory.complaintTypes;
        }
    }

    private updateComplaint(complaint: any) {
        if (!complaint) {
            complaint = {};
        }
        complaint.brand = this.editForm.get(['brand']).value;
        complaint.product = this.editForm.get(['product']).value;
        complaint.productCategory = this.editForm.get(['productCategory']).value;
        complaint.complaintType = this.editForm.get(['complaintType']).value;
        complaint.complaintDescription = this.editForm.get(['complaintDescription']).value;
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
