import {AfterViewInit, Component, ElementRef, OnInit, Renderer} from '@angular/core';
import {FormBuilder} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {EventManager, EventManagerConstants} from 'app/shared';
import {LoginService} from 'app/landing/login/login.service';
import {StateStorageService} from 'app/core/auth/state-storage.service';
import {map} from 'rxjs/operators';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {ActiveLoginConfirmationComponent} from 'app/landing/active-login-confirmation/active-login-confirmation.component';

@Component({
    selector: 'eh-login-component',
    templateUrl: './login.component.html'
})
export class LoginComponent implements AfterViewInit, OnInit {
    authenticationError: boolean;
    hasMultipleLogin: boolean;

    loginForm = this.fb.group({
        username: [''],
        password: [''],
        rememberMe: [false]
    });

    constructor(
        private eventManager: EventManager,
        private loginService: LoginService,
        private stateStorageService: StateStorageService,
        private elementRef: ElementRef,
        private renderer: Renderer,
        private router: Router,
        private fb: FormBuilder
        , private route: ActivatedRoute
        , private modalService: NgbModal
    ) {
    }

    ngAfterViewInit() {
        setTimeout(() => this.renderer.invokeElementMethod(this.elementRef.nativeElement.querySelector('#username'), 'focus', []), 0);
    }

    cancel() {
        this.authenticationError = false;
        this.loginForm.patchValue({
            username: '',
            password: ''
        });
    }

    login() {
        const credentials = {
            username: this.loginForm.get('username').value,
            password: this.loginForm.get('password').value,
            rememberMe: this.loginForm.get('rememberMe').value,
        };
        this.eventManager.broadcast({name: EventManagerConstants.SHOW_LOADER, content: true});
        this.loginService
            .verifyCredentials(credentials)
            .subscribe(
                (verificationResponse: any) => {
                    this.authenticationError = !verificationResponse.isValidUser;
                    if (!this.authenticationError) {
                        if (verificationResponse.isOtpCheckEnabled) {
                            this.eventManager.broadcast({name: EventManagerConstants.SHOW_LOADER, content: false});
                            this.loginService.userCredentials = credentials;
                            this.router.navigate(['/confirmOtp']);
                        } else {
                            this.processLogin(credentials);
                        }
                    } else {
                        this.eventManager.broadcast({name: EventManagerConstants.SHOW_LOADER, content: false});
                    }
                },
                () => {
                    this.eventManager.broadcast({name: EventManagerConstants.SHOW_LOADER, content: false});
                    this.authenticationError = true;
                }
            );
    }

    private processLogin(userCredentials) {
        this.eventManager.broadcast({name: EventManagerConstants.SHOW_LOADER, content: true});
        this.loginService
            .login(userCredentials)
            .subscribe(
                () => {
                    this.eventManager.broadcast({name: EventManagerConstants.SHOW_LOADER, content: false});

                    const redirect = this.stateStorageService.getUrl();
                    if (redirect) {
                        this.stateStorageService.storeUrl(null);
                        this.router.navigateByUrl(redirect);
                    }
                    this.router.navigate(['/dashboard']);
                },
                () => {
                    this.authenticationError = true;
                    this.eventManager.broadcast({name: EventManagerConstants.SHOW_LOADER, content: false});
                }
            );
    }
    register() {
        this.router.navigate(['/account/register']);
    }

    requestResetPassword() {
        this.router.navigate(['/account/reset', 'request']);
    }

    ngOnInit(): void {
        this.route
            .queryParamMap
            .pipe(map(params => params.get('multipleLogin') || 'false')).subscribe(multipleLogin => {
            this.hasMultipleLogin = multipleLogin === 'true';
        });
    }
}
