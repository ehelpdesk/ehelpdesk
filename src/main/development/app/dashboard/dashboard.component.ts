import {Component, OnDestroy, OnInit, Renderer2} from '@angular/core';
import { Router } from '@angular/router';
import {AccountService} from 'app/core/auth/account.service';

@Component({
    selector: 'qf-dashboard',
    templateUrl: './dashboard.component.html'
})
export class DashboardComponent implements OnInit, OnDestroy {
    constructor(private router: Router
                , private accountService: AccountService
                , private renderer2: Renderer2) {}

    ngOnInit() {
      this.accountService.isLoginActive().subscribe();
    }

  ngOnDestroy(): void {
  }
}
