import {Component, ElementRef, OnInit, Renderer} from '@angular/core';
import {EventManager} from 'app/shared';
import {LoginService} from 'app/landing/login/login.service';
import {StateStorageService} from 'app/core/auth/state-storage.service';
import {ActivatedRoute, Router} from '@angular/router';
import {FormBuilder} from '@angular/forms';
import {filter} from 'rxjs/operators';
import {ForgotUsernamePasswordService} from 'app/landing/fortgot-username-password/fortgot-username-password.service';

@Component({
  selector: 'eh-forgot-username-password',
  templateUrl: './forgot-username-password.component.html',
  styles: []
})
export class ForgotUsernamePasswordComponent implements OnInit {
  loginForm = this.fb.group({
    email: [''],
  });
  noEmailFound = false;
  isSuccess = false;
  isForgotUsername: boolean;
  isParamsResolved = false;

  constructor(
    private eventManager: EventManager,
    private loginService: LoginService,
    private stateStorageService: StateStorageService,
    private elementRef: ElementRef,
    private renderer: Renderer,
    private router: Router,
    private fb: FormBuilder
    , private route: ActivatedRoute
    , private forgotUsernamePasswordService: ForgotUsernamePasswordService
  ) {
  }

  ngOnInit() {
    this.route.queryParams.pipe(
      filter(params => params.type)
    ).subscribe(params => {
      this.isForgotUsername = params.type === 'forgotUsername';
      this.isParamsResolved = true;
    });
  }

  forgotUsernamePassword() {
    this.isSuccess = false;
    this.noEmailFound = false;
    if (this.isForgotUsername) {
      this.forgotUsernamePasswordService.sendUsername(this.loginForm.get('email').value).subscribe(() => {
        this.isSuccess = true;
      }, () => {
        this.noEmailFound = true;
      });
    } else {
      this.forgotUsernamePasswordService.sendPassword(this.loginForm.get('email').value).subscribe(() => {
        this.isSuccess = true;
      }, () => {
        this.noEmailFound = true;
      });
    }
  }

  navigateToLogin() {
    this.router.navigate(['/landing/login']);
  }
}
