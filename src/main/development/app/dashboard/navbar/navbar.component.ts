import {Component, isDevMode, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {AuthServerProvider} from '../../core/auth/auth-session.service';
import {AccountService} from 'app/core/auth/account.service';

@Component({
  selector: 'qf-navbar',
  templateUrl: './navbar.component.html'
})
export class NavbarComponent implements OnInit {
  role: string;
  activeNav: string;
  constructor(private authServerProvider: AuthServerProvider
              , private router: Router
              , private accountService: AccountService
              ) {}

  ngOnInit(): void {
    this.activeNav = '';
    this.accountService.identity().subscribe(account => {
      this.role = account.authority;
    });
  }

  logout() {
    this.authServerProvider.logout().subscribe(() => {
      this.router.navigate(['home']);
    });
  }

  activateNav(nav: string) {
    this.activeNav = nav;
  }

  navigateToDashboard() {
    this.router.navigate(['/home']);
  }
}
