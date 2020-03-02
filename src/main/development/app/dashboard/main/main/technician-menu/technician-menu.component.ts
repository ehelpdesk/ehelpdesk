import { Component, OnInit } from '@angular/core';
import {Router} from '@angular/router';
import {AuthServerProvider} from 'app/core/auth/auth-session.service';
import {AccountService} from 'app/core/auth/account.service';

@Component({
  selector: 'eh-technician-menu',
  templateUrl: './technician-menu.component.html',
  styles: []
})
export class TechnicianMenuComponent implements OnInit {
  role: any;

  constructor(private router: Router
      , private authServerProvider: AuthServerProvider
      , private accountService: AccountService) {
  }

  ngOnInit() {
    this.accountService.identity().subscribe(account => {
      this.role = account.authority;
    });
  }

  navigateToNewComplaint() {
    this.router.navigate(['/dashboard/main/complaint/newComplaint']);
  }

  navigateToComplaints() {
    this.router.navigate(['/dashboard/main/complaint/complaints']);
  }

  logout() {
    this.authServerProvider.logout().subscribe(() => {
      this.router.navigate(['home']);
    });
  }

  navigateToMyAccount() {
    this.router.navigate(['/dashboard/myAccount/changePassword']);
  }

}
