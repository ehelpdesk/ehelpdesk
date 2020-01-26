import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {DEBUG_INFO_ENABLED} from './app.constants';
import {pageState} from 'app/dashboard/dashboard.route';

const routes: Routes = [
  {
    path: '',
    redirectTo: '/dashboard',
    pathMatch: 'full'
  }
];

@NgModule({
  imports: [RouterModule.forRoot(pageState, {useHash: true, enableTracing: DEBUG_INFO_ENABLED})],
  exports: [RouterModule]
})
export class IcsAppRoutingModule {
}
