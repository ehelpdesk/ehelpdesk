import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { HttpClientModule } from '@angular/common/http';
import { RouterModule } from '@angular/router';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { LoginComponent } from 'app/landing/login/login.component';
import { LOGIN_ROUTES } from 'app/landing/login/landing.route';
import { LoginService } from 'app/landing/login/login.service';
import { ForgotUsernamePasswordComponent } from './fortgot-username-password/forgot-username-password.component';
import { PasswordResetComponent } from './password-reset/password-reset.component';
import { ActiveLoginConfirmationComponent } from './active-login-confirmation/active-login-confirmation.component';
import { ConfirmOtpComponent } from './confirm-otp/confirm-otp.component';
import {SharedModule} from 'app/shared/shared.module';

@NgModule({
    declarations: [LoginComponent, ForgotUsernamePasswordComponent, PasswordResetComponent, ActiveLoginConfirmationComponent, ConfirmOtpComponent],
    entryComponents: [ActiveLoginConfirmationComponent],
    imports: [SharedModule, RouterModule.forRoot(LOGIN_ROUTES, { useHash: true })],
    providers: [LoginService],
    bootstrap: []
})
export class LandingModule {}
