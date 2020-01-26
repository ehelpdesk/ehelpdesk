import {HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {tap} from 'rxjs/operators';
import {NavigationExtras, Router} from '@angular/router';
import {Observable} from 'rxjs';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  constructor(private router: Router) {
  }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(request).pipe(
      tap(
        () => {
        },
        (err: any) => {
          if (err instanceof HttpErrorResponse) {
            if (err.status === 401) {
              const currentRoute = this.router.url;
              if (currentRoute !== '/login' && currentRoute !== '/confirmOtp' && currentRoute.indexOf('forgotUsernamePassword') < 0) {
                if (err.error && err.error.message && err.error.message.includes('Authentication failed')) {
                  this.router.navigate(['/login']);
                } else {
                  this.router.navigate(['/home']);
                }
              }
            } else if (err.status === 200 && err.error && err.error.text.includes('This session has been expired')) {
              const queryParams: NavigationExtras = {
                queryParams: {'multipleLogin': 'true'}
              };
              this.router.navigate(['/login'], queryParams);
            }

          }
        }
      )
    );
  }
}
