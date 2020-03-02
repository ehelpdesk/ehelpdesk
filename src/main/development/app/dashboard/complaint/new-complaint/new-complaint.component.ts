import {Component, OnInit} from '@angular/core';
import {FormBuilder, Validators} from '@angular/forms';
import {BrandsService} from 'app/dashboard/main/brands.service';
import {ActivatedRoute} from '@angular/router';
import {ComplaintService} from 'app/dashboard/complaint/complaint.service';

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
        , private route: ActivatedRoute
        , private newComplaintService: ComplaintService
        , private brandsService: BrandsService) {
    }

    ngOnInit() {
        this.route.data.subscribe(({complaint}) => {
            this.complaint = complaint.body ? complaint.body : complaint;
            this.updateComplaint();
        });
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
        this.updateComplaint();
        if (this.complaint.id) {
            this.newComplaintService.update(this.complaint).subscribe(response => this.onSaveSuccess(response), () => this.onSaveError());
        } else {
            this.newComplaintService.create(this.complaint).subscribe(response => this.onSaveSuccess(response), () => this.onSaveError());
        }
    }

    selectProductCategory() {
        if (this.editForm.get('product') && this.editForm.get('product').value) {
            this.productCategory = this.getSelectedProduct().productCategory.categoryName;
            this.complaintTypes = this.getSelectedProduct().productCategory.complaintTypes;
        }
    }

    private getSelectedProduct() {
        return this.products.filter(product => product.id === parseInt(this.editForm.get('product').value, 10))[0];
    }

    private getSelectedComplaintType() {
        return this.complaintTypes.filter(complaintType => complaintType.id === parseInt(this.editForm.get('complaintType').value, 10))[0];
    }

    private updateComplaint() {
        if (!this.complaint) {
            this.complaint = {};
        }
        this.complaint.brand = this.editForm.get(['brand']).value;
        this.complaint.product = this.editForm.get(['product']).value;
        this.complaint.productName = this.getSelectedProduct().productName;
        this.complaint.productCategory = this.editForm.get(['productCategory']).value;
        this.complaint.complaintType = this.editForm.get(['complaintType']).value;
        this.complaint.complaintTypeName = this.getSelectedComplaintType().type;
        this.complaint.complaintDescription = this.editForm.get(['complaintDescription']).value;
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
