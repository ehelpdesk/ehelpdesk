import {NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';
import {LoginComponent} from 'app/landing/login/login.component';
import {LOGIN_ROUTES} from 'app/landing/landing.route';
import {LoginService} from 'app/landing/login/login.service';
import {ForgotUsernamePasswordComponent} from './fortgot-username-password/forgot-username-password.component';
import {PasswordResetComponent} from './password-reset/password-reset.component';
import {ActiveLoginConfirmationComponent} from './active-login-confirmation/active-login-confirmation.component';
import {ConfirmOtpComponent} from './confirm-otp/confirm-otp.component';
import {SharedModule} from 'app/shared/shared.module';
import {RegistrationComponent} from 'app/landing/registration/registration.component';
import {LandingComponent} from './landing/landing.component';
import { ActivateComponent } from './activate/activate.component';

@NgModule({
    declarations: [
        LoginComponent
        , ForgotUsernamePasswordComponent
        , PasswordResetComponent
        , ActiveLoginConfirmationComponent
        , ConfirmOtpComponent
        , RegistrationComponent
        , LandingComponent, ActivateComponent
    ],
    entryComponents: [ActiveLoginConfirmationComponent],
    imports: [SharedModule, RouterModule.forRoot(LOGIN_ROUTES, {useHash: true})],
    providers: [LoginService],
    bootstrap: []
})
export class LandingModule {
}
