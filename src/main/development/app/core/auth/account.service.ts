import {Account} from 'app/model/account.model';

import {SERVER_API_URL} from 'app/app.constants';
import {Injectable} from '@angular/core';
import {Observable, Subject} from 'rxjs';
import {SessionStorageService} from 'ngx-webstorage';
import {HttpClient} from '@angular/common/http';
import {shareReplay, tap} from 'rxjs/operators';

@Injectable({providedIn: 'root'})
export class AccountService {
    private userIdentity: Account;
    private authenticated = false;
    private authenticationState = new Subject<any>();
    private accountCache$: Observable<Account>;

    constructor(private sessionStorage: SessionStorageService, private http: HttpClient) {
    }

    fetch(): Observable<Account> {
        return this.http.get<Account>(SERVER_API_URL + 'api/account');
    }

    save(account: Account): Observable<Account> {
        return this.http.post<Account>(SERVER_API_URL + 'api/account', account);
    }

    authenticate(identity) {
        this.userIdentity = identity;
        this.authenticated = identity !== null;
        this.authenticationState.next(this.userIdentity);
    }

    hasAnyAuthority(authority: string[] | string): boolean {
        if (!this.authenticated || !this.userIdentity || !this.userIdentity.authority) {
            return false;
        }

        if (!Array.isArray(authority)) {
            authority = [authority];
        }

        return authority.some((auth: string) => this.userIdentity.authority.includes(auth));
    }

    identity(force?: boolean): Observable<Account> {
        if (force) {
            this.accountCache$ = null;
        }

        if (!this.accountCache$) {
            this.accountCache$ = this.fetch().pipe(
                tap(
                    account => {
                        if (account) {
                            this.userIdentity = account;
                            this.authenticated = true;
                        } else {
                            this.userIdentity = null;
                            this.authenticated = false;
                        }
                        this.authenticationState.next(this.userIdentity);
                    },
                    () => {
                        this.userIdentity = null;
                        this.authenticated = false;
                        this.authenticationState.next(this.userIdentity);
                    }
                ),
                shareReplay()
            );
        }
        return this.accountCache$;
    }

    isAuthenticated(): boolean {
        return this.authenticated;
    }

    isIdentityResolved(): boolean {
        return this.userIdentity !== undefined;
    }

    getAuthenticationState(): Observable<any> {
        return this.authenticationState.asObservable();
    }

    getImageUrl(): string {
        return this.isIdentityResolved() ? this.userIdentity.imageUrl : null;
    }

    isLoginActive(): any {
        return this.http.get('api/isLoginActive');
    }
}
