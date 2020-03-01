import {NgModule} from '@angular/core';
import {SharedModule} from 'app/shared/shared.module';
import {RouterModule} from '@angular/router';
import {MainComponent} from 'app/dashboard/main/main/main.component';
import {mainRoute} from 'app/dashboard/main/main/main.route';
import {ManagerMenuComponent} from './main/manager-menu/manager-menu.component';
import {TechnicianMenuComponent} from './main/technician-menu/technician-menu.component';
import {CustomerMenuComponent} from './main/customer-menu/customer-menu.component';
import {AdminMenuComponent} from './main/admin-menu/admin-menu.component';

@NgModule({
    declarations: [MainComponent, ManagerMenuComponent, TechnicianMenuComponent, CustomerMenuComponent, AdminMenuComponent],
    imports: [SharedModule, RouterModule.forChild(mainRoute)],
})
export class MainModule {
}
