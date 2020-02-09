import {Routes} from '@angular/router';
import {DashboardComponent} from './dashboard.component';
import * as routeConstant from '../route.constants';
import {UserRouteAccessService} from 'app/core/auth/user-route-access-service';

const PAGE_ROUTES =
  [
    {
      path: 'super-admin',
      data: {
        authority: ['ROLE_SUPER_ADMIN']
      },
      canActivate: [UserRouteAccessService],
      loadChildren: () => import('./../admin/admin-routing.module').then(m => m.AdminRoutingModule)
    },
    {
      path: 'admin',
      data: {
        authority: ['ROLE_ADMIN', 'ROLE_SUPER_ADMIN']
      },
      canActivate: [UserRouteAccessService],
      loadChildren: () => import('./../admin/admin-routing.module').then(m => m.AdminRoutingModule)
    },
    {
      path: 'myAccount',
      loadChildren: () => import('./my-account/my-account-routing.module').then(m => m.MyAccountRoutingModule)
    }
  ];
export const pageState: Routes = [
  {
    path: 'dashboard',
    component: DashboardComponent,
    children: PAGE_ROUTES
  },
  {
    path: '',
    redirectTo: '/dashboard',
    pathMatch: 'full'
  }
];
