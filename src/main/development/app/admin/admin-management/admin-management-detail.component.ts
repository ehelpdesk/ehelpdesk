import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { User } from 'app/model/user.model';

@Component({
    selector: 'eh-admin-mgmt-detail',
    templateUrl: './admin-management-detail.component.html'
})
export class AdminManagementDetailComponent implements OnInit {
    user: User;

    constructor(private route: ActivatedRoute) {}

    ngOnInit() {
        this.route.data.subscribe(({ user }) => {
            this.user = user.body ? user.body : user;
        });
    }
}
