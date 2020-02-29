import { Component, OnInit } from '@angular/core';
import {ActivateService} from 'app/landing/activate/activate.service';
import {ActivatedRoute, Router} from '@angular/router';

@Component({
  selector: 'eh-activate',
  templateUrl: './activate.component.html',
  styles: []
})
export class ActivateComponent implements OnInit {
  error: string;
  success: string;

  constructor(private activateService: ActivateService, private route: ActivatedRoute, private router: Router) {}

  ngOnInit() {
    this.route.queryParams.subscribe(params => {
      this.activateService.get(params['key']).subscribe(
          () => {
            this.error = null;
            this.success = 'OK';
          },
          () => {
            this.success = null;
            this.error = 'ERROR';
          }
      );
    });
  }

    login() {
        this.router.navigate(['/landing/login']);
    }
}
