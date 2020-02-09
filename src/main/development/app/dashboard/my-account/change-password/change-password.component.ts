import { Component, OnInit } from '@angular/core';
import {ChangePasswordService} from 'app/dashboard/my-account/change-password/change-password.service';
import {AccountService} from 'app/core/auth/account.service';

@Component({
  selector: 'eh-change-password',
  templateUrl: './change-password.component.html'
})
export class ChangePasswordComponent implements OnInit {
  doNotMatch: string;
  error: string;
  success: string;
  account: any;
  currentPassword: string;
  newPassword: string;
  confirmPassword: string;

  constructor(private changePasswordService: ChangePasswordService, private accountService: AccountService) {}

  ngOnInit() {
    this.accountService.identity().subscribe(account => {
      this.account = account;
    });
  }

  changePassword() {
    if (this.newPassword !== this.confirmPassword) {
      this.error = null;
      this.success = null;
      this.doNotMatch = 'ERROR';
    } else {
      this.doNotMatch = null;
      this.changePasswordService.save(this.newPassword, this.currentPassword).subscribe(
        () => {
          this.error = null;
          this.success = 'OK';
        },
        () => {
          this.success = null;
          this.error = 'ERROR';
        }
      );
    }
  }
}
