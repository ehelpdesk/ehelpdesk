import {Component, EventEmitter, Input, isDevMode, OnInit, Output} from '@angular/core';
import {Router} from '@angular/router';
import {AuthServerProvider} from '../../core/auth/auth-session.service';
import {AccountService} from 'app/core/auth/account.service';

@Component({
  selector: 'eh-navbar',
  templateUrl: './navbar.component.html'
})
export class NavbarComponent implements OnInit {
  @Input()
  toggleSideBar: boolean;
  @Output()
  toggleSideBarChange = new EventEmitter<boolean>();
  role: string;
  loggedInUserFirstName: string;
  activeNav: string;

  constructor(private authServerProvider: AuthServerProvider
      , private router: Router
      , private accountService: AccountService
  ) {
  }

  ngOnInit(): void {
    this.activeNav = '';
    this.accountService.identity().subscribe(account => {
      this.role = account.authority;
      this.loggedInUserFirstName = account.firstName;
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
    this.router.navigate(['/dashboard']);
  }

  toggleSideMenu() {
    this.toggleSideBar = !this.toggleSideBar;
    this.toggleSideBarChange.emit(this.toggleSideBar);
  }
}
