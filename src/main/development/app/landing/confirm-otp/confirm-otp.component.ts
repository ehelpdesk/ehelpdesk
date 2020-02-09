import {Component, OnInit} from '@angular/core';
import {FormBuilder} from '@angular/forms';
import {ActiveLoginConfirmationComponent} from 'app/landing/active-login-confirmation/active-login-confirmation.component';
import {LoginService} from 'app/landing/login/login.service';
import {ActivatedRoute, Router} from '@angular/router';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {EventManager, EventManagerConstants} from 'app/shared';
import {StateStorageService} from 'app/core/auth/state-storage.service';

@Component({
  selector: 'eh-confirm-otp',
  templateUrl: './confirm-otp.component.html',
  styles: []
})
export class ConfirmOtpComponent implements OnInit {
  otpForm = this.fb.group({
    otp: [''],
  });
  invalidOtp: boolean;
  noUserCreds: boolean;

  constructor(private fb: FormBuilder
    , private loginService: LoginService
    , private route: ActivatedRoute
    , private modalService: NgbModal
    , private router: Router
    , private eventManager: EventManager
    , private stateStorageService: StateStorageService
  ) {
  }

  ngOnInit() {
  }

  verifyOtp() {
    const otp = this.otpForm.get('otp').value;
    const userCredentials = this.loginService.userCredentials;
    if (userCredentials && userCredentials.username && userCredentials.password) {
      const originalUsername = userCredentials.username;
      userCredentials.username = originalUsername + '|||' + otp;
      this.login(userCredentials, originalUsername);
    } else {
      this.noUserCreds = true;
    }
  }

  login(userCredentials, originalUsername) {
    this.eventManager.broadcast({name: EventManagerConstants.SHOW_LOADER, content: true});
    this.loginService.hasActiveLogin(originalUsername).subscribe(response => {
      if (response) {
        this.eventManager.broadcast({name: EventManagerConstants.SHOW_LOADER, content: false});
        const modalRef = this.modalService.open(ActiveLoginConfirmationComponent, {backdrop: 'static'});
        modalRef.componentInstance.confirmation.subscribe(confirm => {
          if (confirm) {
            this.processLogin(userCredentials);
          }
        });
      } else {
        this.processLogin(userCredentials);
      }
    });
  }

  private processLogin(userCredentials) {
    this.eventManager.broadcast({name: EventManagerConstants.SHOW_LOADER, content: true});
    this.loginService
      .login(userCredentials)
      .subscribe(
        () => {
          this.eventManager.broadcast({name: EventManagerConstants.SHOW_LOADER, content: false});
          this.invalidOtp = false;
          if (
            this.router.url === '/account/register' ||
            this.router.url.startsWith('/account/activate') ||
            this.router.url.startsWith('/account/reset/')
          ) {
            this.router.navigate(['/dashboard']);
          }

          this.eventManager.broadcast({
            name: 'authenticationSuccess',
            content: 'Sending Authentication Success'
          });

          const redirect = this.stateStorageService.getUrl();
          if (redirect) {
            this.stateStorageService.storeUrl(null);
            this.router.navigateByUrl(redirect);
          }
          this.router.navigate(['/dashboard']);
        },
        () => {
          this.invalidOtp = true;
          this.eventManager.broadcast({name: EventManagerConstants.SHOW_LOADER, content: false});
        }
      );
  }

  navigateToLogin() {
    this.router.navigate(['/login']);
  }
}
