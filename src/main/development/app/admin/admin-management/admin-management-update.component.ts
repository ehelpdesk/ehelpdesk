import {Component, OnInit} from '@angular/core';
import {FormBuilder, Validators} from '@angular/forms';
import {ActivatedRoute} from '@angular/router';
import {User} from 'app/model/user.model';
import {UserService} from 'app/model/user.service';
import {AccountService} from 'app/core/auth/account.service';

@Component({
  selector: 'eh-admin-mgmt-update',
  templateUrl: './admin-management-update.component.html'
})
export class AdminManagementUpdateComponent implements OnInit {
  user: User;
  languages: any[];
  authorities: any[];
  isSaving: boolean;
  role: string;
  accessibleStates: any = [];

  editForm = this.fb.group({
    id: [null],
    login: ['', [Validators.required, Validators.minLength(1), Validators.maxLength(50), Validators.pattern('^[_.@A-Za-z0-9-]*')]],
    firstName: ['', [Validators.maxLength(50)]],
    lastName: ['', [Validators.maxLength(50)]],
    email: ['', [Validators.minLength(5), Validators.maxLength(254), Validators.email]],
    activated: [true],
    authority: ['', Validators.required],
  });
  login: string;

  constructor(private userService: UserService
    , private route: ActivatedRoute
    , private fb: FormBuilder
    , private accountService: AccountService) {
  }

  ngOnInit() {
    this.isSaving = false;
    this.route.data.subscribe(({user}) => {
      this.user = user.body ? user.body : user;
      this.updateForm(this.user);
    });
    this.userService.authority().subscribe(authority => {
      this.authorities = authority;
    });

    this.accountService.identity().subscribe(account => {
      this.role = account.authority;

    });
  }

  private updateForm(user: User): void {
    this.editForm.patchValue({
      id: user.id,
      login: user.login,
      firstName: user.firstName,
      lastName: user.lastName,
      email: user.email,
      activated: user.activated,
      authority: user.authority
  });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    this.updateUser(this.user);
    if (this.user.id !== null) {
      this.userService.update(this.user).subscribe(response => this.onSaveSuccess(response), () => this.onSaveError());
    } else {
      this.userService.create(this.user).subscribe(response => this.onSaveSuccess(response), () => this.onSaveError());
    }
  }

  private updateUser(user: User): void {
    user.login = this.editForm.get(['login']).value;
    user.firstName = this.editForm.get(['firstName']).value;
    user.lastName = this.editForm.get(['lastName']).value;
    user.email = this.editForm.get(['email']).value;
    user.activated = this.editForm.get(['activated']).value;
    user.authority = this.editForm.get(['authority']).value;
  }

  private onSaveSuccess(result) {
    this.isSaving = false;
    this.previousState();
  }

  private onSaveError() {
    this.isSaving = false;
  }

  isCurrentUserNotSuperAdmin() {
    return this.role !== 'ROLE_SUPER_ADMIN';
  }

  private isAdminNotSelected() {
    return this.user.authority !== 'ROLE_ADMIN';
  }

}
