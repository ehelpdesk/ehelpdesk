import {Routes} from '@angular/router';
import {LoginComponent} from 'app/landing/login/login.component';
import {ForgotUsernamePasswordComponent} from 'app/landing/fortgot-username-password/forgot-username-password.component';
import {passwordResetRoute} from 'app/landing/password-reset/password-reset.route';
import {ConfirmOtpComponent} from 'app/landing/confirm-otp/confirm-otp.component';
import {LandingComponent} from 'app/landing/landing/landing.component';
import {RegistrationComponent} from 'app/landing/registration/registration.component';
import {ActivateComponent} from 'app/landing/activate/activate.component';

export const LOGIN_ROUTES: Routes = [
    {
        path: 'landing',
        component: LandingComponent,
        children: [
            {
                path: 'login',
                component: LoginComponent
            },
            {
                path: 'forgotUsernamePassword',
                component: ForgotUsernamePasswordComponent
            },
            {
                path: 'confirmOtp',
                component: ConfirmOtpComponent
            },
            passwordResetRoute,
            {
                path: 'register',
                component: RegistrationComponent
            },
            {
                path: 'activate',
                component: ActivateComponent
            },
        ]
    }
];
