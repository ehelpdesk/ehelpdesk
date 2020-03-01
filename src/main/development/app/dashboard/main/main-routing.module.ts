import {NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';
import {AddProductPurchaseComponent} from 'app/dashboard/main/add-product-purchase/add-product-purchase.component';
import {SharedModule} from 'app/shared/shared.module';

@NgModule({
  declarations: [AddProductPurchaseComponent],
  imports: [
    SharedModule,
    RouterModule.forChild([
      {
        path: 'first',
        loadChildren: () => import('./main.module').then(m => m.MainModule)
      },
      {
        path: 'complaint',
        loadChildren: () => import('./../complaint/complaint.module').then(m => m.ComplaintModule)
      },
      {
        path: 'addProductPurchase',
        component: AddProductPurchaseComponent
      }
    ])
  ]
})
export class MainRoutingModule { }
