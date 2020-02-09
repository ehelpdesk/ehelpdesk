import './vendor.ts';
import {NgModule, Injector} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {NgxWebstorageModule} from 'ngx-webstorage';
import {SharedModule} from './shared/shared.module';
import {IcsAppRoutingModule} from './app-routing.module';
import {MainComponent} from 'app/main/main.component';
import {DashboardModule} from './dashboard/dashboard.module';
import {LandingModule} from 'app/landing/landing.module';
import {HTTP_INTERCEPTORS} from '@angular/common/http';
import {AuthInterceptor} from 'app/blocks/interceptor/auth.interceptor';
import {HomeModule} from 'app/home/home.module';
import {ErrorHandlerInterceptor} from 'app/blocks/interceptor/errorhandler.interceptor';

@NgModule({
  imports: [
    BrowserModule,
    IcsAppRoutingModule,
    NgxWebstorageModule.forRoot({prefix: 'eh-ehelpdesk', separator: '-'}),
    SharedModule,
    LandingModule,
    DashboardModule,
    HomeModule
  ],
  declarations: [MainComponent],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: ErrorHandlerInterceptor,
      multi: true
    },
  ],
  bootstrap: [MainComponent]
})
export class IcsAppModule {
}
