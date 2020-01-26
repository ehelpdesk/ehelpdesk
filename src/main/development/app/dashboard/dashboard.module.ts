import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {HttpClientModule} from '@angular/common/http';
import {RouterModule} from '@angular/router';
import {pageState} from './dashboard.route';
import {DashboardComponent} from './dashboard.component';
import {LoadingScreenComponent} from 'app/blocks/loadingScreen/loadingScreen.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {NavbarComponent} from 'app/dashboard/navbar/navbar.component';
import {SharedModule} from 'app/shared/shared.module';

@NgModule({
  declarations: [DashboardComponent, NavbarComponent, LoadingScreenComponent],
  entryComponents: [],
  imports: [SharedModule, BrowserModule, HttpClientModule, ReactiveFormsModule, RouterModule.forRoot(pageState, {useHash: true})],
  providers: [],
  bootstrap: []
})
export class DashboardModule {
}
