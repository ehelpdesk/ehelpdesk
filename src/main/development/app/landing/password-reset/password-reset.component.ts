import {AfterViewInit, Component, ElementRef, OnInit, Renderer} from '@angular/core';
import {NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {ActivatedRoute, Router} from '@angular/router';
import {PasswordResetFinishService} from 'app/landing/password-reset/password-reset-finish.service';

@Component({
  selector: 'eh-password-reset-end',
  templateUrl: './password-reset.component.html',
  styles: []
})
export class PasswordResetComponent implements OnInit, AfterViewInit {
  confirmPassword: string;
  doNotMatch: string;
  error: string;
  keyMissing: boolean;
  resetAccount: any;
  success: string;
  modalRef: NgbModalRef;
  key: string;

  constructor(
    private passwordResetFinishService: PasswordResetFinishService,
    private route: ActivatedRoute,
    private elementRef: ElementRef,
    private renderer: Renderer,
    private router: Router
  ) {}

  ngOnInit() {
    this.route.queryParams.subscribe(params => {
      this.key = params['key'];
    });
    this.resetAccount = {};
    this.keyMissing = !this.key;
  }

  ngAfterViewInit() {
    if (this.elementRef.nativeElement.querySelector('#password') != null) {
      this.renderer.invokeElementMethod(this.elementRef.nativeElement.querySelector('#password'), 'focus', []);
    }
  }

  resetPassword() {
    if (this.success) {
      this.router.navigate(['/landing/login']);
    } else {
      this.doNotMatch = null;
      this.error = null;
      if (this.resetAccount.password !== this.confirmPassword) {
        this.doNotMatch = 'ERROR';
      } else {
        this.passwordResetFinishService.save({key: this.key, newPassword: this.resetAccount.password}).subscribe(
          () => {
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

  navigateToLogin() {
    this.router.navigate(['/landing/login']);
  }
}
