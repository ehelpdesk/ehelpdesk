import {Component, OnInit} from '@angular/core';
import {FormBuilder, Validators} from '@angular/forms';
import {ActivatedRoute} from '@angular/router';
import {User} from 'app/model/user.model';
import {UserService} from 'app/model/user.service';
import {AccountService} from 'app/core/auth/account.service';
import {MasterService} from 'app/shared/service/master.service';

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
    langKey: [],
    authority: ['', Validators.required],
    owner: [''],
    accessibleStates: ['']
  });
  login: string;

  constructor(private userService: UserService
    , private route: ActivatedRoute
    , private fb: FormBuilder
    , private accountService: AccountService
    , private masterService: MasterService) {
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

      this.editForm.get(['owner']).setValue(account.login);
      if (this.isCurrentUserNotSuperAdmin()) {
        this.editForm.get(['owner']).disable();
      }
    });
    this.getAvailableStates();
  }

  private updateForm(user: User): void {
    this.editForm.patchValue({
      id: user.id,
      login: user.login,
      firstName: user.firstName,
      lastName: user.lastName,
      email: user.email,
      activated: user.activated,
      langKey: user.langKey,
      authority: user.authority,
      owner: user.owner,
      accessibleStates: user.accessibleStates
  })
    ;
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
    user.langKey = this.editForm.get(['langKey']).value;
    user.authority = this.editForm.get(['authority']).value;
    user.owner = this.editForm.get(['owner']).value;
    user.accessibleStates = this.editForm.get(['accessibleStates']).value;
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

  getAvailableStates() {
    this.masterService.getAvailableStates().subscribe(response => this.accessibleStates = response);
  }

  private isAdminNotSelected() {
    return this.user.authority !== 'ROLE_ADMIN';
  }

  isSelected(stateName: string) {
    if (this.user.accessibleStates) {
      this.user.accessibleStates.forEach(accessibleState => {
        return accessibleState.name === stateName;
      });
    }
  }
}
