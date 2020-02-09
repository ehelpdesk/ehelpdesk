import {NgModule} from '@angular/core';
import {HomeComponent} from 'app/home/home.component';
import {SharedModule} from 'app/shared/shared.module';
import {BrowserModule} from '@angular/platform-browser';
import {HttpClientModule} from '@angular/common/http';
import {RouterModule} from '@angular/router';
import {homeRoutes} from 'app/home/home.route';
import { ContactUsComponent } from './contact-us/contact-us.component';
import { FooterComponent } from './footer/footer.component';
import { ProductsComponent } from './products/products.component';
import { WorksComponent } from './works/works.component';

@NgModule({
  declarations: [HomeComponent, ContactUsComponent, FooterComponent, ProductsComponent, WorksComponent],
  entryComponents: [],
  imports: [SharedModule, BrowserModule, HttpClientModule, RouterModule.forRoot(homeRoutes, {useHash: true})],
  providers: [],
  bootstrap: []
})
export class HomeModule {
}
