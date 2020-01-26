import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedModule } from 'app/shared/shared.module';
import { AdminManagementComponent } from 'app/admin/admin-management/admin-management.component';
import { AdminManagementDetailComponent } from 'app/admin/admin-management/admin-management-detail.component';
import { AdminManagementUpdateComponent } from 'app/admin/admin-management/admin-management-update.component';
import { AdminManagementDeleteDialogComponent } from 'app/admin/admin-management/admin-management-delete-dialog.component';
import { adminManagementRoute } from 'app/admin/admin-management/admin-management.route';

@NgModule({
    imports: [SharedModule, RouterModule.forChild(adminManagementRoute)],
    declarations: [
        AdminManagementComponent,
        AdminManagementDetailComponent,
        AdminManagementUpdateComponent,
        AdminManagementDeleteDialogComponent
    ],
    entryComponents: [AdminManagementDeleteDialogComponent]
})
export class AdminManagementModule {}
