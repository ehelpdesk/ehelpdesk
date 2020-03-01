import {Component, OnDestroy, OnInit, Renderer2} from '@angular/core';
import {Router} from '@angular/router';
import {AccountService} from 'app/core/auth/account.service';
import {AuthServerProvider} from 'app/core/auth/auth-session.service';

@Component({
    selector: 'eh-dashboard',
    templateUrl: './dashboard.component.html'
})
export class DashboardComponent implements OnInit, OnDestroy {
    toggleSideBar = false;
    role: string;
    constructor(private router: Router
        , private accountService: AccountService
        , private renderer2: Renderer2
        , private authServerProvider: AuthServerProvider) {
    }

    ngOnInit() {
        this.accountService.isLoginActive().subscribe();
        this.accountService.identity().subscribe(account => {
            this.role = account.authority;
            this.router.navigate(['/dashboard/main/first']);
        });
    }

    ngOnDestroy(): void {
    }

    logout() {
        this.authServerProvider.logout().subscribe(() => {
            this.router.navigate(['home']);
        });
    }

    closeSideBar() {
        this.toggleSideBar = false;
    }
}
