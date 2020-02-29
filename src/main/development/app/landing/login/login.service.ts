import {Injectable} from '@angular/core';
import {flatMap} from 'rxjs/operators';
import {AccountService} from 'app/core/auth/account.service';
import {AuthServerProvider} from 'app/core/auth/auth-session.service';
import {HttpClient} from '@angular/common/http';

@Injectable({providedIn: 'root'})
export class LoginService {
  private _userCredentials: any;

  get userCredentials(): any {
    return this._userCredentials;
  }

  set userCredentials(userCredentials: any) {
    this._userCredentials = userCredentials;
  }

  constructor(private accountService: AccountService, private authServerProvider: AuthServerProvider, private http: HttpClient) {
  }

  login(credentials) {
    return this.authServerProvider.login(credentials).pipe(flatMap(() => this.accountService.identity(true)));
  }

  logout() {
    this.authServerProvider.logout().subscribe(null, null, () => this.accountService.authenticate(null));
  }

  hasActiveLogin(loginName) {
    return this.http.get(`/api/hasActiveLogin/${loginName}`);
  }

  verifyCredentials(credentials: { password: any; rememberMe: any; username: any }) {
    return this.http.post(`/api/authentication`, credentials);
  }
}
