import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
    imports: [
        RouterModule.forChild([
            {
                path: 'admin-management',
                loadChildren: () => import('./admin-management/admin-management.module').then(m => m.AdminManagementModule)
            },
            {
                path: 'audits',
                loadChildren: () => import('./audits/audits.module').then(m => m.AuditsModule)
            },
            {
                path: 'configuration',
                loadChildren: () => import('./configuration/configuration.module').then(m => m.ConfigurationModule)
            },
            {
                path: 'health',
                loadChildren: () => import('./health/health.module').then(m => m.HealthModule)
            },
            {
                path: 'logs',
                loadChildren: () => import('./logs/logs.module').then(m => m.LogsModule)
            }
        ])
    ]
})
export class AdminRoutingModule {}
