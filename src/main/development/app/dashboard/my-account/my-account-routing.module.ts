import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'changePassword',
        loadChildren: () => import('./change-password/change-password.module').then(m => m.ChangePasswordModule)
      }
    ])
  ]
})
export class MyAccountRoutingModule {}
