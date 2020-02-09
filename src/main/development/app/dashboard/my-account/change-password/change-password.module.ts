import {NgModule} from '@angular/core';
import {ChangePasswordComponent} from './change-password.component';
import {SharedModule} from 'app/shared/shared.module';
import {RouterModule} from '@angular/router';
import {changePasswordRoute} from 'app/dashboard/my-account/change-password/change-password.route';

@NgModule({
  declarations: [ChangePasswordComponent],
  imports: [SharedModule, RouterModule.forChild(changePasswordRoute)],
})
export class ChangePasswordModule {
}
