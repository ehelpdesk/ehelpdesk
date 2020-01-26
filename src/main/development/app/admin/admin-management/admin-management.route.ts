import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { AdminManagementComponent } from 'app/admin/admin-management/admin-management.component';
import { AdminManagementDetailComponent } from 'app/admin/admin-management/admin-management-detail.component';
import { AdminManagementUpdateComponent } from 'app/admin/admin-management/admin-management-update.component';
import { UserService } from 'app/model/user.service';
import { User } from 'app/model/user.model';
import { ResolvePagingParams } from 'app/shared';

@Injectable({ providedIn: 'root' })
export class UserManagementResolve implements Resolve<any> {
    constructor(private service: UserService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['login'] ? route.params['login'] : null;
        if (id) {
            return this.service.find(id);
        }
        return new User();
    }
}

export const adminManagementRoute: Routes = [
    {
        path: '',
        component: AdminManagementComponent,
        resolve: {
            pagingParams: ResolvePagingParams
        },
        data: {
            pageTitle: 'userManagement.home.title',
            defaultSort: 'id,asc'
        }
    },
    {
        path: ':login/view',
        component: AdminManagementDetailComponent,
        resolve: {
            user: UserManagementResolve
        },
        data: {
            pageTitle: 'userManagement.home.title'
        }
    },
    {
        path: 'new',
        component: AdminManagementUpdateComponent,
        resolve: {
            user: UserManagementResolve
        }
    },
    {
        path: ':login/edit',
        component: AdminManagementUpdateComponent,
        resolve: {
            user: UserManagementResolve
        }
    }
];
