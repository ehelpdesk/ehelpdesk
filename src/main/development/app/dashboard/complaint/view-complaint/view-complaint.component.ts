import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {AccountService} from 'app/core/auth/account.service';
import {ComplaintService} from 'app/dashboard/complaint/complaint.service';

@Component({
    selector: 'eh-view-complaint',
    templateUrl: './view-complaint.component.html',
    styles: []
})
export class ViewComplaintComponent implements OnInit {
    complaint: any;
    role: any;
    technicians: any;
    billAmount = 0;
    selectTechnician: any;

    constructor(private route: ActivatedRoute
                , private accountService: AccountService
                , private complaintService: ComplaintService) {
    }

    ngOnInit() {
        this.route.data.subscribe(({complaint}) => {
            this.complaint = complaint.body ? complaint.body : complaint;
        });

        this.accountService.identity().subscribe(account => {
            this.role = account.authority;
            if (this.role === 'ROLE_MANAGER') {
                this.complaintService.getTechnicians().subscribe(response => {
                    this.technicians = response;
                });
            }
        });
    }

    assignComplaint() {
        this.complaintService.assignComplaint({id: this.complaint.id, technician: this.selectTechnician}).subscribe(() => {
            this.previousState();
        });
    }

    previousState() {
        window.history.back();
    }

    resolveComplaint() {
        this.complaintService.resolveComplaint({id: this.complaint.id, billAmount: this.billAmount}).subscribe(() => {
            this.previousState();
        });
    }
}
