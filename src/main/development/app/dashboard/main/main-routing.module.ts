import {NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';
import {AddProductPurchaseComponent} from 'app/dashboard/main/add-product-purchase/add-product-purchase.component';
import {SharedModule} from 'app/shared/shared.module';
import {ProductsComponent} from 'app/dashboard/main/products/products.component';
import {TopProductsComponent} from 'app/dashboard/main/top-products/top-products.component';

@NgModule({
    declarations: [AddProductPurchaseComponent, ProductsComponent, TopProductsComponent],
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
            },
            {
                path: 'products',
                component: ProductsComponent
            },
            {
                path: 'topProducts',
                component: TopProductsComponent
            }
        ])
    ]
})
export class MainRoutingModule {
}
