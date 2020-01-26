import {Routes} from '@angular/router';
import {LoginComponent} from 'app/landing/login/login.component';
import {ForgotUsernamePasswordComponent} from 'app/landing/fortgot-username-password/forgot-username-password.component';
import {passwordResetRoute} from 'app/landing/password-reset/password-reset.route';
import {ConfirmOtpComponent} from 'app/landing/confirm-otp/confirm-otp.component';

export const LOGIN_ROUTES: Routes = [
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
  passwordResetRoute
];
