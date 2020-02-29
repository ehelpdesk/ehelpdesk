import {Component, OnInit} from '@angular/core';
import {FormBuilder} from '@angular/forms';
import {RegistrationService} from 'app/landing/registration/registration.service';
import {EventManager} from 'app/shared';
import {Router} from '@angular/router';

@Component({
    selector: 'eh-registration',
    templateUrl: './registration.component.html',
    styles: []
})
export class RegistrationComponent implements OnInit {

    registrationForm = this.fb.group({
        productCode: [''],
        username: [''],
        firstName: [''],
        lastName: [''],
        email: [''],
        mobileNumber: [''],
        password: [''],
        confirmPassword: [''],
        address: ['']
    });
    isProductCodeValidated = false;
    isInvalidProductCode = false;

    constructor(private fb: FormBuilder
        , private registrationService: RegistrationService
        , private eventManager: EventManager
        , private router: Router) {
    }

    ngOnInit() {
    }

    register() {
        this.registrationService.registerCustomer({
            productCode: this.registrationForm.get('productCode').value,
            username: this.registrationForm.get('username').value,
            firstName: this.registrationForm.get('firstName').value,
            lastName: this.registrationForm.get('lastName').value,
            email: this.registrationForm.get('email').value,
            mobileNumber: this.registrationForm.get('mobileNumber').value,
            password: this.registrationForm.get('password').value,
            confirmPassword: this.registrationForm.get('confirmPassword').value,
            address: this.registrationForm.get('address').value
        }).subscribe((response: any) => {
            this.eventManager.broadcast({
                name: 'Ehelpdesk_httpSuccess',
                content: 'Registration Successful, Please check your email.'
            });
            setTimeout(() => {
                this.router.navigate(['/home']);
            }, 5000);
        }, () => {
            window.scroll(0, 0);
        });
    }

    validateProductCode() {
        this.registrationService.validateProductCode(this.registrationForm.get('productCode').value).subscribe((response: any) => {
            window.scroll(0, 0);
            if (response && response.code) {
                this.registrationForm.patchValue({
                    productCode: response.code,
                    username: '',
                    firstName: response.userFirstName,
                    lastName: response.userLastName,
                    email: response.userEmail,
                    mobileNumber: response.userMobileNo,
                    password: '',
                    confirmPassword: '',
                    address: response.userAddress
                });
                this.isInvalidProductCode = false;
                this.isProductCodeValidated = true;
            } else {
                this.isInvalidProductCode = true;
                this.isProductCodeValidated = false;
            }
        });
    }
}
